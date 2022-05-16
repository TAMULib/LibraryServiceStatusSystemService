package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.enums.Role;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private static final Credentials TEST_CREDENTIALS_1 = new Credentials();
    static {
        TEST_CREDENTIALS_1.setUin("123456789");
        TEST_CREDENTIALS_1.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_1.setFirstName("Aggie");
        TEST_CREDENTIALS_1.setLastName("Jack");
        TEST_CREDENTIALS_1.setRole("ROLE_USER");
    }

    private static final Credentials TEST_CREDENTIALS_2 = new Credentials();
    static {
        TEST_CREDENTIALS_2.setUin("987654321");
        TEST_CREDENTIALS_2.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_2.setFirstName("Aggie");
        TEST_CREDENTIALS_2.setLastName("Jack");
        TEST_CREDENTIALS_2.setRole("ROLE_USER");
    }

    private User testUser1 = new User(TEST_CREDENTIALS_1.getUin(), TEST_CREDENTIALS_1.getEmail(), TEST_CREDENTIALS_1.getFirstName(), TEST_CREDENTIALS_1.getLastName(), Role.valueOf(TEST_CREDENTIALS_1.getRole()));
    private User testUser2 = new User(TEST_CREDENTIALS_2.getUin(), TEST_CREDENTIALS_2.getEmail(), TEST_CREDENTIALS_2.getFirstName(), TEST_CREDENTIALS_2.getLastName(), Role.valueOf(TEST_CREDENTIALS_2.getRole()));

    private List<User> mockUserList = new ArrayList<User>(Arrays.asList(new User[] { testUser1, testUser2 }));

    private static ApiResponse apiResponse;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        when(userRepo.findAll()).thenReturn(mockUserList);
        when(userRepo.save(any(User.class))).thenReturn(testUser1);
        doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(Object.class));
    }

    @Test
    public void testCredentials() {
        apiResponse = userController.credentials(TEST_CREDENTIALS_1);
        assertEquals(SUCCESS, apiResponse.getMeta().getStatus(), "Unable to get user credentials");
    }

    @Test
    public void testNullCredentials() {
        apiResponse = userController.credentials(null);
        assertEquals(ERROR, apiResponse.getMeta().getStatus(), "Unable to get user credentials");
    }

    @Test
    public void testGetUser() {
        apiResponse = userController.getUser(testUser1);
        assertEquals(SUCCESS, apiResponse.getMeta().getStatus(), "Unable to get user");
    }

    @Test
    public void testGetNullUser() {
        apiResponse = userController.getUser(null);
        assertEquals(ERROR, apiResponse.getMeta().getStatus(), "Unable to get user");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllUsers() throws Exception {
        apiResponse = userController.allUsers();
        assertEquals(SUCCESS, apiResponse.getMeta().getStatus(), "Request for users was unsuccessful");
        assertEquals(2, ((ArrayList<User>) apiResponse.getPayload().get("ArrayList<User>")).size(), "Number of users was not correct");
    }

    @Test
    public void testUpdateUser() throws Exception {
        apiResponse = userController.updateUser(testUser1);
        assertEquals(SUCCESS, apiResponse.getMeta().getStatus(), "User was not successfully updated");
    }
}
