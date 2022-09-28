package eu.girc.pis.dtos;

import java.beans.ConstructorProperties;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import eu.girc.pis.main.Pis;
import eu.girc.pis.model.User;
import eu.girc.pis.utils.SecurityConfig;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "email"})
public class AccountDto extends User {
	
	@JsonCreator
	@ConstructorProperties({"id", "name", "email"})
	public AccountDto(String id, String name, String email) {
		super(id, name, email, getUser(id).map(User::getPassword).orElse(SecurityConfig.getPasswordEncoder().encode("1234")), getUser(id).map(User::isPasswordChangeRequired).orElse(false), getUser(id).map(User::getRoles).orElse(new String[0]));
	}
	
	private static Optional<User> getUser(String id) {
		return Pis.getUserService().get(id);
	}

}
