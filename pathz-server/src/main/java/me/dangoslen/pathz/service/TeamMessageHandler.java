package me.dangoslen.pathz.service;

import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;

import java.util.Collection;
import java.util.Collections;

public interface TeamMessageHandler {

    Collection<TeamMate> getRecipients(Project project, Team team, TeamMate sender);

    class DefaultMessageHandler implements TeamMessageHandler {

        @Override
        public Collection<TeamMate> getRecipients(Project project, Team team, TeamMate sender) {
            if (project.getProjectManager().getHandle().equalsIgnoreCase(sender.getHandle())
                || team.getTeammates().contains(sender)) {
                return team.getTeammates();
            }
            return Collections.emptyList();
        }
    }

    class DefaultProjectTeamMessageHandler implements TeamMessageHandler {

        @Override
        public Collection<TeamMate> getRecipients(Project project, Team team, TeamMate sender) {
            if (project.getProjectManager().getHandle().equalsIgnoreCase(sender.getHandle())) {
                return team.getTeammates();
            }
            return Collections.emptyList();
        }
    }
}
