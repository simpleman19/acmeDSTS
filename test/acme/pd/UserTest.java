package acme.pd;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import acme.data.HibernateAdapter;

public class UserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HibernateAdapter.startUp();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HibernateAdapter.shutDown();
	}

	@Test
	public void testIsAuthenticated() {
		User user =  new User();
		user.setUsername(UUID.randomUUID().toString());
		user.setPassword(UUID.randomUUID().toString());
		user.create();
		
		assertTrue(User.isAuthenticated(user.getUsername(), user.getPassword()));
	}
	
	@Test
	public void testIsNotAuthenticated() {
		User user =  new User();
		user.setUsername(UUID.randomUUID().toString());
		user.setPassword(UUID.randomUUID().toString());

		assertFalse(User.isAuthenticated(user.getUsername(), user.getPassword()));
	}

}
