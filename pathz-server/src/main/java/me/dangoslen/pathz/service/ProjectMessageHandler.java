package me.dangoslen.pathz.service;

import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.repository.TeamMatesRepository;
import me.dangoslen.pathz.repository.TeamsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    private final MessagingService messagingService;

    private static final Pattern JOIN_PATTERN = Pattern.compile("(^/[j|J]oin)\\s(@.*)");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(@.*?)\\s(.*)");
    @Autowired
    ProjectMessageHandler(TeamsRepository teamsRepository, TeamMatesRepository teamMatesRepository,
                          MessagingService messagingService) {
        this.teamsRepository = teamsRepository;
        this.messagingService = messagingService;
        this.teamMatesRepository = teamMatesRepository;
    }

    public void parseMessageAndSendResultingMessages(Project project, Message message) {
        Optional<TeamMate> optionalSender = getSendingTeammate(project, message.getFrom());
        if (!optionalSender.isPresent()) {
            Matcher matcher = JOIN_PATTERN.matcher(message.getText());
            if (matcher.matches()) {
                TeamMate joiner = new TeamMate(matcher.group(2), message.getFrom(), matcher.group(2));
                teamMatesRepository.saveTeamMate(joiner);
                Team team = teamsRepository.getProjectTeam(project, DEFAULT_TEAM_HANDLE).get();
                team.addTeamMate(joiner);
                teamsRepository.saveProjectTeam(project, team);
                messagingService.sendMessage(project.getPhoneNumber(), project.getPhoneNumber(),
                        String.format("Welcome to the Team! You have been added to Project '%s'.", project.getName()));
            } else {
                messagingService.sendMessage(message.getFrom(), project.getPhoneNumber(), "You are not a member of this project.");
            }
        }

        TeamMate sender = optionalSender.get();
        PathzMessage pathzMessage = getMessage(project, sender, message);
        if (CollectionUtils.isEmpty(pathzMessage.getRecipients())) {
            messagingService.sendMessage(message.getFrom(), project.getPhoneNumber(), "Could not find the desired Team or TeamMate. Use '@teams' to find which teams you are a member of");
        } else {
            String sentMessage = pathzMessage.getMessage();
            for (TeamMate teamMate : pathzMessage.getRecipients()) {
                messagingService.sendMessage(teamMate.getPhoneNumber(), project.getPhoneNumber(), sentMessage);
            }
        }
    }

    private PathzMessage getMessage(Project project, TeamMate sender, Message message) {
        HandleMessagePair handleMessagePair = extractIntendedTeam(message);
        if (handleMessagePair.getHandle().equalsIgnoreCase(TEAMS_RESERVED_HANDLE)) {
            String teamsMessage = getTeammateTeamMessage(project, sender);
            return new PathzMessage(Optional.empty(), null, teamsMessage, Arrays.asList(sender));
        }

        Optional<Team> intendedTeam = teamsRepository.getProjectTeam(project, handleMessagePair.getHandle());
        if (intendedTeam.isPresent() && StringUtils.isBlank(handleMessagePair.getMessage())) {
            Team team = intendedTeam.get();
            String teamsMessage = getTeammatesMessage(project, team);
            return new PathzMessage(Optional.empty(), null, teamsMessage, Arrays.asList(sender));
        }

        if (intendedTeam.isPresent()) {
            TeamMessageHandler handler =  teamsRepository.getTeamMessageHandler(intendedTeam.get());
            return handler.constructTeamMessage(project, intendedTeam.get(), sender, handleMessagePair.getMessage());
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
            if (matcher.matches()) {
                return new HandleMessagePair(matcher.group(1), matcher.group(2));
            } else {
                return new HandleMessagePair(desiredMessage, "");
            }
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

    private String getTeammateTeamMessage(Project project, TeamMate teammate) {
        StringBuilder builder = new StringBuilder("You are apart of the following teams:");
        Collection<Team> teams = teamsRepository.getProjectTeams(project, teammate);
        teams.stream().forEach((team) -> builder.append("\n").append(team.getHandle()));
        return builder.toString();
    }

    private String getTeammatesMessage(Project project, Team team) {
        StringBuilder builder = new StringBuilder("Your team has the following members (limited to the first 10 if more than 10):");
        Collection<TeamMate> teams = teamsRepository.getProjectTeammates(project, team.getHandle());
        teams.stream().skip(0).limit(10).forEach((teammate) -> builder.append("\n").append(teammate.getHandle()));
        return builder.toString();
    }
}
