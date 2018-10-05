package me.dangoslen.pathz.service;

import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public interface TeamMessageHandler {

    PathzMessage constructTeamMessage(Project project, Team team, TeamMate sender, String message);

    class DefaultMessageHandler implements TeamMessageHandler {

        @Override
        public PathzMessage constructTeamMessage(Project project, Team team, TeamMate sender, String message) {
            if (project.getProjectManager().getHandle().equalsIgnoreCase(sender.getHandle())
                || team.getTeammates().contains(sender)) {
                return new PathzMessage(team, sender, message, team.getTeammates());
            }
            return new PathzMessage(team, null, "You are not allowed to send to a message to Team '" + team.getHandle() + "'", Arrays.asList(sender));
        }
    }

    class DefaultProjectTeamMessageHandler implements TeamMessageHandler {

        @Override
        public PathzMessage constructTeamMessage(Project project, Team team, TeamMate sender, String message) {
            if (project.getProjectManager().getHandle().equalsIgnoreCase(sender.getHandle())) {
                return new PathzMessage(team, sender, message, team.getTeammates());
            }
            return new PathzMessage(team, null, "You are not allowed to send to a message to Team '" + team.getHandle() + "'", Arrays.asList(sender));
        }
    }
}
