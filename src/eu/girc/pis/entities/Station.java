package eu.girc.pis.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platforms"})
public class Station implements Entity, Comparable<Station> {

	private final String id;
	private final String name;
	private final int platforms;

	@JsonCreator
	public Station(
			@JsonProperty("id") String id, 
			@JsonProperty("name") String name, 
			@JsonProperty("platforms") int platforms) {
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

	@Override
	public int compareTo(Station station) {
		return this.getId().compareTo(station.getId());
	}

}
