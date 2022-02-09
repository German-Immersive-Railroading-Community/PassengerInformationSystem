package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class LineStation {
	
	@JsonElement
	private Station station;
	
	@JsonElement
	private int plattform;
	
	@JsonElement
	private int travelTimetoNextStation;

	public LineStation(Station station, int plattform, int travelTimetoNextStation) {
		this.station = station;
		if (plattform <= station.getPlattforms()) {
			this.plattform = plattform;
		} else {
			this.plattform = 0;
			throw new IllegalArgumentException("Station " + station.getName() + " have only " + station.getPlattforms() + " plattforms!");
		}
		
		this.travelTimetoNextStation = travelTimetoNextStation;
	}
	
	public Station getStation() {
		return station;
	}
	
	public int getPlattform() {
		return plattform;
	}
	
	public int getTravelTimetoNextStation() {
		return travelTimetoNextStation;
	}
	
	@Override
	public String toString() {
		JsonParser parser = new JsonParser();
		parser.setClassAsJsonObject("", this);
		return parser.toString();
	}

}
