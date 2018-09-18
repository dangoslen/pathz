package me.dangoslen.pathz.api;

import me.dangoslen.pathz.bandwidth.client.apis.messages.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController("/callbacks/messaging")
public class MessagingCallbackController {

    public ResponseEntity<Void> receiveMessag(Message message) {
        return ResponseEntity.ok(null);
    }
}
