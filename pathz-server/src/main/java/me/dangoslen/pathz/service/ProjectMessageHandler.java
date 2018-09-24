package me.dangoslen.pathz.service;

import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingService;
import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.models.Team;
import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.repository.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProjectMessageHandler {

    private final TeamsRepository teamsRepository;
    private final BandwidthMessagingService messagingService;
    private final String userId;

    @Autowired
    ProjectMessageHandler(TeamsRepository teamsRepository, BandwidthMessagingService messagingService,
                          @Value("${bandwidth.api.userId}") String userId) {
        this.teamsRepository = teamsRepository;
        this.messagingService = messagingService;
        this.userId = userId;
    }

    public void parseMessageAndSendResultingMessages(Project project, Message message) {
        if (message.getFrom().equalsIgnoreCase(project.getProjectManager().getPhoneNumber())) {
            Collection<TeamMate> teamMates = teamsRepository.getProjectTeammates(project);
            for(TeamMate teamMate : teamMates) {
                messagingService.sendMessageAndGetId(userId, teamMate.getPhoneNumber(), project.getPhoneNumber(), message.getText());
            }
        }
    }
}
