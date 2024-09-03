package org.sparta.newsfeed.profiles.exception;

public class CustomException extends RuntimeException {

    private final String clientMessage;

    public CustomException(ExceptionMessage exceptionMessage, Object... args) {
        super(exceptionMessage.getConsoleMessage(args));
        this.clientMessage = exceptionMessage.getClientMessage();
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
