/* 
 * AppUserRepo.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.custom.UserRepoCustom;

/**
 * Application User repository.
 * 
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, UserRepoCustom {

    /**
     * Retrieve user by UIN.
     * 
     * @param uin
     *            Long
     * 
     * @return AppUser
     */
    public User findByUin(String uin);

    /**
     * Retrieve user by email.
     * 
     * @param email
     *            Long
     * 
     * @return AppUser
     * 
     */
    public User findByEmail(String email);

}
