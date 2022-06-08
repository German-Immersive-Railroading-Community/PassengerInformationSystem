package eu.girc.pis.components;

import eu.derzauberer.javautils.parser.JsonParser;

public class Box {
	
	private String title;
	private String text;
	private String color;
	
	public Box(String title, String text, String color) {
		this.title = title;
		this.text = text;
		this.color = color;
	}
	
	public Box(JsonParser parser) {
		this("", "", "light-grey");
		fromJson(parser);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
	public void fromJson(JsonParser parser) {
		title = parser.getString("title");
		text = parser.getString("text");
		color = parser.getString("color");
	}
	
	public JsonParser toJson() {
		final JsonParser parser = new JsonParser();
		parser.set("title", title);
		parser.set("text", text);
		parser.set("color", color);
		return parser;
	}
	
	public String toHtml() {
		return "<div class=\"box " + color + "\"><b>" + title + "</b><br>" + text + "</div>";
	}

}
