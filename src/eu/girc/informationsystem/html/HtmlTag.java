package eu.girc.informationsystem.html;

import java.util.ArrayList;
import java.util.List;

public class HtmlTag {
	
	private String tag;
	private String innerHtml;
	private List<String> classes;

	public HtmlTag(String tag, String innerHtml) {
		this.tag = tag;
		this.innerHtml = innerHtml;
		classes = new ArrayList<>();
	}
	
	public HtmlTag(String tag, String innerHtml, String... classes) {
		this(tag, innerHtml);
		for (String string : classes) {
			this.classes.add(string);
		}
	}
	
	public HtmlTag(String tag, String innerHtml, List<String> classes) {
		this(tag, innerHtml);
		this.classes = classes;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setInnerHtml(String innerHtml) {
		this.innerHtml = innerHtml;
	}
	
	public String getInnerHtml() {
		return innerHtml;
	}
	
	public List<String> getClasses() {
		return classes;
	}
	
	private String getStringFromClasses() {
		String string = "";
		for (int i = 0; i < classes.size() - 1; i++) string += classes.get(i) + ", ";
		string += classes.get(classes.size() - 1);
		return string;
	}
	
	@Override
	public String toString() {
		if (classes.isEmpty()) return "<" + tag + ">" + innerHtml + "</" + tag + ">"; 
		return "<" + tag + " class=\"" + getStringFromClasses() + "\">" + innerHtml + "</" + tag + ">";
	}
	
}
