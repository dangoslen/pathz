package me.dangoslen.pathz.api;

import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
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

    @PostMapping("/messaging")
    public ResponseEntity<Void> receiveMessage(@RequestBody Message message) {
        LOGGER.info("Received Message: {}", message);
        return ResponseEntity.ok(null);
    }
}
