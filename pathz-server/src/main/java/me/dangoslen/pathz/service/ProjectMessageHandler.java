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

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.dangoslen.pathz.config.Variables.DEFAULT_TEAM_HANDLE;
import static me.dangoslen.pathz.config.Variables.TEAMS_RESERVED_HANDLE;

@Service
public class ProjectMessageHandler {

    private final TeamsRepository teamsRepository;
    private final TeamMatesRepository teamMatesRepository;
    private final BandwidthMessagingService messagingService;
    private final String userId;

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(@.*?)\\s(.*)");

    @Autowired
    ProjectMessageHandler(TeamsRepository teamsRepository, TeamMatesRepository teamMatesRepository,
                          BandwidthMessagingService messagingService, @Value("${bandwidth.api.userId}") String userId) {
        this.teamsRepository = teamsRepository;
        this.messagingService = messagingService;
        this.teamMatesRepository = teamMatesRepository;
        this.userId = userId;
    }

    public void parseMessageAndSendResultingMessages(Project project, Message message) {
        Optional<TeamMate> optionalSender = getSendingTeammate(project, message.getFrom());
        if (!optionalSender.isPresent()) {
            messagingService.sendMessageAndGetId(userId, message.getFrom(), project.getPhoneNumber(), "You are not a member of this project.");
        }

        TeamMate sender = optionalSender.get();
        PathzMessage pathzMessage = getMessage(project, sender, message);
        if (CollectionUtils.isEmpty(pathzMessage.getRecipients())) {
            messagingService.sendMessageAndGetId(userId, message.getFrom(), project.getPhoneNumber(), "Could not find the desired Team or TeamMate. Use '@teams' to find which teams you are a member of");
        } else {
            String sentMessage = pathzMessage.getMessage();
            for (TeamMate teamMate : pathzMessage.getRecipients()) {
                messagingService.sendMessageAndGetId(userId, teamMate.getPhoneNumber(), project.getPhoneNumber(), sentMessage);
            }
        }
    }

    private PathzMessage getMessage(Project project, TeamMate sender, Message message) {
        HandleMessagePair handleMessagePair = extractIntendedTeam(message);
        Optional<Team> intendedTeam = teamsRepository.getProjectTeam(project, handleMessagePair.getHandle());
        if (intendedTeam.isPresent()) {
            Collection<TeamMate> recipients = teamsRepository.getTeammatesForMessage(project, intendedTeam.get(), sender);
            return new PathzMessage(intendedTeam, sender, handleMessagePair.getMessage(), recipients);
        } else {
            Optional<TeamMate> intendedTeammate = getIntendedTeammate(project, handleMessagePair.getHandle());
            if (intendedTeammate.isPresent()) {
                return new PathzMessage(intendedTeam, sender, handleMessagePair.getMessage(), Arrays.asList(intendedTeammate.get()));
            } else {
                return new PathzMessage(intendedTeam, sender, handleMessagePair.getMessage(), Collections.emptyList());
            }
        }
    }

    private Optional<TeamMate> getIntendedTeammate(Project project, String handle) {
        if (project.getProjectManager().getHandle().equalsIgnoreCase(handle)) {
            return Optional.of(project.getProjectManager());
        }
        return teamsRepository.getProjectTeammate(project, handle);
    }

    private HandleMessagePair extractIntendedTeam(Message message) {
        String desiredMessage = message.getText().trim();
        if (desiredMessage.startsWith("@")) {
            Matcher matcher = MESSAGE_PATTERN.matcher(desiredMessage);
            matcher.matches();
            return new HandleMessagePair(matcher.group(1), matcher.group(2));
        }
        return new HandleMessagePair(DEFAULT_TEAM_HANDLE, desiredMessage);
    }

    private Optional<TeamMate> getSendingTeammate(Project project, String fromNumber) {
        Optional<TeamMate> optionalTeamMate = teamMatesRepository.getTeamMateByNumber(fromNumber);
        if (optionalTeamMate.isPresent()) {
            TeamMate sender = optionalTeamMate.get();
            if (project.getProjectManager().getHandle().equalsIgnoreCase(sender.getHandle())
                || !teamsRepository.getProjectTeammate(project, sender.getHandle()).isPresent()) {
                return optionalTeamMate;
            } else {
                return optionalTeamMate;
            }
        }
        return optionalTeamMate;
    }
}
