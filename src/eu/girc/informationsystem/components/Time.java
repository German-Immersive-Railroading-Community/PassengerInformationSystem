package eu.girc.informationsystem.components;

public class Time {

	private int hour;
	private int minute;
	
	public Time(int hour, int minute) {
		if (hour < 24) {
			if (minute < 60) {
				this.hour = hour;
				this.minute = minute;
			} else {
				throw new IllegalArgumentException("minute > 59");
			}
		} else {
			throw new IllegalArgumentException("hour > 23");
		}
	}
	
	public Time(String string) {
		this(getTimeFromString(string, 0), getTimeFromString(string, 1));
	}
	
	private static int getTimeFromString(String string, int index) {
		if (string.split(":").length == 2) {
			String time[] = string.split(":");
			return Integer.parseInt(time[index]);
		} else {
			throw new IllegalArgumentException("Wrong format, must be hh:mm");
		}
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
	
	@Override
	public String toString() {
		String hour = Integer.toString(this.hour);
		String minute = Integer.toString(this.minute);
		while (hour.length() < 2) hour = "0" + hour;
		while (minute.length() < 2) minute = "0" + minute;
		return hour + ":" + minute;
	}
	
}
