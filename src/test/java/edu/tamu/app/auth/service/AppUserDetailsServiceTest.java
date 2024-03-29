package edu.tamu.app.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.enums.Role;
import edu.tamu.app.model.User;
import edu.tamu.weaver.auth.model.Credentials;

@ExtendWith(SpringExtension.class)
public class AppUserDetailsServiceTest {
    
    private static final Credentials TEST_CREDENTIALS_1 = new Credentials();
    static {
        TEST_CREDENTIALS_1.setUin("123456789");
        TEST_CREDENTIALS_1.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_1.setFirstName("Aggie");
        TEST_CREDENTIALS_1.setLastName("Jack");
        TEST_CREDENTIALS_1.setRole("ROLE_USER");
    }
    
    private User testUser1 = new User(TEST_CREDENTIALS_1.getUin(), TEST_CREDENTIALS_1.getEmail(), TEST_CREDENTIALS_1.getFirstName(), TEST_CREDENTIALS_1.getLastName(), Role.valueOf(TEST_CREDENTIALS_1.getRole()));
    
    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    public void testBuildUserDetails() {
        UserDetails details = appUserDetailsService.buildUserDetails(testUser1);
        assertEquals(TEST_CREDENTIALS_1.getUin(), details.getUsername(), "User details not built correctly");
    }

}
