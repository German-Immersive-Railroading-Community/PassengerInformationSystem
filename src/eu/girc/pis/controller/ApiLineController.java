package eu.girc.pis.controller;

import java.util.List;
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
import eu.girc.pis.main.Pis;

@RestController
@RequestMapping("/api/lines")
public class ApiLineController {

	@GetMapping
	public static List<Line> getLines() {
		return Pis.getLineService().getAsList();
	}

	@GetMapping("/{id}")
	public static Line getLine(@PathVariable("id") String id) {
		return Pis.getLineService().get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public static void postLine(@RequestBody Line line) {
		Pis.getLineService().remove(line);
		Pis.getLineService().add(line);
	}

	@DeleteMapping
	public static void deleteLine(@RequestBody Line line) {
		final boolean success = Pis.getLineService().remove(line);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

}
