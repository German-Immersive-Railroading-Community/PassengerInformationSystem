package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class LineStation {
	
	@JsonElement
	private Station station;
	
	@JsonElement
	private int plattform;
	
	@JsonElement
	private String departure;
	
	@JsonElement
	private int travelTimeFromLastStation;

	public LineStation(Station station, int plattform, int travelTimeFromLastStation) {
		this.station = station;
		if (plattform <= station.getPlattforms()) {
			this.plattform = plattform;
		} else {
			this.plattform = 0;
			throw new IllegalArgumentException("Station " + station.getName() + " have only " + station.getPlattforms() + " plattforms!");
		}
		
		this.travelTimeFromLastStation = travelTimeFromLastStation;
	}
	
	public Station getStation() {
		return station;
	}
	
	public int getPlattform() {
		return plattform;
	}
	
	protected void setDeparture(Time time) {
		this.departure = time.toString();
	}
	
	public Time getDeparture() {
		return new Time(departure);
	}
	
	public int getTravelTimeFromLastStation() {
		return travelTimeFromLastStation;
	}
	
	@Override
	public String toString() {
		JsonParser parser = new JsonParser();
		parser.setClassAsJsonObject("", this);
		return parser.toString();
	}

}
