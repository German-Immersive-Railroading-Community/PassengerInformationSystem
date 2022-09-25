package eu.girc.pis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.girc.pis.main.Pis;
import eu.girc.pis.model.PisComponent;

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
		List<PisComponent> results = new ArrayList<>();
		results.addAll(Pis.getStationService()
				.stream()
				.filter(station -> station.getId().equals(query) || station.getName().contains(query) || query.contains(station.getName()))
				.collect(Collectors.toList()));
		results.addAll(Pis.getLineService()
				.stream()
				.filter(line -> line.getId().equals(query) || line.getName().contains(query) || query.contains(line.getName()))
				.collect(Collectors.toList()));
		model.addAttribute("results", results);
		return "search.html";
	}

	@GetMapping("/studio")
	public static String getDashboardPage(Model model) {
		return "studio/dashboard.html";
	}

}
