package org.poolc.api.kubernetes.repository;

import org.poolc.api.kubernetes.domain.KubernetesMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface KubernetesRepository extends JpaRepository<KubernetesMapping, Long> {


    @Query(value = "SELECT DISTINCT MEMBER_UUID\n" +
            "FROM ROLES\n" +
            "WHERE MEMBER_UUID IN (\n" +
            "    SELECT MEMBER_UUID\n" +
            "    FROM ROLES\n" +
            "    WHERE ROLES = 'MEMBER'\n" +
            ")\n" +
            "AND MEMBER_UUID NOT IN (\n" +
            "    SELECT MEMBER_UUID\n" +
            "    FROM ROLES\n" +
            "    WHERE ROLES = 'INACTIVE'\n" +
            ")", nativeQuery = true)
    List<String> findAllActiveMembers();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE KUBERNETES_MAPPINGS", nativeQuery = true)
    void truncateKubernetesMappingTable();

    @Query(value = "SELECT kubernetesKey FROM kubernetes_mappings WHERE UUID IN (SELECT UUID FROM Member WHERE loginID = :userId)")
    Optional<String> findKubernetesKeyByUserId(String userId);
}
