package me.dangoslen.pathz.repository;

import me.dangoslen.pathz.models.TeamMate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TeamMatesRepository {

    private final ConcurrentHashMap<String, TeamMate> teamMates;
    private final ConcurrentHashMap<String, TeamMate> teamMatesByNumber;

    TeamMatesRepository() {
        this.teamMates = new ConcurrentHashMap<>();
        this.teamMatesByNumber = new ConcurrentHashMap<>();
    }

    public Collection<TeamMate> getTeamMates() {
        return teamMates.values();
    }

    public void saveTeamMate(TeamMate teamMate) {
        teamMates.merge(teamMate.getHandle(), teamMate, (handle, t) -> teamMate);
        teamMatesByNumber.merge(teamMate.getPhoneNumber(), teamMate, (handle, t) -> teamMate);
    }

    public Optional<TeamMate> getTeamMate(String handle) {
        return Optional.ofNullable(teamMates.get(handle));
    }

    public Optional<TeamMate> getTeamMateByNumber(String number) {
        return Optional.ofNullable(teamMatesByNumber.get(number));
    }

}
