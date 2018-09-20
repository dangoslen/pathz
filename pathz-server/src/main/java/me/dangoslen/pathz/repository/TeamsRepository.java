package me.dangoslen.pathz.repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TeamsRepository {

    private Multimap<Integer, Team> projectTeams;
    private Multimap<String, Team> userTeams;

    TeamsRepository() {
        this.projectTeams = ArrayListMultimap.create();
        this.userTeams = ArrayListMultimap.create();
    }

    public void saveProjectTeam(Project project, Team team) {
        projectTeams.put(project.getId(), team);
        for(TeamMate mate : team.getTeammates()) {
            userTeams.put(mate.getHandle(), team);
        }
    }

    public Collection<Team> getProjectTeams(Project project) {
        return projectTeams.get(project.getId());
    }

    public Team getProjectTeam(Project project, String handle) {
        return getProjectTeams(project).stream()
                .filter((team -> team.getHandle().equalsIgnoreCase(handle)))
                .findFirst()
                .get();
    }

    public Collection<Team> getProjectTeams(TeamMate teamMate) {
       return userTeams.get(teamMate.getHandle());
    }
}
