package edu.tamu.app.auth.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.enums.Role;
import edu.tamu.app.model.User;
import edu.tamu.weaver.auth.model.Credentials;

@ExtendWith(SpringExtension.class)
public class AppUserDetailsTest {

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    static {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }
    
    private User testUser = new User(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));

    @Test
    public void testConstructor() {
        AppUserDetails appUser = new AppUserDetails(testUser);
        assertEquals(testUser.getId(), appUser.getId(), "The parent constructor was not called correctly");
    }
}
