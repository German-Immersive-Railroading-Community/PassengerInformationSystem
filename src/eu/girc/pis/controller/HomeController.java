package eu.girc.pis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import eu.girc.pis.entities.Entity;
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.entities.User;
import eu.girc.pis.main.PIS;
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
		if (query != null) {
			List<Entity> results = new ArrayList<>();
			results.addAll(PIS.getStations()
					.stream()
					.filter(station -> station.getName().contains(query) || query.contains(station.getName()) || station.getId().equals(query))
					.collect(Collectors.toList()));
			results.addAll(PIS.getLines()
					.stream()
					.filter(line -> line.getName().contains(query) || query.contains(line.getName()) || line.getId().equals(query))
					.collect(Collectors.toList()));
			model.addAttribute("results", results);
		}
		return "search.html";
	}

	@GetMapping("/stations")
	public static String getStationsPage(Model model) {
		model.addAttribute("stations", PIS.getStations());
		return "stations.html";
	}
	
	@GetMapping("/stations/{id}")
	public static String getStationPage(@PathVariable("id") String id, Model model) {
		final Optional<Station> station = PIS.find(PIS.getStations(), id);
		if (station.isPresent()) {
			final Set<Line> lines = PIS.getLines()
					.stream()
					.filter(line -> line.getStations().stream().filter(lineStation -> lineStation.getId().equals(id)).findAny().isPresent())
					.collect(Collectors.toSet());
			model.addAttribute("station", station.get());
			model.addAttribute("lines", lines);
			return "station.html";
		} else {
			return "error.html";
		}
	}
	
	@GetMapping("/lines")
	public static String getLinesPage(Model model) {
		model.addAttribute("lines", PIS.getLines());
		return "lines.html";
	}
	
	@GetMapping("/lines/{id}")
	public static String getLinePage(@PathVariable("id") String id, Model model) {
		final Optional<Line> line = PIS.find(PIS.getLines(), id);
		if (line.isPresent()) {
			model.addAttribute("line", line.get());
			return "line.html";
		} else {
			return "error.html";
		}
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
		User user = PIS.find(PIS.getUsers(), id).orElse(null);
		if (user == null) {
			return "redirect:/password?error=User+does+not+exist!";
		} else {
			String oldPassword = request.getParameter("old-password");
			String newPassword = request.getParameter("new-password");
			String repeatNewPassword = request.getParameter("repeat-new-password");
			if (!SecurityConfig.getPasswordEncoder().matches(oldPassword, user.getPassword())) {
				return "redirect:/password?id=" + id + "&error=Wrong+original+password!";
			} else if (!newPassword.equals(repeatNewPassword)) {
				return "redirect:/password?id=" + id + "&error=The+new+passwords+are+not+the+same!";
			} else {
				user.setUnencryptedPassword(newPassword);
				PIS.saveUsers();
				return "redirect:/studio";
			}
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
