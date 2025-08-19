package org.poolc.api.kubernetes.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.kubernetes.domain.KubernetesMapping;
import org.poolc.api.kubernetes.dto.ActiveMemberDto;
import org.poolc.api.kubernetes.repository.KubernetesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KubernetesService {
    private final KubernetesRepository kubernetesRepository;

    @Value("${kubernetes.api.key}")
    private String API_KEY;

    public List<ActiveMemberDto> getAllActiveMembers(String apiKey) {
        if(!isValidApiKey(apiKey)) {
            throw new IllegalArgumentException("Invalid API key");
        }
        return kubernetesRepository.findAllActiveMembers();
    }

    @Transactional
    public void refreshMemberKey(Map<String,String> requestBody, String apiKey) {
        if(!isValidApiKey(apiKey)) {
            throw new IllegalArgumentException("Invalid API key");
        }

        kubernetesRepository.truncateKubernetesMappingTable();

        List<KubernetesMapping> mappings = requestBody.entrySet().stream()
                .map(entry -> KubernetesMapping.builder()
                        .UUID(entry.getKey())
                        .kubernetesKey(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        kubernetesRepository.saveAll(mappings);
    }

    public String getKubernetesKeyByUserId(String userId) {
        return kubernetesRepository.findKubernetesKeyByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Kubernetes key found for user: " + userId));
    }

    private boolean isValidApiKey(String apiKey) {
        return API_KEY.equals(apiKey);
    }
}
