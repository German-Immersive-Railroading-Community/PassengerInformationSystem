package eu.girc.informationsystem.html;

import eu.girc.informationsystem.resources.Resource;

public class Html {
	
	private static String site = Resource.getTextFile("site.html");
	
	private String string;
	
	public Html(String menu, String content) {
		string = site;
		string = string.replace("{menu-focus}", menu);
		string = string.replace("{content}", content);
	}
	
	public Html(String content) {
		this("null", content);
	}
	
	public Html() {
		string = "";
	}
	
	protected void setString(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}

}
