package eu.girc.pis.utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import eu.girc.pis.main.Pis;
import eu.girc.pis.model.User;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
	implements AuthenticationSuccessHandler, LogoutSuccessHandler, AuthenticationProvider{
		
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
	
	@SuppressWarnings("serial")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        User user = Pis.getUserService().get(username).orElse(null);
        if (user != null && getPasswordEncoder().matches(password, user.getPassword())) {
        	return new UsernamePasswordAuthenticationToken(username, password, user.getRolesAsAuthorities());
        }
        throw new AuthenticationException("Your credentials aren't correct, please try again!") {};
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
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
