package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.member.domain.Member;
import org.poolc.api.project.dto.ProjectResponse;

import java.io.Serializable;
import java.util.List;

@Getter
public class MemberResponse implements Serializable {
    private final String loginID;
    private final String email;
    private final String phoneNumber;
    private final String name;
    private final String department;
    private final String studentID;
    private final String profileImageURL;
    private final String introduction;
    private final Boolean isActivated;
    private final Boolean isAdmin;
    private final List<ActivityResponse> hostActivities;
    private final List<ActivityResponse> participantActivities;
    private final List<ProjectResponse> projects;
    private String role;
    private final Badge badge;

    @JsonCreator
    public MemberResponse(String loginID, String email, String phoneNumber, String name, String department, String studentID, String profileImageURL, String introduction, Boolean isActivated, Boolean isAdmin, List<ActivityResponse> hostActivities, List<ActivityResponse> participantActivities, List<ProjectResponse> projects, String role, Badge badge) {
        this.loginID = loginID;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
        this.isActivated = isActivated;
        this.isAdmin = isAdmin;
        this.hostActivities = hostActivities;
        this.participantActivities = participantActivities;
        this.projects = projects;
        this.role = role;
        this.badge = badge;
    }

    public MemberResponse(Member member, boolean isAdmin) {
        if (isAdmin) {
            this.loginID = member.getLoginID();
            this.email = null;
            this.phoneNumber = member.getPhoneNumber();
            this.name = member.getName();
            this.department = member.getDepartment();
            this.studentID = member.getStudentID();
            this.profileImageURL = null;
            this.introduction = null;
            this.isActivated = null;
            this.isAdmin = null;
            this.hostActivities = null;
            this.participantActivities = null;
            this.projects = null;
            this.role = null;
            this.badge = member.getBadge();
        } else {
            this.loginID = member.getLoginID();
            this.email = null;
            this.phoneNumber = null;
            this.name = null;
            this.department = null;
            this.studentID = null;
            this.profileImageURL = null;
            this.introduction = null;
            this.isActivated = null;
            this.isAdmin = null;
            this.hostActivities = null;
            this.participantActivities = null;
            this.projects = null;
            this.role = null;
            this.badge = null;
        }

    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getLoginID(), member.getEmail(), member.getPhoneNumber(), member.getName(), member.getDepartment(), member.getStudentID(), member.getProfileImageURL(), member.getIntroduction(), member.isMember(), member.isAdmin(), null, null, null, member.getRole(), member.getBadge());
    }

    public static MemberResponse of(Member findMember, Member loginMember,
                                    List<ActivityResponse> hostActivities,
                                    List<ActivityResponse> participantActivities,
                                    List<ProjectResponse> projects) {
        if(findMember.equals(loginMember)) {
            return new MemberResponse(findMember.getLoginID(), findMember.getEmail(), findMember.getPhoneNumber(), findMember.getName(), findMember.getDepartment(), findMember.getStudentID(), findMember.getProfileImageURL(), findMember.getIntroduction(), findMember.isMember(), findMember.isAdmin(), hostActivities, participantActivities, projects, findMember.getRole(),  findMember.getBadge());
        }else{
            return new MemberResponse(findMember.getLoginID(), null, null, findMember.getName(), findMember.getDepartment(), null, findMember.getProfileImageURL(), findMember.getIntroduction(), findMember.isMember(), findMember.isAdmin(), hostActivities, participantActivities, projects, findMember.getRole(),  findMember.getBadge());
        }
    }
}
