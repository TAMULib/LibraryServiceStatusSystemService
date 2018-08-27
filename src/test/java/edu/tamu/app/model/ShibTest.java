package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.StatusApplication;
import edu.tamu.weaver.auth.model.Credentials;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ShibTest {

    private Map<String, Object> aggieJackToken;

    private long timestamp = new Date().getTime() + (5 * 60 * 1000);

    @Before
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

        assertEquals("Last name did not match.", "Daniels", shib.getLastName());
        assertEquals("First name did not match.", "Jack", shib.getFirstName());
        assertEquals("Netid did not match.", "aggiejack", shib.getNetid());
        assertEquals("UIN did not match.", "123456789", shib.getUin());
        assertEquals("Expiration did not match.", String.valueOf(timestamp), shib.getExp());
        assertEquals("Email did not match.", "aggiejack@tamu.edu", shib.getEmail());
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
