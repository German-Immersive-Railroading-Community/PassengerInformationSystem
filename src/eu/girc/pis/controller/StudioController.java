package eu.girc.pis.controller;

import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.entities.User;
import eu.girc.pis.main.Pis;
import eu.girc.pis.utils.PisService;
import eu.girc.pis.utils.TrainType;

@Controller
@RequestMapping("/studio")
public class StudioController {
	
	@GetMapping
	public static String getDashboardPage(Model model) {
		return "studio/dashboard.html";
	}
	
	@GetMapping("/{type}")
	public static String getPage(@PathVariable("type") String type, Model model) {
		List<?> entities = PisService.getService(type).map(service -> service.getAsList()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute(type, entities);
		return "studio/" + type + ".html";
	}
	
	@GetMapping("/{type}/edit")
	public static String getEditPage(
			@PathVariable("type") String type,
			@RequestParam(name = "id", required = false) String id,
			Model model) {
		if (type.equals("stations")) {
			model.addAttribute("station", Pis.getStationService().get(id).orElse(new Station("", "", 1)));
		} else if (type.equals("lines")) {
			model.addAttribute("line", Pis.getLineService().get(id).orElse(new Line(null, TrainType.INTERCITY_EXPRESS, 0, "", "", LocalTime.of(0, 0), false, 0, null)));
		} else if (type.equals("templates")) {
			//
		} else if (type.equals("archived")) {
			//
		} else if (type.equals("users")) {
			model.addAttribute("user", Pis.getUserService().get(id).orElse(new User("", "", "", "", false, "")));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		model.addAttribute("trainType", TrainType.values());
		model.addAttribute("stationObjects", Pis.getStationService().getAsList());
		return "studio/edit/edit_" + type + ".html";
	}
	
	@PostMapping("/stations/edit")
	public static String postStationEditPage(@ModelAttribute Station station) {
		System.out.println(station.getId() + " " + station.getName() + " " + station.getPlatforms());
		Pis.getStationService().remove(station.getId());
		Pis.getStationService().add(station);
		return "redirect:/studio/stations";
	}
	
	@PostMapping("/lines/edit")
	public static String postLineEditPage(@ModelAttribute Line line) {
		Pis.getLineService().remove(line.getId());
		line.generate12BitIdIfUnset();
		line.calculateTime();
		line.getStations().forEach(station -> {
			station.setName(Pis.getStationService().get(line.getId()).map(lineStation -> lineStation.getName()).orElse(station.getId()));
		});
		Pis.getLineService().add(line);
		return "redirect:/studio/lines";
	}
	
	@PostMapping("/users/edit")
	public static String postLineEditPage(@ModelAttribute User user) {
		Pis.getUserService().remove(user.getId());
		Pis.getUserService().add(user);
		return "redirect:/studio/users";
	}
	
	@GetMapping("/{type}/delete")
	public static String deleteEditPage(@PathVariable("type") String type, @RequestParam(name = "id", required = false) String id) {
		PisService<?> service = PisService.getService(type).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		boolean success = service.remove(id);
		return "redirect:/studio/" + type;
	}
	
}
