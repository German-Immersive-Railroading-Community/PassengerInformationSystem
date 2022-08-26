package eu.girc.pis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@GetMapping
	public static ApiManual getIndex() {
		return new ApiManual();
	}
	
	@SuppressWarnings("unused")
	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonPropertyOrder({"stations", "specific_station", "lines_of_station", "lines_of_plattform", "lines", "specific_line"})
	private static class ApiManual {
		private String stations = "/api/stations";
		private String specific_station = "/api/stations/{name}";
		private String lines_of_station = "/api/stations/{name}/lines";
		private String lines_of_plattform = "/api/stations/{name}/lines/{platform}";
		private String lines = "/api/lines";
		private String specific_line = "/api/lines/{id}";
	}

}
