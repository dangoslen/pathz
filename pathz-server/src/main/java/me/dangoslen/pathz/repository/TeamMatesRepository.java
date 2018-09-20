package me.dangoslen.pathz.repository;

import me.dangoslen.pathz.models.TeamMate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TeamMatesRepository {

    private final ConcurrentHashMap<String, TeamMate> teamMates;

    TeamMatesRepository() {
        this.teamMates = new ConcurrentHashMap<>();
    }

    public Collection<TeamMate> getTeamMates() {
        return teamMates.values();
    }

    public void saveTeamMate(TeamMate teamMate) {
        teamMates.merge(teamMate.getHandle(), teamMate, (handle, t) -> teamMate);
    }

    public Optional<TeamMate> getTeamMate(String handle) {
        return Optional.ofNullable(teamMates.get(handle));
    }

}
