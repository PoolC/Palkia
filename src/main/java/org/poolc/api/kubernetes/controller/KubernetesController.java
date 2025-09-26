package org.poolc.api.kubernetes.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.kubernetes.dto.GetKubernetesResponseDto;
import org.poolc.api.kubernetes.dto.GetMyKubernetesKeyResponseDto;
import org.poolc.api.kubernetes.service.KubernetesService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kubernetes")
@RequiredArgsConstructor
public class KubernetesController {

    private final KubernetesService kubernetesService;

    @GetMapping(value="/")
    public ResponseEntity<GetKubernetesResponseDto> getAllActiveMembers(@RequestHeader("X-API-KEY") String apiKey){

        GetKubernetesResponseDto response = new GetKubernetesResponseDto(
                kubernetesService.getAllActiveMembers(apiKey)
        );
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value="/")
    public ResponseEntity<Void> refreshMamberKeys(@RequestHeader("X-API-KEY") String apiKey, @RequestBody Map<String,String> requestBody) {

        kubernetesService.refreshMemberKey(requestBody, apiKey);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/me")
    public ResponseEntity<GetMyKubernetesKeyResponseDto> getMyKey(@AuthenticationPrincipal Member loginMember){
        GetMyKubernetesKeyResponseDto response = new GetMyKubernetesKeyResponseDto(
                kubernetesService.getKubernetesKeyByUUID(loginMember.getUUID())
        );
        return ResponseEntity.ok().body(response);
    }

}
