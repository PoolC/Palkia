package org.poolc.api.gamification.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.gamification.domain.CatalogSyncRun;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;
import org.poolc.api.gamification.domain.SyncStatus;
import org.poolc.api.gamification.repository.CatalogSyncRunRepository;
import org.poolc.api.gamification.repository.CollectibleCatalogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogSyncService {
    private final CatalogSyncRunRepository syncRunRepository;
    private final CollectibleCatalogRepository catalogRepository;
    private final RestTemplate restTemplate = createRestTemplate();
    private final Map<String, String> abilityNamesKo = new HashMap<>();

    @Value("${gamification.pokeapi.base-url:https://pokeapi.co/api/v2}")
    private String pokeApiBaseUrl;

    @Transactional
    public CatalogSyncRun startSync() {
        syncRunRepository.findTopByOrderByStartedAtDesc()
                .filter(run -> run.getStatus() == SyncStatus.RUNNING)
                .ifPresent(run -> {
                    throw new ConflictException("이미 도감 동기화가 진행 중입니다.");
                });

        return syncRunRepository.saveAndFlush(new CatalogSyncRun());
    }

    public Optional<CatalogSyncRun> getLatestRun() {
        return syncRunRepository.findTopByOrderByStartedAtDesc();
    }

    public void synchronize(Long runId) {
        CatalogSyncRun run = syncRunRepository.findById(runId)
                .orElseThrow(() -> new NoSuchElementException("도감 동기화 실행 정보를 찾을 수 없습니다."));
        try {
            JsonNode speciesList = getJson(pokeApiBaseUrl + "/pokemon-species?limit=2000");
            int processedCount = 0;
            for (JsonNode resource : speciesList.path("results")) {
                JsonNode species = getJson(resource.path("url").asText());
                JsonNode defaultVariety = findDefaultVariety(species);
                if (defaultVariety == null) {
                    continue;
                }
                JsonNode pokemon = getJson(defaultVariety.path("pokemon").path("url").asText());
                upsertCatalog(species, pokemon);
                processedCount++;
            }
            run.complete(processedCount);
            syncRunRepository.save(run);
        } catch (Exception exception) {
            run.fail(exception.getMessage());
            syncRunRepository.save(run);
        }
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(15000);
        return new RestTemplate(requestFactory);
    }

    private JsonNode getJson(String url) {
        JsonNode body = restTemplate.getForObject(url, JsonNode.class);
        if (body == null) {
            throw new IllegalStateException("PokeAPI 응답이 비어 있습니다.");
        }
        return body;
    }

    private void upsertCatalog(JsonNode species, JsonNode pokemon) {
        Long externalId = species.path("id").asLong();
        String slug = species.path("name").asText();
        String nameKo = localizedName(species, "ko", slug);
        Integer generation = generationNumber(species.path("generation").path("name").asText());
        String types = pokemonTypes(pokemon);
        CollectibleRarity rarity = determineRarity(species);
        String categoryKo = localizedGenus(species, "ko");
        String descriptionKo = localizedFlavorText(species, "ko");
        String spriteUrl = pokemon.path("sprites").path("other").path("official-artwork").path("front_default").asText(null);
        if (spriteUrl == null || spriteUrl.isBlank()) {
            spriteUrl = pokemon.path("sprites").path("front_default").asText(null);
        }
        String shinySpriteUrl = pokemon.path("sprites").path("other").path("official-artwork").path("front_shiny").asText(null);
        if (shinySpriteUrl == null || shinySpriteUrl.isBlank()) {
            shinySpriteUrl = pokemon.path("sprites").path("front_shiny").asText(null);
        }
        final String importedSpriteUrl = spriteUrl;
        final String importedShinySpriteUrl = shinySpriteUrl;
        Integer heightDecimeters = pokemon.path("height").isInt() ? pokemon.path("height").asInt() : null;
        Integer weightHectograms = pokemon.path("weight").isInt() ? pokemon.path("weight").asInt() : null;
        String abilities = pokemonAbilities(pokemon);
        Map<String, Integer> stats = pokemonStats(pokemon);

        CollectibleCatalog collectible = catalogRepository.findByExternalId(externalId)
                .orElseGet(() -> new CollectibleCatalog(externalId, slug, nameKo, generation, types, rarity, importedSpriteUrl, importedShinySpriteUrl,
                        categoryKo, descriptionKo, heightDecimeters, weightHectograms, abilities,
                        stats.get("hp"), stats.get("attack"), stats.get("defense"), stats.get("special-attack"), stats.get("special-defense"), stats.get("speed")));
        if (collectible.getId() != null) {
            collectible.updateFromImport(slug, nameKo, generation, types, rarity, importedSpriteUrl, importedShinySpriteUrl,
                    categoryKo, descriptionKo, heightDecimeters, weightHectograms, abilities,
                    stats.get("hp"), stats.get("attack"), stats.get("defense"), stats.get("special-attack"), stats.get("special-defense"), stats.get("speed"));
        }
        catalogRepository.save(collectible);
    }

    private JsonNode findDefaultVariety(JsonNode species) {
        for (JsonNode variety : species.path("varieties")) {
            if (variety.path("is_default").asBoolean()) {
                return variety;
            }
        }
        return null;
    }

    private String localizedName(JsonNode species, String language, String fallback) {
        for (JsonNode name : species.path("names")) {
            if (language.equals(name.path("language").path("name").asText())) {
                return name.path("name").asText(fallback);
            }
        }
        return fallback;
    }

    private String localizedGenus(JsonNode species, String language) {
        for (JsonNode genus : species.path("genera")) {
            if (language.equals(genus.path("language").path("name").asText())) {
                return genus.path("genus").asText(null);
            }
        }
        return null;
    }

    private String localizedFlavorText(JsonNode species, String language) {
        String flavorText = null;
        for (JsonNode entry : species.path("flavor_text_entries")) {
            if (language.equals(entry.path("language").path("name").asText())) {
                flavorText = entry.path("flavor_text").asText(null);
            }
        }
        return flavorText == null ? null : flavorText.replaceAll("[\\n\\f]", " ").trim();
    }

    private Integer generationNumber(String generation) {
        switch (generation) {
            case "generation-i": return 1;
            case "generation-ii": return 2;
            case "generation-iii": return 3;
            case "generation-iv": return 4;
            case "generation-v": return 5;
            case "generation-vi": return 6;
            case "generation-vii": return 7;
            case "generation-viii": return 8;
            case "generation-ix": return 9;
            default: return 0;
        }
    }

    private String pokemonTypes(JsonNode pokemon) {
        List<JsonNode> typeNodes = new ArrayList<>();
        pokemon.path("types").forEach(typeNodes::add);
        return typeNodes.stream()
                .sorted(Comparator.comparing(node -> node.path("slot").asInt()))
                .map(node -> node.path("type").path("name").asText())
                .collect(Collectors.joining(","));
    }

    private String pokemonAbilities(JsonNode pokemon) {
        List<JsonNode> abilityNodes = new ArrayList<>();
        pokemon.path("abilities").forEach(abilityNodes::add);
        return abilityNodes.stream()
                .filter(node -> !node.path("is_hidden").asBoolean())
                .sorted(Comparator.comparing(node -> node.path("slot").asInt()))
                .map(node -> localizedAbilityName(node.path("ability")))
                .collect(Collectors.joining(", "));
    }

    private String localizedAbilityName(JsonNode ability) {
        String url = ability.path("url").asText();
        String fallback = ability.path("name").asText();
        if (url.isBlank()) {
            return fallback;
        }
        return abilityNamesKo.computeIfAbsent(url, key -> localizedName(getJson(key), "ko", fallback));
    }

    private Map<String, Integer> pokemonStats(JsonNode pokemon) {
        Map<String, Integer> stats = new HashMap<>();
        for (JsonNode stat : pokemon.path("stats")) {
            stats.put(stat.path("stat").path("name").asText(), stat.path("base_stat").asInt());
        }
        return stats;
    }

    private CollectibleRarity determineRarity(JsonNode species) {
        if (species.path("is_legendary").asBoolean() || species.path("is_mythical").asBoolean()) {
            return CollectibleRarity.LEGENDARY;
        }
        int captureRate = species.path("capture_rate").asInt(255);
        if (captureRate <= 30) {
            return CollectibleRarity.EPIC;
        }
        if (captureRate <= 90) {
            return CollectibleRarity.RARE;
        }
        return CollectibleRarity.COMMON;
    }
}
