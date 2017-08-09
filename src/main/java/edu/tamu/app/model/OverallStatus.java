package edu.tamu.app.model;

import edu.tamu.app.enums.OverallMessageType;

public class OverallStatus {

    private String message;

    private OverallMessageType type;

    public OverallStatus(OverallMessageType type, String message) {
        setType(type);
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OverallMessageType getType() {
        return type;
    }

    public void setType(OverallMessageType type) {
        this.type = type;
    }

}
