package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;
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
            + "and not exists (select d.id from CollectionDraw d where d.collectible = c and d.member.UUID = :memberUuid)")
    List<CollectibleCatalog> findUncollectedByMemberUuidAndRarity(
            @Param("memberUuid") String memberUuid,
            @Param("rarity") CollectibleRarity rarity);

    long countByEnabledTrue();
}
