package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import java.math.BigDecimal;

@Getter
public class MemberResponseWithHour {

    private final MemberResponse member;
    private final BigDecimal hour;
    private final Boolean isExcepted;

    @JsonCreator
    public MemberResponseWithHour(MemberResponse member, BigDecimal hour, Boolean isExcepted) {
        this.member = member;
        this.hour = hour;
        this.isExcepted = isExcepted;
    }

    public static MemberResponseWithHour of(Member member, BigDecimal hour) {
        return new MemberResponseWithHour(MemberResponse.of(member), hour, member.getIsExcepted());
    }
}
