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
public class LineStation implements Entity {

	private final String id;
	private final String name;
	private final int platform;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime departure;
	private final int travelTimeFromLastStation;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private boolean passed;

	public LineStation(Station station, int platform, int travelTimeFromLastStation) {
		this.id = station.getId();
		this.name = station.getName();
		this.platform = platform;
		this.travelTimeFromLastStation = travelTimeFromLastStation;
		this.departure = LocalTime.of(0, 0);
		this.cancelled = false;
		this.delay = 0;
		this.changedPlatform = 0;
		this.passed = false;
	}

	@JsonCreator
	private LineStation(
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
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getPlatform() {
		return platform;
	}

	public int getDelay() {
		return delay;
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

	public boolean hasPassed() {
		return passed;
	}
	
	public String getStatusColor(Line line) {
		boolean statusActive = line.getFirstStation().hasPassed() && !line.getLastStation().hasPassed() && !line.isCancelled();
		if (!statusActive) {
			return "light-grey";
		} else if (hasPassed()) {
			return "blue";
		} else {
			return "dark-grey";
		}
	}

}
