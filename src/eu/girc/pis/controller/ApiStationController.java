package eu.girc.pis.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.girc.pis.component.Line;
import eu.girc.pis.component.Station;
import eu.girc.pis.main.Pis;

@RestController
@RequestMapping("/api/stations")
public class ApiStationController {

	@GetMapping
	public static List<Station> getStations() {
		return Pis.getStationService().getAsList();
	}

	@GetMapping("/{id}")
	public static Station getStation(@PathVariable("id") String id) {
		return Pis.getStationService().get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/{id}/lines")
	public static List<Line> getStationLines(@PathVariable("id") String id) {
		return Pis.getLineService()
				.stream()
				.filter(line -> line.getStations().stream().filter(station -> station.getId().equals(id)).findAny().isPresent())
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}/lines/{platform}")
	public static List<Line> getStationLinesAtPlatform(@PathVariable("id") String id, @PathVariable("platform") int platform) {
		return Pis.getLineService()
				.stream()
				.filter(line -> line.getStations().stream().filter(station -> station.getId().equals(id) && station.getPlatform() == platform)
				.findAny().isPresent())
				.collect(Collectors.toList());
	}

	@PostMapping
	public static void postStation(@RequestBody Station station) {
		Pis.getStationService().remove(station);
		Pis.getStationService().add(station);
	}

	@DeleteMapping
	public static void deleteStation(@RequestBody Station station) {
		final boolean success = Pis.getStationService().remove(station);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

}
