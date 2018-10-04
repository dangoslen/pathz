package me.dangoslen.pathz.service;

import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final BandwidthMessagingService service;
    private final String userId;

    @Autowired
    public MessagingService(BandwidthMessagingService service, @Value("${bandwidth.api.userId}") String userId) {
        this.service = service;
        this.userId = userId;
    }

    public void sendMessage(String to, String from, String message) {
        this.service.sendMessageAndGetId(userId, to, from, message);
    }
}
