package org.poolc.api.kubernetes.repository;

import org.poolc.api.kubernetes.domain.KubernetesMapping;
import org.poolc.api.kubernetes.dto.ActiveMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface KubernetesRepository extends JpaRepository<KubernetesMapping, Long> {


    @Query(value = "SELECT DISTINCT T2.MEMBER_UUID\n" +
            "   , (SELECT login_id" +
            "       FROM MEMBER T1" +
            "       WHERE T1.UUID = T2.MEMBER_UUID)\n" +
            "FROM ROLES T2\n" +
            "WHERE T2.MEMBER_UUID IN (\n" +
            "    SELECT MEMBER_UUID\n" +
            "    FROM ROLES\n" +
            "    WHERE ROLES = 'MEMBER'\n" +
            ")\n" +
            "AND T2.MEMBER_UUID NOT IN (\n" +
            "    SELECT MEMBER_UUID\n" +
            "    FROM ROLES\n" +
            "    WHERE ROLES = 'INACTIVE'\n" +
            ")", nativeQuery = true)
    List<ActiveMemberDto> findAllActiveMembers();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE KUBERNETES_MAPPINGS", nativeQuery = true)
    void truncateKubernetesMappingTable();

    @Query(value = "SELECT kubernetesKey FROM kubernetes_mappings WHERE UUID IN (SELECT UUID FROM Member WHERE loginID = :userId)")
    Optional<String> findKubernetesKeyByUserId(String userId);
}
