package me.dangoslen.pathz.api;

import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.repository.ProjectRepository;
import me.dangoslen.pathz.service.DefaultTeamFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private static final String NUMBER = "+18303214537";

    private final AtomicInteger atomicInteger;
    private final ProjectRepository projectRepository;

    ProjectsController(ProjectRepository projectRepository) {
        this.atomicInteger = new AtomicInteger();
        this.projectRepository = projectRepository;
    }


    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Team defaultTeam = DefaultTeamFactory.buildDefaultTeam();
        project.setPhoneNumber(NUMBER);
        project.setId(atomicInteger.incrementAndGet());
        project.addTeam(defaultTeam);

        projectRepository.saveProject(project);


        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") int id) {
        Project project = projectRepository.getProject(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Collection<Team>> getProjectTeams(@PathVariable("id") int id) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/{id}/teams")
    public ResponseEntity<Team> createTeam(@PathVariable("id") int id, @RequestBody Team team) {
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{id}/teams/{handle}")
    public ResponseEntity<Team> getProjectTeams(@PathVariable("handle") String handle) {
        return ResponseEntity.ok(new Team());
    }

    @PutMapping("/{id}/teams/{handle}/teammates/{teammate}")
    public ResponseEntity<Team> addTeammateToTeam(
            @PathVariable("handle") String handle, @PathVariable("teammate") String teammateHandle) {
        return ResponseEntity.ok(new Team());
    }

}
