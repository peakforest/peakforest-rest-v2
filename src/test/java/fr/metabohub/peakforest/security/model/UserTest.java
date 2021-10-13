package fr.metabohub.peakforest.security.model;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

	@Test
	public void testGetSetId() {
		final User u = new User();
		Assert.assertEquals(0L, u.getId(), 0);
		u.setId(42L);
		Assert.assertEquals(42L, u.getId(), 0);
	}

	@Test
	public void testGetSetVersion() {
		final User u = new User();
		Assert.assertNull(u.getVersion());
		u.setVersion(666L);
		Assert.assertEquals(666L, u.getVersion(), 0);
	}

	@Test
	public void testGetSetLogin() {
		final User u = new User();
		Assert.assertNull(u.getLogin());
		u.setLogin("set-login");
		Assert.assertEquals("set-login", u.getLogin());
	}

	@Test
	public void testGetSetEmail() {
		final User u = new User();
		Assert.assertNull(u.getEmail());
		u.setEmail("set-email");
		Assert.assertEquals("set-email", u.getEmail());
	}

	@Test
	public void testGetSetPassword() {
		final User u = new User();
		Assert.assertNull(u.getPassword());
		u.setPassword("set-password");
		Assert.assertNull("set-password", u.getPassword());
	}

	@Test
	public void testIsAdmin() {
		final User u = new User();
		Assert.assertFalse(u.isAdmin());
		u.setAdmin(Boolean.TRUE);
		Assert.assertTrue("set-admin", u.isAdmin());
	}

	@Test
	public void testIsConfirmed() {
		final User u = new User();
		Assert.assertFalse(u.isConfirmed());
		u.setConfirmed(Boolean.TRUE);
		Assert.assertTrue("set-Confirmed", u.isConfirmed());
	}

	@Test
	public void testGetSetCreated() {
		final User u = new User();
		Assert.assertNull(u.getCreated());
		u.setCreated(new Date());
		Assert.assertNotNull("set-Created", u.getCreated());
	}

	@Test
	public void testGetSetUpdated() {
		final User u = new User();
		Assert.assertNull(u.getUpdated());
		u.setUpdated(new Date());
		Assert.assertNotNull("set-Updated", u.getUpdated());
	}

	@Test
	public void testIsCurator() {
		final User u = new User();
		Assert.assertFalse(u.isCurator());
		u.setCurator(Boolean.TRUE);
		Assert.assertTrue("set-Curator", u.isCurator());
	}

	@Test
	public void testGetSetToken() {
		final User u = new User();
		Assert.assertNull(u.getToken());
		u.setToken("set-Token");
		Assert.assertEquals("set-Token", u.getToken());
	}

	@Test
	public void testPrune() {
		final User u1 = new User();
		u1.setLogin("login");
		u1.setEmail("email");
		u1.setAdmin(Boolean.TRUE);
		u1.setPassword("password");
		u1.setToken("token");
		Assert.assertTrue(u1.isAdmin());
		Assert.assertNull(u1.getPassword());
		Assert.assertNotNull(u1.getToken());
		final User u2 = u1.prune();
		Assert.assertEquals("login", u2.getLogin());
		Assert.assertEquals("email", u2.getEmail());
		Assert.assertFalse(u2.isAdmin());
		Assert.assertNull(u2.getPassword());
		Assert.assertNull(u2.getToken());
	}

	@Test
	public void testGetSetMainTechnology() {
		final User u = new User();
		Assert.assertEquals(User.PREF_LCMS, u.getMainTechnology());
		u.setMainTechnology(User.PREF_GCMS);
		Assert.assertEquals(User.PREF_GCMS, u.getMainTechnology());
	}

}
