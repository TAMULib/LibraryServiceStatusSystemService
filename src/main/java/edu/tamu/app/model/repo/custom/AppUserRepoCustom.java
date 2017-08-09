package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.AppUser;

/**
 * 
 */
public interface AppUserRepoCustom {

    /**
     * method to create user based on uin
     * 
     * @param uin
     *            Long
     */
    public AppUser create(String uin);

    public AppUser create(String email, String firstName, String lastName, String role);

}
