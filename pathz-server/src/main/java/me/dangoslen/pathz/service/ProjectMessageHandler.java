package me.dangoslen.pathz.service;

import javafx.util.Pair;
import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingService;
import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.repository.TeamMatesRepository;
import me.dangoslen.pathz.repository.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.dangoslen.pathz.config.Variables.DEFAULT_TEAM_HANDLE;

@Service
public class ProjectMessageHandler {

    private final TeamsRepository teamsRepository;
    private final BandwidthMessagingService messagingService;
    private final String userId;

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(@.*?)\\s(.*)");

    @Autowired
    ProjectMessageHandler(TeamsRepository teamsRepository, BandwidthMessagingService messagingService,
                          @Value("${bandwidth.api.userId}") String userId) {
        this.teamsRepository = teamsRepository;
        this.messagingService = messagingService;
        this.userId = userId;
    }

    public void parseMessageAndSendResultingMessages(Project project, Message message) {
        Pair<String, String> handleMessagePair = extractIntendedTeam(project, message);

        Collection<TeamMate> teamMates = new ArrayList<>();
        Optional<Team> team = teamsRepository.getProjectTeam(project, handleMessagePair.getKey());
        if (team.isPresent()) {
            teamMates = teamsRepository.getProjectTeammates(project, team.get().getHandle());
        } else {
            Optional<TeamMate> teamMate = teamsRepository.getProjectTeammate(project, handleMessagePair.getKey());
            if (teamMate.isPresent()) {
                teamMates.add(teamMate.get());
            }
        }

        if (CollectionUtils.isEmpty(teamMates)) {
            messagingService.sendMessageAndGetId(userId, message.getFrom(), project.getPhoneNumber(), "We couldn't find who you are trying to send this message to. Please try again!");
        }

        if (handleMessagePair.getKey().equalsIgnoreCase(DEFAULT_TEAM_HANDLE)
                && !project.getProjectManager().getPhoneNumber().equalsIgnoreCase(message.getFrom())) {
            messagingService.sendMessageAndGetId(userId, message.getFrom(), project.getPhoneNumber(), "You are not allowed to send message to @all");
        } else {
            for(TeamMate teamMate : teamMates) {
                messagingService.sendMessageAndGetId(userId, teamMate.getPhoneNumber(), project.getPhoneNumber(), handleMessagePair.getValue());
            }
        }
    }

    private Pair<String, String> extractIntendedTeam(Project project, Message message) {
        String desiredMessage = message.getText().trim();
        if (desiredMessage.startsWith("@")) {
            Matcher matcher = MESSAGE_PATTERN.matcher(desiredMessage);
            matcher.matches();
            return new Pair<>(matcher.group(1), matcher.group(2));
        }
        return new Pair(project.getTeam(DEFAULT_TEAM_HANDLE), desiredMessage);
    }
}
