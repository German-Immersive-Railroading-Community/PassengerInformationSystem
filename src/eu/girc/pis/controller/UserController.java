package eu.girc.pis.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.girc.pis.dtos.UserDto;
import eu.girc.pis.main.Pis;
import eu.girc.pis.model.User;
import eu.girc.pis.utils.SecurityConfig;

@Controller
@RequestMapping("/")
public class UserController {

	@GetMapping("/login")
	public static String getLoginPage(@RequestParam(name = "failed", required = false) boolean failed) {
		return "forms/login.html";
	}

	@GetMapping("/logout")
	public static String getLogoutPage() {
		return "redirect:home.html";
	}

	@GetMapping("/password")
	public static String getPasswordPage() {
		return "forms/password.html";
	}

	@PostMapping("/password")
	public static String postPasswordPage(HttpServletRequest request) {
		String id = request.getParameter("user");
		User user = Pis.getUserService().get(id).orElse(null);
		if (user == null) return "redirect:/password?error=User+does+not+exist!";
		String oldPassword = request.getParameter("old-password");
		String newPassword = request.getParameter("new-password");
		String repeatNewPassword = request.getParameter("repeat-new-password");
		if (!SecurityConfig.getPasswordEncoder().matches(oldPassword, user.getPassword())) {
			return "redirect:/password?id=" + id + "&error=Wrong+original+password!";
		} else if (!newPassword.equals(repeatNewPassword)) {
			return "redirect:/password?id=" + id + "&error=The+new+passwords+are+not+the+same!";
		} else {
			user.setUnencryptedPassword(newPassword);
			Pis.getUserService().save();
			return "redirect:/studio";
		}
	}

	@GetMapping("/reset")
	public static String getResetPage() {
		return "forms/reset.html";
	}

	@GetMapping("/forgot")
	public static String getForgotPage() {
		return "forms/forgot.html";
	}

	@GetMapping("/studio/users")
	public String getStudioListPage(Model model) {
		model.addAttribute("users", Pis.getUserService().getAsList());
		return "studio/users.html";
	}

	@GetMapping("/studio/users/edit")
	public String getEditPage(@RequestParam(name = "id", required = false) String id, Model model) {
		final User user = Pis.getUserService().get(id).orElse(User.empty());
		model.addAttribute("user", user);
		return "studio/edit/edit_user.html";
	}

	@PostMapping("/studio/users/edit")
	public String postEditPage(UserDto user) {
		Pis.getUserService().remove(user.getId());
		Pis.getUserService().add(user);
		return "redirect:/studio/users";
	}

	@GetMapping("/studio/users/delete")
	public String deleteComponent(@RequestParam(name = "id", required = true) String id, Model model) {
		Pis.getUserService().remove(id);
		return "redirect:/studio/users";
	}

}
