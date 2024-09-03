package org.sparta.newsfeed.domain.profiles.exception;

public class CustomException extends RuntimeException {

    private final String clientMessage;
    private final String consoleMessage;

    public CustomException(ExceptionMessage exceptionMessage, Object... args) {
        super(exceptionMessage.getConsoleMessage(args));
        this.consoleMessage = exceptionMessage.getConsoleMessage(args);
        this.clientMessage = exceptionMessage.getClientMessage();
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }
}
