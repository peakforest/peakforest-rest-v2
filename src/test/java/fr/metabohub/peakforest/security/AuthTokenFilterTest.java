package fr.metabohub.peakforest.security;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.metabohub.peakforest.security.model.User;
import fr.metabohub.peakforest.utils.PeakForestApiHibernateUtils;
import fr.metabohub.peakforest.utils.PeakForestUtils;

public class AuthTokenFilterTest {

	static String token_confirmed_1;
	static String login_confirmed_1;
	static String token_confirmed_2;
	static String login_confirmed_2;
	static String token_not_confirmed;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// init config. for test
		PeakForestUtils.setBundleConf(ResourceBundle.getBundle("confTest"));
		// init token
		token_confirmed_1 = "token_user_confirmed_1_" + System.currentTimeMillis();
		login_confirmed_1 = "login_user_confirmed_1_" + System.currentTimeMillis();
		token_confirmed_2 = "token_user_confirmed_2_" + System.currentTimeMillis();
		login_confirmed_2 = "login_user_confirmed_2_" + System.currentTimeMillis();
		token_not_confirmed = "token_user_not_confirmed_" + System.currentTimeMillis();
		// add user confirmed
		final User uC1 = new User();
		uC1.setEmail("userC1." + System.currentTimeMillis() + "@inrae.fr");
		uC1.setLogin(login_confirmed_1);
		uC1.setPassword("");
		uC1.setAdmin(Boolean.TRUE);
		uC1.setConfirmed(Boolean.TRUE);
		uC1.setCurator(Boolean.TRUE);
		uC1.setToken(token_confirmed_1);
		create(uC1);
		final User uC2 = new User();
		uC2.setEmail("userC2." + System.currentTimeMillis() + "@inrae.fr");
		uC2.setLogin(login_confirmed_2);
		uC2.setPassword("");
		uC2.setConfirmed(Boolean.TRUE);
		uC2.setToken(token_confirmed_2);
		create(uC2);
		// add usernot confirmed
		final User uNC = new User();
		uNC.setEmail("userNC." + System.currentTimeMillis() + "@inrae.fr");
		uNC.setLogin("userNC." + System.currentTimeMillis() + "@inrae.fr");
		uNC.setPassword("");
		uNC.setConfirmed(Boolean.FALSE);
		uNC.setToken(token_not_confirmed);
		create(uNC);
	}

	private MockHttpServletRequest request;
	private HttpServletResponse response;
	private MockFilterChain filterChain;

	@Before
	@SuppressWarnings("serial")
	public void setup() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(null);
		this.request = new MockHttpServletRequest();
		this.request.setScheme("http");
		this.request.setServerName("localhost");
		this.request.setServerPort(80);
		this.filterChain = new MockFilterChain(new HttpServlet() {
		});
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void testDoFilter_noToken() throws ServletException, IOException {
		// test KO - no token
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNull(getAuthentication());
	}

	@Test
	public void testDoFilter_invalidToken() throws ServletException, IOException {
		// test KO - invalid token
		request.addParameter("token", "xxx");
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNull(getAuthentication());
	}

	@Test
	public void testDoFilter_emptyToken() throws ServletException, IOException {
		// test KO - empty token
		request.addParameter("token", "");
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNull(getAuthentication());
	}

	@Test
	public void testDoFilter_userNotConfirmed() throws ServletException, IOException {
		// test KO - user not confirmed
		request.addParameter("token", token_not_confirmed);
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNull(getAuthentication());
	}

	@Test
	public void testDoFilter_tokenGet() throws ServletException, IOException {
		// test OK - token GET
		request.addParameter("token", token_confirmed_1);
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNotNull(getAuthentication());
		Assert.assertTrue(getAuthentication().getPrincipal() instanceof User);
		Assert.assertEquals(login_confirmed_1, ((User) getAuthentication().getPrincipal()).getLogin());
	}

	@Test
	public void testDoFilter_tokenHeaderAuthorization() throws ServletException, IOException {
		// test OK - token HEADER
		request.addHeader("Authorization", "X-API-KEY " + token_confirmed_2);
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNotNull(getAuthentication());
		Assert.assertTrue(getAuthentication().getPrincipal() instanceof User);
		Assert.assertEquals(login_confirmed_2, ((User) getAuthentication().getPrincipal()).getLogin());
	}

	@Test
	public void testDoFilter_tokenHeaderKey() throws ServletException, IOException {
		// test OK - token HEADER
		request.addHeader("X-API-KEY", token_confirmed_2);
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNotNull(getAuthentication());
		Assert.assertTrue(getAuthentication().getPrincipal() instanceof User);
		Assert.assertEquals(login_confirmed_2, ((User) getAuthentication().getPrincipal()).getLogin());
	}

	@Test
	public void testDoFilter_tokenHeaderNotValid() throws ServletException, IOException {
		// test KO - token HEADER not valid (missing "X-API-KEY")
		request.addHeader("Authorization", token_confirmed_2);
		(new AuthTokenFilter()).doFilterInternal(request, response, filterChain);
		Assert.assertNull(getAuthentication());
	}

	///////////////////////////////////////////////////////////////////////////

	private UsernamePasswordAuthenticationToken getAuthentication() {
		return (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	}

	private static void create(final User user) {
		Transaction transaction = null;
		final Session session = PeakForestApiHibernateUtils.getSessionFactory().openSession();
		try {
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
		} catch (final HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

}
