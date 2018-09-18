package me.dangoslen.pathz.bandwidth.client.apis.messages;

import feign.Response;

import java.util.Collection;
import java.util.Optional;

public class HttpBandwidthMessagingService implements BandwidthMessagingService {

    private final BandwidthMessagingApiSpec api;

    public HttpBandwidthMessagingService(BandwidthMessagingApiSpec api) {
        this.api = api;
    }

    @Override
    public String sendMessageAndGetId(String userId, String to, String from, String text) {
        Message message = new Message(to, from, text);
        Response response = api.sendMessage(userId, message);
        return extractLocationIdAsString(response);
    }

    @Override
    public Collection<Message> getAllMessages(String userId) {
        return api.getAllMessages(userId);
    }

    private String extractLocationIdAsString(Response response) {
        Optional<String> optionalLocation = response.headers().get("location").stream()
                .findFirst();

        if(!optionalLocation.isPresent()) {
            return "";
        }

        String location = optionalLocation.get();
        return location.substring(location.lastIndexOf("/") + 1);
    }
}
