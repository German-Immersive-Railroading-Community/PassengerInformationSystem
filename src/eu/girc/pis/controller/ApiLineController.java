package eu.girc.pis.controller;

import java.util.Set;
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
import eu.girc.pis.main.PIS;

@RestController
@RequestMapping("/api/lines")
public class ApiLineController {

	@GetMapping
	public static Set<Line> getLines() {
		return PIS.getLines();
	}

	@GetMapping("/{id}")
	public static Line getLine(@PathVariable("id") String id) {
		return PIS.find(PIS.getLines(), id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public static void postLine(@RequestBody Line line) {
		PIS.find(PIS.getLines(), line.getId()).ifPresent(PIS.getLines()::remove);
		PIS.getLines().add(line);
		PIS.saveLines();
	}

	@DeleteMapping
	public static void deleteLine(@RequestBody Line line) {
		PIS.find(PIS.getLines(), line.getId()).ifPresent(PIS.getLines()::remove);
		PIS.saveLines();
	}

}
