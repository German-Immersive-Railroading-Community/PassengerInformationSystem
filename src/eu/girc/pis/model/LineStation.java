package eu.girc.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.girc.pis.utils.TimeDeserializer;
import eu.girc.pis.utils.TimeSerializer;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platform", "departure", "cancelled", "delay", "changedPlatform", "passed"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineStation implements PisComponent {

	private String id;
	private String name;
	private int platform;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime departure;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private boolean passed;

	public LineStation() {
	}

	@JsonCreator
	@ConstructorProperties({"id", "name", "platform", "departure", "cancelled", "delay", "changedPlatform", "passed"})
	public LineStation(String id, String name, int platform, LocalTime departure,
			boolean cancelled, int delay, int changedPlatform, boolean passed) {
		this.id = id;
		this.name = name;
		this.platform = platform;
		this.departure = departure;
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

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public LocalTime getDeparture() {
		return departure;
	}
	
	public String getDepartureAsString() {
		if (getDeparture() == null) return "00:00";
		return getDeparture().format(DateTimeFormatter.ofPattern("HH:mm"));
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
	
	public boolean isPassed() {
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
