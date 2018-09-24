package me.dangoslen.pathz.api;

import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
import me.dangoslen.pathz.models.Project;
import me.dangoslen.pathz.repository.ProjectRepository;
import me.dangoslen.pathz.service.ProjectMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/callbacks")
public class MessagingCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingCallbackController.class);

    private final ProjectRepository projectRepository;
    private final ProjectMessageHandler projectMessageHandler;

    MessagingCallbackController(ProjectRepository projectRepository, ProjectMessageHandler projectMessageHandler) {
        this.projectRepository = projectRepository;
        this.projectMessageHandler = projectMessageHandler;
    }

    @PostMapping("/messaging")
    public ResponseEntity<Void> receiveMessage(@RequestBody Message message) {
        String number = message.getTo();
        Project project = projectRepository.getProjectFromPhoneNumber(number);
        projectMessageHandler.parseMessageAndSendResultingMessages(project, message);
        return ResponseEntity.accepted().body(null);
    }
}
