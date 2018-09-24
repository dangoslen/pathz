package me.dangoslen.pathz.service;

import me.dangoslen.pathz.models.Team;

import static me.dangoslen.pathz.config.Variables.DEFAULT_TEAM_HANDLE;

public class DefaultTeamFactory {

    public static final Team buildDefaultTeam(int projectId) {
        Team team = new Team();
        team.setName("All");
        team.setHandle(DEFAULT_TEAM_HANDLE);
        team.setProjectId(projectId);
        return team;
    }
}
