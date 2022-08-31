package eu.girc.pis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import eu.girc.pis.entities.PisEntity;
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.entities.User;
import eu.girc.pis.main.Pis;
import eu.girc.pis.utils.SecurityConfig;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public static String homePage() {
		return "home.html";
	}
	
	@GetMapping("/search")
	public static String getStationsPage(@RequestParam(name = "query", required = false) String query, Model model) {
		if (query == null) return "search.html";
		List<PisEntity> results = new ArrayList<>();
		results.addAll(Pis.getStationService()
			.stream()
			.filter(station -> station.getName().contains(query) || query.contains(station.getName()) || station.getId().equals(query))
			.collect(Collectors.toList()));
		results.addAll(Pis.getLineService()
			.stream()
			.filter(line -> line.getName().contains(query) || query.contains(line.getName()) || line.getId().equals(query))
			.collect(Collectors.toList()));
		model.addAttribute("results", results);
		return "search.html";
	}

	@GetMapping("/stations")
	public static String getStationsPage(Model model) {
		model.addAttribute("stations", Pis.getStationService().getAsList());
		return "stations.html";
	}
	
	@GetMapping("/stations/{id}")
	public static String getStationPage(@PathVariable("id") String id, Model model) {
		final Optional<Station> station = Pis.getStationService().get(id);
		if (!station.isPresent()) return "error.html";
		model.addAttribute("station", station.get());
		return "station.html";
	}
	
	@GetMapping("/lines")
	public static String getLinesPage(Model model) {
		model.addAttribute("lines", Pis.getLineService().getAsList());
		return "lines.html";
	}
	
	@GetMapping("/lines/{id}")
	public static String getLinePage(@PathVariable("id") String id, Model model) {
		final Optional<Line> line = Pis.getLineService().get(id);
		if (!line.isPresent()) return "error.html";
		model.addAttribute("line", line.get());
		return "line.html";
	}
	
	@GetMapping("/login")
	public static String getLoginPage(@RequestParam(name = "failed", required = false) boolean failed) {
		return "login.html";
	}
	
	@GetMapping("/logout")
	public static String getLogoutPage() {
		return "redirect:home.html";
	}
	
	@GetMapping("/password")
	public static String getPasswordPage() {
		return "password.html";
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
		return "reset.html";
	}
	
	@GetMapping("/forgot")
	public static String getForgotPage() {
		return "forgot.html";
	}
	
}
