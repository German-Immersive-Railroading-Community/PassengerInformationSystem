package eu.girc.pis.dtos;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import eu.girc.pis.component.User;
import eu.girc.pis.main.Pis;
import eu.girc.pis.utils.SecurityConfig;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "email", "password", "forcePasswordChange", "roles"})
public class UserDto extends User {
	
	@JsonCreator
	@ConstructorProperties({"id", "name", "email", "passwordChangeRequired", "roles"})
	public UserDto(String id, String name, String email, boolean passwordChangeRequired, String roles) {
		super(id, name, email, (Pis.getUserService().get(id).map(User::getPassword).orElse(SecurityConfig.getPasswordEncoder().encode("1234"))), passwordChangeRequired, roles.replace(" ", "").split(","));
	}
	
	public static UserDto empty() {
		return new UserDto("", "", "", false, "");
	}
	
}
