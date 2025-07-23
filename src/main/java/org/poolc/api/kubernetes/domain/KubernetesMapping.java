package org.poolc.api.kubernetes.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity(name = "kubernetes_mappings")
public class KubernetesMapping {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_uuid",nullable = false)
    private String UUID;

    @Column(name = "kubernetes_key", nullable = false)
    private String kubernetesKey;

    @Builder
    public KubernetesMapping(String UUID, String kubernetesKey) {
        this.UUID = UUID;
        this.kubernetesKey = kubernetesKey;
    }

    protected KubernetesMapping() {

    }
}
