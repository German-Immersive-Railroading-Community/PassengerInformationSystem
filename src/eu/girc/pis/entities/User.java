package eu.girc.pis.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import eu.girc.pis.utils.SecurityConfig;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "email", "name", "password", "forcePasswordChange", "roles"})
public class User implements PisEntity, Comparable<User> {
	
	private final String id;
	private String email;
	private String name;
	private String password;
	private boolean forcePasswordChange;
	private String roles;
	
	public User(
			@JsonProperty("id") String id,
			@JsonProperty("email") String email,
			@JsonProperty("name") String name, 
			@JsonProperty("password") String password,
			@JsonProperty("forcePasswordChange") boolean forcePasswordChange,
			@JsonProperty("roles") String roles) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.forcePasswordChange = forcePasswordChange;
		this.roles = roles;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setEmail(String mail) {
		this.email = mail;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
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
	
	public void setForcePasswordChange(boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}
	
	public boolean isForcePasswordChange() {
		return forcePasswordChange;
	}
	
	public String getRoles() {
		return roles;
	}
	
	public String[] getRolesAsStringArray() {
		if (roles == null || roles.isEmpty()) return new String[0];
		else return roles.trim().split(", ");
	}

	@Override
	public int compareTo(User user) {
		return this.id.compareTo(user.id);
	}
		
}
