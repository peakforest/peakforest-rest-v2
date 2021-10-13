package fr.metabohub.peakforest.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Class used to map Users entites in the database
 * 
 * @author Nils Paulhe
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

	/**
	 * Serializable => default ID
	 */
	private static final long serialVersionUID = 1L;

	public static final int NORMAL = 0;
	public static final int CURATOR = 1;
	public static final int ADMIN = 2;

	public static final int SEARCH_ALL = 0;
	public static final int SEARCH_NOT_ACTIVATED = 1;
	public static final int SEARCH_ONLY_ACTIVATED = 2;

	public static final char PREF_LCMS = 'l';
	public static final char PREF_GCMS = 'g';
	public static final char PREF_LCMSMS = 'm';
	public static final char PREF_NMR = 'n';
	public static final char PREF_NMR2D = '2';

	@Id
	@GeneratedValue
	private @Setter long id;

	@Version
	private @Setter Long version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private @Setter Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private @Setter Date updated;

	@Column(nullable = false, unique = true)
	private @Setter String login;

	@Column(nullable = false, unique = true)
	private @Setter String email;

	@Column(nullable = false)
	private @Setter String password;

	@Column(nullable = false)
	private @Setter boolean admin = false;

	@Column(nullable = false)
	private @Setter boolean confirmed = false;

	@Column(nullable = false)
	private @Setter boolean curator = false;

	@Column(name = "main_technology", columnDefinition = " char(1) default '" + PREF_LCMS + "'") //
	private @Getter @Setter char mainTechnology = PREF_LCMS;

	@Column(nullable = true, unique = true)
	private @Setter String token = null;

	/**
	 * Get the User's ID
	 * 
	 * @return the id of the user
	 */
	@JsonProperty("id")
	public long getId() {
		return id;
	}

	/**
	 * Get the User's VERSION number
	 * 
	 * @return the version of the user
	 */
	@JsonProperty("version")
	public Long getVersion() {
		return version;
	}

	/**
	 * Get the User's LOGIN
	 * 
	 * @return the login of the user
	 */
	@JsonProperty("login")
	public String getLogin() {
		return login;
	}

	/**
	 * Get the User's EMAIL
	 * 
	 * @return the email of the user
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 * Get the User's PASSWORD
	 * 
	 * @return the password of the user
	 */
	@JsonIgnore
	public String getPassword() {
		return null;
	}

	/**
	 * Test if the user is ADMIN
	 * 
	 * @return true if the user has this kind of authorizations, false otherwise
	 */
	@JsonProperty("isAdmin")
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * Test if the user is CONFIRMED
	 * 
	 * @return true if the user is confirmed, false otherwise
	 */
	@JsonProperty("isConfirmed")
	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * Get the user's CREATION DATE
	 * 
	 * @return the created date
	 */
	@JsonProperty("created")
	public Date getCreated() {
		return created;
	}

	/**
	 * Get the user's LAST UPDATE DATE
	 * 
	 * @return the last update date
	 */
	@JsonProperty("updated")
	public Date getUpdated() {
		return updated;
	}

	/**
	 * Test if the user is CURATOR
	 * 
	 * @return true if the user has this kind of authorizations, false otherwise
	 */
	@JsonProperty("isCurator")
	public boolean isCurator() {
		return curator;
	}

	/**
	 * Get the user's TOKEN
	 * 
	 * @return the token of the user
	 */
	@JsonIgnore
	public String getToken() {
		return token;
	}

	/**
	 * Prune user (for easy return in json)
	 * 
	 * @return the pruned entity
	 */
	public User prune() {
		final User u = new User();
		u.setLogin(this.login);
		u.setEmail(this.email);
		u.setAdmin(Boolean.FALSE);
		u.setPassword(null);
		u.setToken(null);
		return u;
	}

}
