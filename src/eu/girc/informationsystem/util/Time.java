package eu.girc.informationsystem.util;

import java.util.ArrayList;
import java.util.HashMap;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;

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
	
	public Time addTime(int hour, int minute) {
		int newHour = (this.hour + hour + ((this.minute + minute) / 60)) % 24;
		int newMinute = (this.minute + minute) % 60;
		return new Time(newHour, newMinute);
	}
	
	public static EntityList<Line> timeSort(EntityList<Line> lines) {
		return timeSort(lines, null);
	}
	
	public static EntityList<Line> timeSort(EntityList<Line> lines, Station station) {
		if (lines.getEntities().size() > 1) {
			ArrayList<Time> times = new ArrayList<>();
			HashMap<Time, Line> objects = new HashMap<>();
			for (Line line : lines) {
				line.calculateDepartueTimes();
				if (station != null) {
					if (line.getLineStation(station) != null) {
						Time time = line.getDeparture().addTime(0, line.getDelay());
						line.getLineStation(station).getDeparture();
						times.add(time);
						objects.put(time, line);
					}
				} else {
					Time time = line.getDeparture().addTime(0, line.getDelay());
					times.add(time);
					objects.put(time, line);
				}
			}
			Time min = null;
			Time max = null;
			for (int i = 0; i < times.size() / 2; i++) {
				min = times.get(i);
				max = times.get(i);
				for (int j = i; j < times.size() - i; j++) {
					if (times.get(j).getValueFromTime() > max.getValueFromTime()) {
						max = times.get(j);
					}
					if (times.get(j).getValueFromTime() < min.getValueFromTime()) {
						min = times.get(j);
					}
				}
				swap(times, min, i);
				swap(times, max, times.size() - 1 - i);
			}
			lines.clear();
			times.forEach(time -> lines.add(objects.get(time)));
		}
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
		if (string.split(":").length == 2) {
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
	
	private int getValueFromTime() {
		int value = minute;
		value += (hour * 60);
		return value;
	}
	
	private static void swap(ArrayList<Time> times, Time time, int pos) {
		Time swap = times.get(pos);
		int newPos = times.indexOf(time);
		times.set(pos, time);
		times.set(newPos, swap);
	}
	
}