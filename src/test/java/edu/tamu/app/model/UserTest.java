package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.GrantedAuthority;
import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.Role;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    private UserRepo userRepo;

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    static {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
    }

    @Test
    public void testMethod() {

        // Test create user
        User testUser1 = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        Optional<User> assertUser = userRepo.findByUsername("123456789");

        assertEquals(testUser1.getUsername(), assertUser.get().getUsername(), "Test User1 was not added.");

        // Test disallow duplicate UINs
        userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        List<User> allUsers = (List<User>) userRepo.findAll();
        assertEquals(1, allUsers.size(), "Duplicate UIN found.");

        // Test delete user
        userRepo.delete(testUser1);
        allUsers = (List<User>) userRepo.findAll();
        assertEquals(0, allUsers.size(), "Test User1 was not removed.");

    }

    @Test
    public void testGetAuthorities() {
        User testUser1 = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        Collection<? extends GrantedAuthority> authorities = testUser1.getAuthorities();
        assertNotNull(authorities);
    }
    
    @Test
    public void testStaticUtilityMethods() {
        User testUser1 = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        assertEquals(false, testUser1.isAccountNonExpired(), "Value was not false");
        assertEquals(false, testUser1.isAccountNonLocked(), "Value was not false");
        assertEquals(false, testUser1.isCredentialsNonExpired(), "Value was not false");
        assertEquals(true, testUser1.isEnabled(), "Value was not true");
        assertEquals(null, testUser1.getPassword(), "Value was not null");
    }

    @AfterEach
    public void cleanUp() {
        userRepo.deleteAll();
    }

}
