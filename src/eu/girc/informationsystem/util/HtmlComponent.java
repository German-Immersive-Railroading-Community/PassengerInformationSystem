package eu.girc.informationsystem.util;

import eu.derzauberer.javautils.parser.JsonParser;

public interface HtmlComponent {
	
	public default String buildComponent(String html, JsonParser parser) {
		String result = html;
		for (String key : parser.getKeys()) {
			if (parser.get(key) instanceof String || parser.get(key) instanceof Number) {
				result = result.replace("{" + key + "}", parser.getString(key));
			}
		}
		return result;
	}

}
