package eu.girc.informationsystem.html;

import eu.girc.informationsystem.resources.Resource;

public class Html {
	
	private static String site = Resource.getTextFile("site.html");
	
	private String string;
	
	public Html() {
		string = "";
	}
	
	public Html(String content) {
		this(content, true);
	}
	
	public Html(String content, boolean reload) {
		this("null", content);
		if (!reload) string = string.replace("\n        <meta http-equiv=\"refresh\" content=\"30\">", "");
	}
	
	public Html(String menu, String content) {
		this(menu, content, true);
	}
	
	public Html(String menu, String content, boolean reload) {
		string = site;
		if (!reload) string = string.replace("\n        <meta http-equiv=\"refresh\" content=\"30\">", "");
		string = string.replace("{menu-focus}", menu);
		string = string.replace("{content}", content);
	}
	
	protected void setString(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}

}
