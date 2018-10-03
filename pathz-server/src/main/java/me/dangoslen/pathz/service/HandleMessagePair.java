package me.dangoslen.pathz.service;

public class HandleMessagePair {
    private final String handle;
    private final String message;

    public HandleMessagePair(String handle, String message) {
        this.handle = handle;
        this.message = message;
    }

    public String getHandle() {
        return handle;
    }

    public String getMessage() {
        return message;
    }
}
