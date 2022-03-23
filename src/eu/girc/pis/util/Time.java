package eu.girc.pis.util;

import java.util.Collections;
import java.util.Comparator;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;

public class Time {

	private int hour;
	private int minute;
	
	public Time(int hour, int minute) {
		if (hour < 24 && hour > -1) {
			if (minute < 60 && minute > -1) {
				this.hour = hour;
				this.minute = minute;
			} else {
				throw new IllegalArgumentException("Only minutes in range 0 - 59, " + minute + " is not allowed!");
			}
		} else {
			throw new IllegalArgumentException("Only hours in range 0 - 23, " + hour + " is not allowed!");
		}
	}
	
	public Time(String string) {
		this(getTimeFromString(string, 0), getTimeFromString(string, 1));
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int getTimeValue() {
		int value = minute;
		value += (hour * 60);
		return value;
	}
	
	public Time addTime(int hour, int minute) {
		int newHour = (this.hour + hour + ((this.minute + minute) / 60)) % 24;
		int newMinute = (this.minute + minute) % 60;
		return new Time(newHour, newMinute);
	}
	
	public static EntityList<Line> timeSort(EntityList<Line> lines) {
		return timeSort(lines, null);
	}
	
	public static EntityList<Line> timeSort(EntityList<Line> lines, Station station) {
		if (station != null) lines.forEach((line) -> line.calculateDepartueTimes());
		Collections.sort(lines.getEntities(), new Comparator<Line>() {
			@Override
			public int compare(Line line1, Line line2) {
				int time1 = line1.getDeparture().getTimeValue();
				int time2 = line1.getDeparture().getTimeValue();
				if (station != null) {
					if (line1.getLineStation(station) != null) time1 = line1.getLineStation(station).getDeparture().getTimeValue();
					if (line2.getLineStation(station) != null) time2 = line2.getLineStation(station).getDeparture().getTimeValue();
				}
				return (time1 + line1.getDelay()) - (time2 + line2.getDelay());
			}
		});
		return lines;
	}
	
	public static boolean isValid(String string) {
		try {
			new Time(getTimeFromString(string, 0), getTimeFromString(string, 1));
			return true;
		} catch (IllegalArgumentException  exception) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		String hour = Integer.toString(this.hour);
		String minute = Integer.toString(this.minute);
		while (hour.length() < 2) hour = "0" + hour;
		while (minute.length() < 2) minute = "0" + minute;
		return hour + ":" + minute;
	}
	
	private static int getTimeFromString(String string, int index) {
		if (string.contains(":") && string.split(":").length == 2) {
			String time[] = string.split(":");
			int number = 0;
			try {
				number = Integer.parseInt(time[index]);
				return number;
			} catch (NumberFormatException exception) {
				throw new IllegalArgumentException("Wrong format, must be hh:mm");
			}
		} else {
			throw new IllegalArgumentException("Wrong format, must be hh:mm");
		}
	}
	
}