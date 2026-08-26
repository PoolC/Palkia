package org.poolc.api.project.vo;

import lombok.Getter;
import org.poolc.api.project.dto.RegisterProjectRequest;
import org.poolc.api.project.domain.ProjectCategory;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ProjectCreateValues {

    private final String name;
    private final String description;
    private final String genre;
    private final ProjectCategory category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String thumbnailURL;
    private final String body;
    List<String> memberLoginIDs;

    public ProjectCreateValues(RegisterProjectRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.genre = request.getGenre();
        this.category = request.getCategory();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.thumbnailURL = request.getThumbnailURL();
        this.body = request.getBody();
        this.memberLoginIDs = request.getMemberLoginIDs();
    }
}
