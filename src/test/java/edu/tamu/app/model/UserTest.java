package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.model.repo.UserRepo;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class UserTest {
	
	@Autowired
	private UserRepo userRepo;
	
	@Before
	public void setUp() {
		userRepo.deleteAll();
	}
	
	@Test
	public void testMethod() {
		
		// Test create user
		User testUser1 = userRepo.create("123456789");		
		User assertUser = userRepo.findByUin("123456789");		
		assertEquals("Test User1 was not added.", testUser1.getUin(), assertUser.getUin());
	
		// Test disallow duplicate UINs
		userRepo.create("123456789");		
		List<User> allUsers = (List<User>) userRepo.findAll();		
		assertEquals("Duplicate UIN found.", 1, allUsers.size());
				
		// Test delete user
		userRepo.delete(testUser1);		
		allUsers = (List<User>) userRepo.findAll();		
		assertEquals("Test User1 was not removed.", 0, allUsers.size());
		
	}
	
	@After
	public void cleanUp() {
		userRepo.deleteAll();
	}
	
}