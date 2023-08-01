package org.poolc.api.mail.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class MailDto {

    @Email
    private String receiver;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String text;
}

