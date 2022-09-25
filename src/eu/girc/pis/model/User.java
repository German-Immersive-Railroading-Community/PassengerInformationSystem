package eu.girc.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import eu.girc.pis.utils.SecurityConfig;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "email", "password", "forcePasswordChange", "roles"})
public class User implements PisComponent, Comparable<User>{
	
	private final String id;
	private String name;
	private String email;
	private String password;
	private boolean passwordChangeRequired;
	private String roles[];
	
	@JsonCreator
	@ConstructorProperties({"id", "name", "email", "password", "passwordChangeRequired", "roles"})
	public User(String id, String name, String email, String password, boolean passwordChangeRequired, String roles[]) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.passwordChangeRequired = passwordChangeRequired;
		this.roles = roles;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setEmail(String mail) {
		this.email = mail;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUnencryptedPassword(String password) {
		this.password = SecurityConfig.getPasswordEncoder().encode(password);
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPasswordChangeRequired(boolean reqired) {
		this.passwordChangeRequired = reqired;
	}

	public boolean isPasswordChangeRequired() {
		return passwordChangeRequired;
	}
	
	public String[] getRoles() {
		return roles;
	}

	@Override
	public int compareTo(User user) {
		return this.id.compareTo(user.getId());
	}

	public static User empty() {
		return new User(null, null, null, "0000", false, new String[0]);
	}

}
