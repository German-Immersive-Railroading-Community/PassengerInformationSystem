package eu.girc.pis.controller;

import java.util.Set;
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
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.main.PIS;

@RestController
@RequestMapping("/api/stations")
public class ApiStationController {

	@GetMapping
	public static Set<Station> getStations() {
		return PIS.getStations();
	}

	@GetMapping("/{id}")
	public static Station getStation(@PathVariable("id") String id) {
		return PIS.find(PIS.getStations(), id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/{id}/lines")
	public static Set<Line> getStationLines(@PathVariable("id") String id) {
		return PIS.getLines()
			.stream()
			.filter(line -> line.getStations().stream().filter(station -> station.getId().equals(id)).findAny().isPresent())
			.collect(Collectors.toSet());
	}

	@GetMapping("/{id}/lines/{platform}")
	public static Set<Line> getStationLinesAtPlatform(@PathVariable("id") String id, @PathVariable("platform") int platform) {
		return PIS.getLines()
				.stream()
				.filter(line -> line.getStations().stream().filter(station -> 
					station.getId().equals(id) && station.getPlatform() == platform
				).findAny().isPresent())
				.collect(Collectors.toSet());
	}

	@PostMapping
	public static void postStation(@RequestBody Station station) {
		PIS.find(PIS.getStations(), station.getId()).ifPresent(PIS.getStations()::remove);
		PIS.getStations().add(station);
		PIS.saveStations();
	}

	@DeleteMapping
	public static void deleteStation(@RequestBody Station station) {
		PIS.find(PIS.getStations(), station.getId()).ifPresent(PIS.getStations()::remove);
		PIS.saveStations();
	}

}
