package eu.girc.pis.model;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import eu.girc.pis.main.Pis;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platforms"})
public class Station implements PisComponent, Comparable<Station> {

	private final String id;
	private final String name;
	private final int platforms;

	@JsonCreator
	@ConstructorProperties({"id", "name", "platforms"})
	public Station(String id, String name, int platforms) {
		this.id = id;
		this.name = name;
		this.platforms = platforms;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getPlatforms() {
		return platforms;
	}
	
	public List<Line> getLines() {
		return Pis.getLineService()
			.stream()
			.filter(line -> line.getStations().stream().filter(lineStation -> lineStation.getId().equals(id)).findAny().isPresent())
			.collect(Collectors.toList());
	}

	@Override
	public int compareTo(Station station) {
		return this.id.toLowerCase().compareTo(station.getId().toLowerCase());
	}
	
	public static Station empty() {
		return new Station(null, null, 0);
	}

}
