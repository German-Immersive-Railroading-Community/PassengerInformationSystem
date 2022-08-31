package eu.girc.pis.utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import eu.girc.pis.entities.User;
import eu.girc.pis.main.Pis;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
	implements AuthenticationSuccessHandler, LogoutSuccessHandler{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		for (User user : Pis.getUserService()) {
			auth.inMemoryAuthentication().withUser(user.getId()).password(user.getPassword()).roles(user.getRolesAsStringArray());
		}
		auth.inMemoryAuthentication()
			.withUser("guest1").password(getPasswordEncoder().encode("password1")).roles("user").and()
			.withUser("guest2").password(getPasswordEncoder().encode("password2")).roles("user").and()
			.withUser("admin").password(getPasswordEncoder().encode("admin")).roles("admin");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/studio/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/studio")
			.failureUrl("/login?failed=true")
			.successHandler(this)
			.and()
			.rememberMe().key("remember-me")
			.and()
			.logout()
			.logoutUrl("/logout")
			.deleteCookies("JSESSIONID")
			.logoutSuccessHandler(this);
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		if (session != null) session.setAttribute("user", authentication.getName());
		response.sendRedirect("/studio");
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null) session.removeAttribute("user");
		response.sendRedirect("/");
	}
	
	@Bean 
	public static PasswordEncoder getPasswordEncoder() { 
	    return new BCryptPasswordEncoder(); 
	}

}
