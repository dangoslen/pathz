package me.dangoslen.pathz.api;

import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.repository.ProjectRepository;
import me.dangoslen.pathz.repository.TeamMatesRepository;
import me.dangoslen.pathz.repository.TeamsRepository;
import me.dangoslen.pathz.service.DefaultTeamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private static final String NUMBER = "+18303214537";

    private final AtomicInteger atomicInteger;
    private final ProjectRepository projectRepository;
    private final TeamsRepository teamsRepository;
    private final TeamMatesRepository teamMatesRepository;

    @Autowired
    ProjectsController(ProjectRepository projectRepository, TeamsRepository teamsRepository, TeamMatesRepository teamMatesRepository) {
        this.atomicInteger = new AtomicInteger();
        this.projectRepository = projectRepository;
        this.teamsRepository = teamsRepository;
        this.teamMatesRepository = teamMatesRepository;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Optional<TeamMate> projectManager = teamMatesRepository.getTeamMate(project.getProjectManagerHandle());
        if (!projectManager.isPresent()) {
            return ResponseEntity.badRequest().body(project);
        } else {
            project.setProjectManager(projectManager.get());
        }

        int projectId = atomicInteger.incrementAndGet();
        Team defaultTeam = DefaultTeamFactory.buildDefaultTeam(projectId);
        project.setId(projectId);
        project.setPhoneNumber(NUMBER);
        project.addTeam(defaultTeam);

        projectRepository.saveProject(project);
        teamsRepository.saveProjectTeam(project, defaultTeam);

        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") int id) {
        Project project = projectRepository.getProject(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Collection<Team>> getProjectTeams(@PathVariable("id") int id) {
        Project project = projectRepository.getProject(id);
        return ResponseEntity.ok(project.getTeams());
    }

    @PostMapping("/{id}/teams")
    public ResponseEntity<Team> createTeam(@PathVariable("id") int id, @RequestBody Team team) {
        Project project = projectRepository.getProject(id);
        teamsRepository.saveProjectTeam(project, team);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{id}/teams/{handle}")
    public ResponseEntity<Team> getProjectTeams(@PathVariable("id") int id, @PathVariable("handle") String handle) {
        Project project = projectRepository.getProject(id);
        Team team = teamsRepository.getProjectTeam(project, handle);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{id}/teams/{handle}/teammates")
    public ResponseEntity<Collection<TeamMate>> getProjectTeamTeammates(@PathVariable("id") int id, @PathVariable("handle") String handle) {
        Project project = projectRepository.getProject(id);
        Team team = teamsRepository.getProjectTeam(project, handle);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(team.getTeammates());
    }

    @PutMapping("/{id}/teams/{handle}/teammates/{teammate}")
    public ResponseEntity<Team> addTeammateToTeam(
            @PathVariable("id") int id, @PathVariable("handle") String handle, @PathVariable("teammate") String teammateHandle) {
        Project project = projectRepository.getProject(id);
        Team team = teamsRepository.getProjectTeam(project, handle);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<TeamMate> teamMate = teamMatesRepository.getTeamMate(teammateHandle);
        if (!teamMate.isPresent()) {
            return ResponseEntity.badRequest().body(team);
        }

        team.addTeamMate(teamMate.get());
        teamsRepository.saveProjectTeam(project, team);

        return ResponseEntity.ok(team);
    }

}
