package eu.girc.pis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import eu.girc.pis.main.Pis;
import eu.girc.pis.model.Station;

@Controller
@RequestMapping("/")
public class StationController {

	@GetMapping("/stations")
	public String getListPage(Model model) {
		model.addAttribute("stations", Pis.getStationService().getAsList());
		return "stations.html";
	}

	@GetMapping("/stations/{id}")
	public String getListPage(@PathVariable("id") String id, Model model) {
		final Station station = Pis.getStationService().get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("station", station);
		return "station.html";
	}

	@GetMapping("/studio/stations")
	public String getStudioListPage(Model model) {
		model.addAttribute("stations", Pis.getStationService().getAsList());
		return "studio/stations.html";
	}

	@GetMapping("/studio/stations/edit")
	public String getEditPage(@RequestParam(name = "id", required = false) String id, Model model) {
		final Station station = Pis.getStationService().get(id).orElse(Station.empty());
		model.addAttribute("station", station);
		return "studio/edit/edit_station.html";
	}

	@PostMapping("/studio/stations/edit")
	public String postEditPage(Station station) {
		Pis.getStationService().remove(station.getId());
		Pis.getStationService().add(station);
		return "redirect:/studio/stations";
	}

	@GetMapping("/studio/stations/delete")
	public String deleteComponent(@RequestParam(name = "id", required = true) String id, Model model) {
		Pis.getStationService().remove(id);
		return "redirect:/studio/stations";
	}

}
