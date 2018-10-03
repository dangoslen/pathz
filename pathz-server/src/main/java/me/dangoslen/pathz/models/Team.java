package me.dangoslen.pathz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import me.dangoslen.pathz.service.TeamMessageHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static me.dangoslen.pathz.config.Variables.DEFAULT_TEAM_HANDLE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {
    private String name;
    private String handle;
    private int projectId;

    @JsonIgnore
    private Collection<TeamMate> teammates;

    public Team() {
        teammates = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Collection<TeamMate> getTeammates() {
        return teammates;
    }

    public void addTeamMate(TeamMate teamMate) {
        this.teammates.add(teamMate);
    }

}
