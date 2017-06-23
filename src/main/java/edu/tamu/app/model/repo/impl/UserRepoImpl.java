/* 
 * AppUserRepoImpl.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.custom.UserRepoCustom;

/**
 * 
 */
public class UserRepoImpl implements UserRepoCustom {

    @Autowired
    private UserRepo appUserRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(String uin) {
        User user = appUserRepo.findByUin(uin);
        if (user == null) {
            return appUserRepo.save(new User(uin));
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(String email, String firstName, String lastName, String role) {
        User user = appUserRepo.findByEmail(email);
        if (user == null) {
            return appUserRepo.save(new User(email, firstName, lastName, role));
        }
        return user;
    }

}
