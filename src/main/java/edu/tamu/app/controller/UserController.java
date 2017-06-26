/* 
 * UserController.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiModel;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

/**
 * User Controller
 * 
 */
@Controller
@ApiMapping("/user")
public class UserController {

    @Autowired
    private AppUserRepo userRepo;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    /**
     * Websocket endpoint to request credentials.
     * 
     * @param shibObj
     *            Object
     * 
     * @return ApiResponse
     * 
     * @throws Exception
     * 
     */
    @ApiMapping("/credentials")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse credentials(@ApiCredentials Credentials credentials) throws Exception {
        return new ApiResponse(SUCCESS, credentials);
    }

    /**
     * Returns all users.
     * 
     * @return
     * @throws Exception
     */
    @ApiMapping("/all")
    @Auth(role = "ROLE_MANAGER")
    public ApiResponse allUsers() throws Exception {
        return new ApiResponse(SUCCESS,  userRepo.findAll());
    }
    
    /**
     * Returns all users.
     * 
     * @return
     * @throws Exception
     */
    @ApiMapping("/update")
    @Auth(role = "ROLE_MANAGER")
    public ApiResponse updateUser(@ApiModel AppUser user) throws Exception {        
        // get the persisted user for its encoded password        
        AppUser persistedUser = userRepo.findOne(user.getId());
        if(persistedUser != null) {
            user.setPassword(persistedUser.getPassword());
        }
        
        user = userRepo.save(user);
        
        simpMessagingTemplate.convertAndSend("/channel/user", new ApiResponse(SUCCESS, userRepo.findAll()));
        
        return new ApiResponse(SUCCESS, user);
    }

}
