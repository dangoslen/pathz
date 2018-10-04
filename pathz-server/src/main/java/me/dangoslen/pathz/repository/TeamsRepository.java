package me.dangoslen.pathz.repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.service.TeamMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static me.dangoslen.pathz.config.Variables.DEFAULT_TEAM_HANDLE;

@Component
public class TeamsRepository {

    private Multimap<Integer, Team> projectTeams;
    private Multimap<String, Team> userTeams;
    private ConcurrentHashMap<String, TeamMessageHandler> messageHandlers;

    TeamsRepository() {
        this.projectTeams = ArrayListMultimap.create();
        this.userTeams = ArrayListMultimap.create();
        this.messageHandlers = new ConcurrentHashMap<>();
    }

    public void saveProjectTeam(Project project, Team team, TeamMessageHandler messageHandler) {
        projectTeams.put(project.getId(), team);
        messageHandlers.put(team.getHandle(), messageHandler);
        for(TeamMate mate : team.getTeammates()) {
            Collection<Team> teams = userTeams.get(mate.getHandle());
            if (!teams.contains(team)) {
                userTeams.put(mate.getHandle(), team);
            }
        }
    }

    public void saveProjectTeam(Project project, Team team) {
        TeamMessageHandler messageHandler = messageHandlers.get(team.getHandle());
        if (messageHandler == null) {
            messageHandler = new TeamMessageHandler.DefaultMessageHandler();
        }
        saveProjectTeam(project, team, messageHandler);
    }

    public Collection<Team> getProjectTeams(Project project) {
        return projectTeams.get(project.getId());
    }

    public Collection<TeamMate> getProjectTeammates(Project project) {
        return getProjectTeammates(project, DEFAULT_TEAM_HANDLE);
    }

    public Optional<Team> getProjectTeam(Project project, String handle) {
        return getProjectTeams(project).stream()
                .filter((team -> team.getHandle().equalsIgnoreCase(handle)))
                .findFirst();
    }

    public Collection<TeamMate> getProjectTeammates(Project project, String handle) {
        Optional<Team> team = getProjectTeam(project, handle);
        if (team.isPresent()) {
            return team.get().getTeammates();
        }
        return Collections.emptyList();
    }

    public Optional<TeamMate> getProjectTeammate(Project project, String handle) {
        return getProjectTeammates(project).stream()
                .filter((t) -> t.getHandle().equalsIgnoreCase(handle))
                .findFirst();
    }

    public Collection<Team> getProjectTeams(Project project, TeamMate teamMate) {
       return userTeams.get(teamMate.getHandle()).stream()
               .filter((team -> team.getProjectId() == project.getId()))
               .collect(Collectors.toList());
    }

    public Collection<TeamMate> getTeammatesForMessage(Project project, Team team, TeamMate sender) {
        TeamMessageHandler handler = messageHandlers.get(team.getHandle());
        if (handler == null) {
            handler = new TeamMessageHandler.DefaultMessageHandler();
        }
        return handler.getRecipients(project, team, sender);
    }
}
