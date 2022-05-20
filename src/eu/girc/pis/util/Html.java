package eu.girc.pis.util;

import eu.girc.pis.resources.Resource;

public class Html {
	
	private static String site = Resource.getTextFile("site.html");
	private static String index = Resource.getTextFile("index.html");
	private static String error404 = Resource.getTextFile("404.html");
	
	public static String buildHtml(String content, boolean reload) {
		return buildHtml("pis", content, reload);
	}
	
	public static String buildHtml(String menu, String content, boolean reload) {
		String string = site;
		if (!reload) string = string.replace("\n        <meta http-equiv=\"refresh\" content=\"30\">", "");
		string = string.replace("{menu-focus}", menu);
		string = string.replace("{content}", content);
		return string;
	}
	
	public static String buildIndexHtml() {
		return buildHtml(index, false);
	}
	
	public static String build404Html() {
		return buildHtml(error404, false);
	}

}
