package me.dangoslen.pathz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {
    private int id;
    private String name;
    private String phoneNumber;
    private String projectManagerHandle;

    @JsonIgnore
    private ConcurrentHashMap<String, Team> teams;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectManagerHandle() {
        return projectManagerHandle;
    }

    public void setProjectManagerHandle(String projectManager) {
        this.projectManagerHandle = projectManager;
    }

    public void addTeam(Team team) {
        teams.merge(team.getHandle(), team, (handle, t) -> team);
    }

    public Team getTeam(String handle) {
        return teams.get(handle);
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

}
