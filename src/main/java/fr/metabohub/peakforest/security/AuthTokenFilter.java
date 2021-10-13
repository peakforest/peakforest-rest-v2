package fr.metabohub.peakforest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.metabohub.peakforest.security.model.User;
import fr.metabohub.peakforest.utils.PeakForestApiHibernateUtils;

public class AuthTokenFilter extends OncePerRequestFilter {

	final static String HEADER_AUTH = "X-API-KEY ";

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(//
			final HttpServletRequest request, //
			final HttpServletResponse response, //
			final FilterChain filterChain)//
			throws ServletException, IOException {
		try {
			// get token
			final String token = getTokenFromRequest(request);
			// get user from token
			final User currentUser = getUserFromToken(token);
			if (currentUser != null) {
				// add authorization
				final List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
				grantedAuths.add(new SimpleGrantedAuthority("USER"));
				if (currentUser.isAdmin()) {
					grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
				}
				if (currentUser.isCurator()) {
					grantedAuths.add(new SimpleGrantedAuthority("CURATOR"));
				}
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(//
						currentUser, null, grantedAuths);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (final Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(final HttpServletRequest request) {
		// try authentication using HEADER "Authorization: HEADER_AUTH xxxxx"
		final String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_AUTH)) {
			return headerAuth.substring(HEADER_AUTH.length(), headerAuth.length());
		}
		// try authentication using HEADER "HEADER_AUTH: xxxxx"
		final String headerKey = request.getHeader(HEADER_AUTH.trim());
		if (StringUtils.hasText(headerKey)) {
			return headerKey.trim();
		}
		// try authentication using TOKEN "?&token=xxxxx"
		final String tokenAuth = request.getParameter("token");
		if (tokenAuth != null) {
			return tokenAuth;
		}
		return null;
	}

	private User getUserFromToken(final String token) {
		// quick check
		if (token == null || token.trim().length() == 0) {
			return null;
		}
		// database read
		User user = null;
		try (final Session session = PeakForestApiHibernateUtils.getSessionFactory().openSession()) {
			// QUERY DB
			final String queryStringD = "FROM " + User.class.getSimpleName() + " WHERE token=(:token) AND confirmed=1 ";
			final TypedQuery<User> queryD = session.createQuery(queryStringD, User.class);
			queryD.setParameter("token", token);
			user = queryD.getSingleResult();
		} catch (final NoResultException e) {
			logger.error("No confirmed user for provided token,  {}", e.getMessage());
		} catch (final HibernateException e) {
			e.printStackTrace();
		}
		return user;
	}

}
