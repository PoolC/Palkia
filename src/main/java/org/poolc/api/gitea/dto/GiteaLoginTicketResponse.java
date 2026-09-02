package org.poolc.api.gitea.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class GiteaLoginTicketResponse {
    private final String ticket;
    private final Instant expiresAt;

    @JsonCreator
    public GiteaLoginTicketResponse(
            @JsonProperty("ticket") String ticket,
            @JsonProperty("expiresAt") Instant expiresAt
    ) {
        this.ticket = ticket;
        this.expiresAt = expiresAt;
    }
}
