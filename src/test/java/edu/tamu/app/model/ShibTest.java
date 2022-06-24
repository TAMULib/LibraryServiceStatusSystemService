package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import edu.tamu.app.StatusApplication;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShibTest {

    private Map<String, Object> aggieJackToken;

    private long timestamp = new Date().getTime() + (5 * 60 * 1000);

    @BeforeEach
    public void setup() {
        aggieJackToken = new HashMap<String, Object>();
        aggieJackToken.put("lastName", "Daniels");
        aggieJackToken.put("firstName", "Jack");
        aggieJackToken.put("netid", "aggiejack");
        aggieJackToken.put("uin", "123456789");
        aggieJackToken.put("exp", String.valueOf(timestamp));
        aggieJackToken.put("email", "aggiejack@tamu.edu");
    }

    @Test
    public void testCreateShib() {
        Credentials shib = new Credentials(aggieJackToken);

        assertEquals("Daniels", shib.getLastName(), "Last name did not match.");
        assertEquals("Jack", shib.getFirstName(), "First name did not match.");
        assertEquals("aggiejack", shib.getNetid(), "Netid did not match.");
        assertEquals("123456789", shib.getUin(), "UIN did not match.");
        assertEquals(String.valueOf(timestamp), shib.getExp(), "Expiration did not match.");
        assertEquals("aggiejack@tamu.edu", shib.getEmail(), "Email did not match.");
    }

    private Map<String, String> createToken(String uin, String firstName, String lastName, String email, String netid, Long time) {

        Map<String, String> newToken = new HashMap<>();

        newToken.put("uin", uin);
        newToken.put("firstName", firstName);
        newToken.put("lastName", lastName);
        newToken.put("email", email);
        newToken.put("netid", netid);
        if (time != null) {
            newToken.put("exp", String.valueOf(time));
        } else {
            newToken.put("exp", String.valueOf(timestamp));
        }

        return newToken;
    }

    @SuppressWarnings("unused")
    private Map<String, String> createToken(String uin, String firstName, String lastName, String email, String netid) {
        return createToken(uin, firstName, lastName, email, netid, null);
    }
}
