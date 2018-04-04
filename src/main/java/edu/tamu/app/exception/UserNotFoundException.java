package edu.tamu.app.exception;

public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = -3132836851437563543L;

    public UserNotFoundException(String message) {
        super(message);
    }

}
