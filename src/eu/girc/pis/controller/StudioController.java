package eu.girc.pis.controller;

import java.util.Optional;
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
import eu.girc.pis.entities.EntityType;
import eu.girc.pis.entities.Entity;
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.entities.User;
import eu.girc.pis.main.PIS;
import eu.girc.pis.utils.TrainType;

@Controller
@RequestMapping("/studio")
public class StudioController {
	
	@GetMapping
	public static String getDashboardPage(Model model) {
		model.addAttribute("lines", PIS.getLines());
		return "studio/dashboard.html";
	}
	
	@GetMapping("/{type}")
	public static String getPage(@PathVariable("type") String type, Model model) {
		EntityType entityType = EntityType.fromString(type).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute(entityType.name().toLowerCase(), entityType.getSet());
		return "studio/" + entityType.name().toLowerCase() + ".html";
	}
	
	@GetMapping("/{type}/edit")
	public static String getEditPage(
			@PathVariable("type") String type,
			@RequestParam(name = "id", required = false) String id,
			Model model) {
		EntityType entityType = EntityType.fromString(type).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Optional<? extends Entity> entity = entityType.getSet().stream().filter(entry -> entry.getId().equals(id)).findAny();
		model.addAttribute(
				entityType.name().toLowerCase().substring(0, entityType.name().length() - 1), 
				entity.isPresent() ? entity.get() : entityType.getStandard());
		if (entityType == EntityType.LINES) {
			model.addAttribute("trainType", TrainType.values());
			model.addAttribute("stationObjects", PIS.getStations());
		}
		return "studio/edit/edit_" + entityType.name().toLowerCase() + ".html";
	}
	
	@PostMapping("/stations/edit")
	public static String postStationEditPage(@ModelAttribute Station station, Model model) {
		return processPost(EntityType.STATIONS, station, model, () -> PIS.getStations().add(station));
	}
	
	@PostMapping("/lines/edit")
	public static String postLineEditPage(@ModelAttribute Line line, Model model) {
		return processPost(EntityType.LINES, line, model, () -> {
			line.generate12BitIdIfUnset();
			line.calculateTime();
			line.getStations().forEach(station -> {
				station.setName(PIS.find(PIS.getStations(), station.getId()).map(lineStation -> lineStation.getName()).orElse(station.getId()));
			});
			PIS.getLines().add(line);
		});
	}
	
	@PostMapping("/users/edit")
	public static String postLineEditPage(@ModelAttribute User user, Model model) {
		return processPost(EntityType.USERS, user, model, () -> PIS.getUsers().add(user));
	}
	
	@GetMapping("/{type}/delete")
	public static String deleteEditPage(@PathVariable("type") String type, @RequestParam(name = "id", required = false) String id) {
		EntityType entityType = EntityType.fromString(type).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		PIS.find(entityType.getSet(), id).ifPresent(entityType.getSet()::remove);
		entityType.save();
		return "redirect:/studio/" + entityType.name().toLowerCase();
	}
	
	private static String processPost(EntityType type, Entity entity, Model model, Runnable add) {
		model.addAttribute(type.name().toLowerCase().substring(0, type.name().length() - 1), entity);
		PIS.find(type.getSet(), entity.getId()).ifPresent(type.getSet()::remove);
		add.run();
		type.save();
		return "redirect:/studio/" + type.name().toLowerCase();
	}
	
}
