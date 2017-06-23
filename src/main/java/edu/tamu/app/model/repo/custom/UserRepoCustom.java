/* 
 * AppUserRepoCustom.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.User;

/**
 * 
 */
public interface UserRepoCustom {

    /**
     * method to create user based on uin
     * 
     * @param uin
     *            Long
     */
    public User create(String uin);

    public User create(String email, String firstName, String lastName, String role);

}
