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

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.repo.custom.AppUserRepoCustom;

/**
 * Application User repository.
 * 
 */
@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long>, AppUserRepoCustom {

    /**
     * Retrieve user by UIN.
     * 
     * @param uin
     *            Long
     * 
     * @return AppUser
     */
    public AppUser findByUin(String uin);

    /**
     * Retrieve user by email.
     * 
     * @param email
     *            Long
     * 
     * @return AppUser
     * 
     */
    public AppUser findByEmail(String email);

}
