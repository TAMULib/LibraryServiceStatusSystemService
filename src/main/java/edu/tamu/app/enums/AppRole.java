package edu.tamu.app.enums;

import edu.tamu.framework.model.IRole;

public enum AppRole implements IRole {

    ROLE_ANONYMOUS(0), ROLE_USER(1), ROLE_STAFF(2), ROLE_SERVICE_MANAGER(3), ROLE_WEB_MANAGER(4), ROLE_ADMIN(5);

    private int value;

    AppRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name();
    }

}