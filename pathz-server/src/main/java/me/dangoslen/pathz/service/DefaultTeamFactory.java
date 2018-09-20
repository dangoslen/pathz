package me.dangoslen.pathz.service;

import me.dangoslen.pathz.models.Team;

public class DefaultTeamFactory {

    public static final Team buildDefaultTeam() {
        Team team = new Team();
        team.setName("Everyone");
        team.setHandle("@everyone");
        return team;
    }
}
