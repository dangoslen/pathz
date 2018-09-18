package me.dangoslen.pathz.bandwidth.client.apis.messages;

import java.util.Collection;

public interface BandwidthMessagingService {

    String sendMessageAndGetId(String userId, String to, String from, String text);

    Collection<Message> getAllMessages(String userId);
}
