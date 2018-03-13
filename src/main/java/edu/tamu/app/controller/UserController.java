package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

/**
 * User Controller
 * 
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

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
    @RequestMapping("/credentials")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse credentials(@WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, credentials);
    }

    /**
     * Returns all users.
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/all")
    @PreAuthorize("hasRole('WEB_MANAGER')")
    public ApiResponse allUsers() throws Exception {
        return new ApiResponse(SUCCESS, userRepo.findAll());
    }

    /**
     * Returns all users.
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('WEB_MANAGER')")
    public ApiResponse updateUser(@RequestBody User user) throws Exception {
        user = userRepo.save(user);
        simpMessagingTemplate.convertAndSend("/channel/user", new ApiResponse(SUCCESS, userRepo.findAll()));
        return new ApiResponse(SUCCESS, user);
    }

}
