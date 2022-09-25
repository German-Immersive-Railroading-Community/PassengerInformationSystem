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
import eu.girc.pis.model.Line;
import eu.girc.pis.model.TrainType;

@Controller
@RequestMapping("/")
public class LineController {

	@GetMapping("/lines")
	public String getListPage(Model model) {
		model.addAttribute("lines", Pis.getLineService().getAsList());
		return "lines.html";
	}

	@GetMapping("/lines/{id}")
	public String getListPage(@PathVariable("id") String id, Model model) {
		final Line line = Pis.getLineService().get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("line", line);
		return "line.html";
	}

	@GetMapping("/studio/lines")
	public String getStudioListPage(Model model) {
		model.addAttribute("lines", Pis.getLineService().getAsList());
		return "studio/lines.html";
	}

	@GetMapping("/studio/lines/edit")
	public String getEditPage(@RequestParam(name = "id", required = false) String id, Model model) {
		final Line line = Pis.getLineService().get(id).orElse(Line.empty());
		model.addAttribute("line", line);
		model.addAttribute("trainType", TrainType.values());
		model.addAttribute("stationObjects", Pis.getStationService().getAsList());
		return "studio/edit/edit_line.html";
	}

	@PostMapping("/studio/lines/edit")
	public String postEditPage(Line line) {
		Pis.getLineService().remove(line.getId());
		Pis.getLineService().add(line);
		return "redirect:/studio/lines";
	}

	@GetMapping("/studio/lines/delete")
	public String deleteComponent(@RequestParam(name = "id", required = true) String id, Model model) {
		Pis.getLineService().remove(id);
		return "redirect:/studio/lines";
	}

}
