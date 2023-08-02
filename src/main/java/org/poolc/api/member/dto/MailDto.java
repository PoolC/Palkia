package org.poolc.api.member.dto;

import lombok.Getter;

@Getter
public class MailDto {
    private String subject;
    private String text;
    private String receiver;
}
