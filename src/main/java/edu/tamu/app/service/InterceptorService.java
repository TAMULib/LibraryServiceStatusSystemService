package edu.tamu.app.service;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

@Service
public class InterceptorService {

    private final Logger logger = LoggerFactory.getLogger(InterceptorService.class);

    @Autowired
    private AppUserRepo userRepo;

    @Value("${app.authority.admins}")
    private String[] admins;

    @Lazy
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Credentials getAnonymousCredentials() {
        Credentials anonymousCredentials = new Credentials();
        anonymousCredentials.setAffiliation("NA");
        anonymousCredentials.setLastName("Anonymous");
        anonymousCredentials.setFirstName("Role");
        anonymousCredentials.setNetid("anonymous-" + Math.round(Math.random() * 100000));
        anonymousCredentials.setUin("000000000");
        anonymousCredentials.setExp("1436982214754");
        anonymousCredentials.setEmail("helpdesk@library.tamu.edu");
        anonymousCredentials.setRole("ROLE_ANONYMOUS");
        return anonymousCredentials;
    }

    public AppUser confirmCreateUser(Credentials credentials) {

        AppUser user;
        String adminTarget;

        if (credentials.getUin().equals("null")) {
            user = userRepo.findByEmail(credentials.getEmail());
            adminTarget = credentials.getEmail();

            if (user == null) {
                // do not create user
                // return null for the core interceptor to return error to ui
                return null;
            }
        } else {
            user = userRepo.findByUin(credentials.getUin());
            adminTarget = credentials.getUin();
        }

        if (user == null) {

            if (credentials.getRole() == null) {
                credentials.setRole("ROLE_USER");
            }

            for (String uin : admins) {
                if (uin.equals(adminTarget)) {
                    credentials.setRole("ROLE_ADMIN");
                }
            }

            user = userRepo.create(credentials.getEmail(), credentials.getFirstName(), credentials.getLastName(), credentials.getRole());

            if (!credentials.getUin().equals("null")) {
                user.setUin(credentials.getUin());
                user = userRepo.save(user);
            }

            logger.info("Created new user: " + credentials.getFirstName() + " " + credentials.getLastName() + ")");

            Map<String, Object> userMap = new HashMap<String, Object>();

            userMap.put("list", userRepo.findAll());

            simpMessagingTemplate.convertAndSend("/channel/users", new ApiResponse(SUCCESS, userMap));
        }

        credentials.setRole(user.getRole().toString());

        return user;
    }

}
