package eu.girc.pis.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.girc.pis.main.PIS;
import eu.girc.pis.utils.TimeDeserializer;
import eu.girc.pis.utils.TimeSerializer;
import eu.girc.pis.utils.TrainType;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "type", "number", "operator", "driver", "departure", "cancelled", "delay", "stations"})
@JsonIgnoreProperties({"empty"})
public class Line implements Entity, Comparable<Line> {

	private String id;
	private TrainType type;
	private int number;
	private String operator;
	private String driver;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime departure;
	private boolean cancelled;
	private int delay;

	final List<LineStation> stations = new ArrayList<>();
	
	@JsonCreator
	public Line(
			@JsonProperty("id") String id,
			@JsonProperty("type") TrainType type, 
			@JsonProperty("number") int number, 
			@JsonProperty("operator") String operator, 
			@JsonProperty("driver") String driver, 
			@JsonProperty("departure") LocalTime departure,
			@JsonProperty("cancelled") boolean cancelled, 
			@JsonProperty("delay") int delay,
			@JsonProperty("stations") List<LineStation> stations) {
		this.id = id;
		this.type = type;
		this.number = number;
		this.operator = operator;
		this.driver = driver;
		this.departure = departure;
		this.cancelled = cancelled;
		this.delay = delay;
		if (stations != null) this.stations.addAll(stations);
	}
	
	public void generate12BitIdIfUnset() {
		if (id == null || id.isEmpty()) id = PIS.generate8BitId();
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		String displayName = type.getToken() + number;
		if (getLastStation() != null) displayName += " " + getLastStation().getName();
		return displayName;
	}
	
	
	public void setType(TrainType type) {
		this.type = type;
	}

	public TrainType getType() {
		return type;
	}
	
	
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}
	
	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}
	
	public LocalTime getDeparture() {
		return departure;
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

	public int getDelay() {
		return delay;
	}

	public void addStation(LineStation station) {
		if (stations.isEmpty()) {
			station.setDeparture(getDeparture());
		} else {
			station.setDeparture(getLastStation().getDeparture().plusMinutes(station.getTravelTimeFromLastStation()));
		}
		stations.add(station);
	}

	public void removeStation(LineStation station) {
		removeStation(stations.indexOf(station));
	}

	public void removeStation(int index) {
		stations.remove(index);
		for (int i = index; i < stations.size(); i++) {
			LineStation station = stations.get(i);
			if (i <= 0) {
				station.setDeparture(getDeparture());
			} else {
				station.setDeparture(
						stations.get(i - 1).getDeparture().plusMinutes(station.getTravelTimeFromLastStation()));
			}
		}
	}

	public LineStation getFirstStation() {
		if (stations.isEmpty()) return null; else return stations.get(0);
	}

	public LineStation getLastStation() {
		if (stations.isEmpty()) return null; else return stations.get(stations.size() - 1);
	}

	public boolean isEmpty() {
		return stations.isEmpty();
	}

	public int size() {
		return stations.size();
	}

	public List<LineStation> getStations() {
		final List<LineStation> lineStations = new ArrayList<>();
		lineStations.addAll(stations);
		return stations;
	}

	@Override
	public int compareTo(Line line) {
		return this.getDeparture().plusMinutes(this.getDelay())
				.compareTo(line.getDeparture().plusMinutes(line.getDelay()));
	}

}
