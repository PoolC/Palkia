package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.domain.ProjectCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    private final Long id;
    private final String name;
    private final String body;
    private final String description;
    private final String genre;
    private final ProjectCategory category;
    private final String duration;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String thumbnailURL;
    private final List<MemberResponse> members;

    @JsonCreator
    public ProjectResponse(Long id, String name, String body, String description, String genre, ProjectCategory category, String duration, LocalDate startDate, LocalDate endDate, String thumbnailURL, List<MemberResponse> members) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.description = description;
        this.genre = genre;
        this.category = category;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailURL = thumbnailURL;
        this.members = members;
    }

    public static ProjectResponse of(Project project, List<Member> members) {
        return ofWithMemberResponses(project, members.stream().map(MemberResponse::of).collect(Collectors.toList()));
    }

    public static ProjectResponse ofWithMemberResponses(Project project, List<MemberResponse> members) {
        return new ProjectResponse(project.getId(), project.getName(), project.getBody(), project.getDescription(),
                project.getGenre(), project.getCategory(), project.getDuration(), project.getStartDate(), project.getEndDate(), project.getThumbnailURL(), members);
    }
}
