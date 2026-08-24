package org.poolc.api.gitea.dto;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class GiteaUserHeaders {
    private final String loginID;
    private final String email;
    private final String name;
    private final boolean admin;

    private GiteaUserHeaders(String loginID, String email, String name, boolean admin) {
        this.loginID = loginID;
        this.email = email;
        this.name = name;
        this.admin = admin;
    }

    public static GiteaUserHeaders of(Member member) {
        return new GiteaUserHeaders(
                member.getLoginID(),
                member.getEmail(),
                member.getName(),
                member.isAdmin()
        );
    }
}
