package org.poolc.api.project.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.service.MemberResponseAssembler;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.domain.ProjectCategory;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.dto.RegisterProjectRequest;
import org.poolc.api.project.dto.UpdateProjectRequest;
import org.poolc.api.project.service.ProjectService;
import org.poolc.api.project.vo.ProjectCreateValues;
import org.poolc.api.project.vo.ProjectUpdateValues;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MemberRepository memberRepository;
    private final MemberResponseAssembler memberResponseAssembler;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewProject(@RequestBody @Valid RegisterProjectRequest requestBody) {
        projectService.createProject(new ProjectCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<ProjectResponse>>> findProjects(@RequestParam(required = false) ProjectCategory category) {
        HashMap<String, List<ProjectResponse>> responseBody = new HashMap<>() {{
            put("data", projectService.findProjects(category).stream()
                    .map(p -> ProjectResponse.of(p, new ArrayList<>()))
                    .collect(Collectors.toList()));
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ProjectResponse>> findOneProject(@PathVariable Long projectID) {
        Project project = projectService.findOne(projectID);
        List<Member> members = memberRepository.findAllMembersByLoginIDList(project.getMemberLoginIDs());
        return ResponseEntity.ok().body(Collections.singletonMap("data", ProjectResponse.ofWithMemberResponses(project, memberResponseAssembler.ofAll(members))));
    }

    @PutMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProject(@RequestBody @Valid UpdateProjectRequest requestBody, @PathVariable Long projectID) {
        projectService.updateProject(new ProjectUpdateValues(requestBody), projectID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{projectID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteOneProject(@PathVariable Long projectID) {
        projectService.deleteProject(projectID);
        return ResponseEntity.ok().build();
    }
}
