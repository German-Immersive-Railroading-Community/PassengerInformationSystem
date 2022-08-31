package eu.girc.pis.entities;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.girc.pis.utils.TimeDeserializer;
import eu.girc.pis.utils.TimeSerializer;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platform", "departure", "travelTimeFromLastStation", "cancelled", "delay", "changedPlatform", "passed"})
public class LineStation implements PisEntity {

	private String id;
	private String name;
	private int platform;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime departure;
	private int travelTimeFromLastStation;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private boolean passed;
	
	public LineStation() {}

	@JsonCreator
	public LineStation(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("platform") int platform,
			@JsonProperty("departure") LocalTime departure,
			@JsonProperty("travelTimeFromLastStation") int travelTimeFromLastStation,
			@JsonProperty("cancelled") boolean cancelled,
			@JsonProperty("delay") int delay,
			@JsonProperty("changedPlatform") int changedPlatform,
			@JsonProperty("passed") boolean passed) {
		this.id = id;
		this.name = name;
		this.platform = platform;
		this.departure = departure;
		this.travelTimeFromLastStation = travelTimeFromLastStation;
		this.cancelled = cancelled;
		this.delay = delay;
		this.changedPlatform = changedPlatform;
		this.passed = passed;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
		
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getPlatform() {
		return platform;
	}

	public int getDelay() {
		return delay;
	}

	public void setTravelTimeFromLastStation(int travelTimeFromLastStation) {
		this.travelTimeFromLastStation = travelTimeFromLastStation;
	}
	
	public int getTravelTimeFromLastStation() {
		return travelTimeFromLastStation;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	protected void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setChangedPlatform(int changedPlatform) {
		this.changedPlatform = changedPlatform;
	}

	public int getChangedPlatform() {
		return changedPlatform;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public boolean isPassed() {
		return passed;
	}
	
	public String getStatusColor(Line line) {
		boolean statusActive = line.getFirstStation().isPassed() && !line.getLastStation().isPassed() && !line.isCancelled();
		if (!statusActive) {
			return "light-grey";
		} else if (isPassed()) {
			return "blue";
		} else {
			return "dark-grey";
		}
	}

}
