package edu.tamu.app.model.repo.custom;

import edu.tamu.app.enums.Role;
import edu.tamu.app.model.User;

public interface UserRepoCustom {

    /**
     * 
     * @param uin
     * @param email
     * @param firstName
     * @param lastName
     * @param role
     * @return
     */
    public User create(String uin, String email, String firstName, String lastName, Role role);

}
