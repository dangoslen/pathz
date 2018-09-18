package me.dangoslen.pathz.bandwidth.client.apis.messages;

import com.google.common.base.MoreObjects;
import org.joda.time.DateTime;

public class Message {
    private String messageId;
    private String to;
    private String from;
    private String text;
    private DateTime dateTime;
    private String direction;
    private String state;

    // For serialization / deserialization
    private Message() {}

    public Message(String to, String from, String text) {
        this.to = to;
        this.from = from;
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getDirection() {
        return direction;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("to", to)
                .add("from", from)
                .add("time", dateTime)
                .toString();
    }
}
