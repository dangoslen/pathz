package me.dangoslen.pathz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {
    private String name;
    private String handle;
    private int projectId;

    @JsonIgnore
    private Collection<TeamMate> teammates;

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
