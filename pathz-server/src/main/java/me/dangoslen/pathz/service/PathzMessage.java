package me.dangoslen.pathz.service;

import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class PathzMessage {

    private final Optional<Team> team;
    private final String message;
    private final TeamMate sender;
    private final Collection<TeamMate> recipients;

    public PathzMessage(Team team, TeamMate sender, String message, Collection<TeamMate> recipients) {
        this(Optional.ofNullable(team), sender, message, recipients);
    }

    public PathzMessage(Optional<Team> team, TeamMate sender, String message, Collection<TeamMate> recipients) {
        this.team = team;
        this.sender = sender;
        this.message = message;
        this.recipients = new HashSet<>();
        this.recipients.addAll(recipients);
    }

    public Optional<Team> getTeam() {
        return team;
    }

    public TeamMate getSender() {
        return sender;
    }

    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder(message);
        if (sender != null) {
            stringBuilder.append(" -").append(sender.getHandle());
            if (team.isPresent()) {
                stringBuilder.append(":").append(team.get().getHandle());
            }
        }
        return stringBuilder.toString();
    }

    public Collection<TeamMate> getRecipients() {
        return recipients;
    }
}
