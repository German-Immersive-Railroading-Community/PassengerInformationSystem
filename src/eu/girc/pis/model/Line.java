package eu.girc.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.girc.pis.main.Pis;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "type", "number", "lenght", "operator", "driver", "cancelled", "stations"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Line implements PisComponent, Comparable<Line> {

	private String id;
	private TrainType type;
	private int number;
	private int lenght;
	private String operator;
	private String driver;
	private boolean cancelled;

	private ArrayList<LineStation> stations = new ArrayList<>();

	@JsonCreator
	@ConstructorProperties({"id", "type", "number", "lenght", "operator", "driver", "cancelled", "stations"})
	public Line(String id, TrainType type, int number, int lenght, String operator, String driver, boolean cancelled, ArrayList<LineStation> stations) {
		this.id = id;
		this.type = type;
		this.number = number;
		this.lenght = lenght;
		this.operator = operator;
		this.driver = driver;
		this.cancelled = cancelled;
		if (stations != null) this.stations.addAll(stations);
	}

	public void generate12BitIdIfUnset() {
		if (id == null || id.isEmpty()) id = Pis.generate8BitId();
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
	
	public int getLenght() {
		return lenght;
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

	public LocalTime getDeparture() {
		if (stations.size() > 0) return stations.get(0).getDeparture();
		return LocalTime.of(23, 59);
	}
	
	public String getDepartureAsString() {
		if (getDeparture() == null) return "00:00";
		return getDeparture().format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public int getDelay() {
		int delay =  stations.stream()
			.filter(station -> !station.hasPassed())
			.findFirst()
			.map(LineStation::getDelay)
			.orElse(-1);
		if (delay == -1) {
			if (stations.size() > 0) return stations.get(stations.size() - 1).getDelay();
			else return 0;
		}
		return delay;
	}

	public LineStation getFirstStation() {
		if (stations.isEmpty()) return null;
		else return stations.get(0);
	}

	public LineStation getLastStation() {
		if (stations.isEmpty()) return null;
		else return stations.get(stations.size() - 1);
	}

	public boolean isEmpty() {
		return stations.isEmpty();
	}

	public int size() {
		return stations.size();
	}

	public ArrayList<LineStation> getStations() {
		return stations;
	}

	@Override
	public int compareTo(Line line) {
		return this.getDeparture().plusMinutes(this.getDelay())
				.compareTo(line.getDeparture().plusMinutes(line.getDelay()));
	}

	public static Line empty() {
		return new Line(null, TrainType.INTERCITY_EXPRESS, 0, 0, null, null, false, null);
	}

}
