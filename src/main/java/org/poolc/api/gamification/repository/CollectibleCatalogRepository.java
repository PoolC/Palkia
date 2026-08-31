package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectibleCatalogRepository extends JpaRepository<CollectibleCatalog, Long> {
    Optional<CollectibleCatalog> findByExternalId(Long externalId);
    List<CollectibleCatalog> findAllByOrderByGenerationAscExternalIdAsc();
    List<CollectibleCatalog> findByRarityAndEnabledTrue(CollectibleRarity rarity);

    @Query("select c from CollectibleCatalog c "
            + "where c.rarity = :rarity and c.enabled = true "
            + "and (:shiny = false or exists (select normalDraw.id from CollectionDraw normalDraw "
            + "where normalDraw.collectible = c and normalDraw.member.UUID = :memberUuid and normalDraw.shiny = false)) "
            + "and not exists (select d.id from CollectionDraw d where d.collectible = c and d.member.UUID = :memberUuid and d.shiny = :shiny) "
            + "order by function('random')")
    List<CollectibleCatalog> findUncollectedVariantByMemberUuidAndRarity(
            @Param("memberUuid") String memberUuid,
            @Param("rarity") CollectibleRarity rarity,
            @Param("shiny") boolean shiny,
            Pageable pageable);

    @Query("select case when count(c) > 0 then true else false end from CollectibleCatalog c "
            + "where c.rarity = :rarity and c.enabled = true "
            + "and (:shiny = false or exists (select normalDraw.id from CollectionDraw normalDraw "
            + "where normalDraw.collectible = c and normalDraw.member.UUID = :memberUuid and normalDraw.shiny = false)) "
            + "and not exists (select d.id from CollectionDraw d where d.collectible = c and d.member.UUID = :memberUuid and d.shiny = :shiny)")
    boolean existsUncollectedVariantByMemberUuidAndRarity(
            @Param("memberUuid") String memberUuid,
            @Param("rarity") CollectibleRarity rarity,
            @Param("shiny") boolean shiny);

    @Query("select case when count(c) > 0 then true else false end from CollectibleCatalog c "
            + "where c.enabled = true "
            + "and exists (select normalDraw.id from CollectionDraw normalDraw "
            + "where normalDraw.collectible = c and normalDraw.member.UUID = :memberUuid and normalDraw.shiny = false) "
            + "and not exists (select shinyDraw.id from CollectionDraw shinyDraw "
            + "where shinyDraw.collectible = c and shinyDraw.member.UUID = :memberUuid and shinyDraw.shiny = true)")
    boolean existsUncollectedShinyVariantForMember(@Param("memberUuid") String memberUuid);

    long countByEnabledTrue();
}
