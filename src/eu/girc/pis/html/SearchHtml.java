package eu.girc.pis.html;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.Entity;
import eu.girc.pis.util.EntityList;

public class SearchHtml {

	private static String searchBar = Resource.getTextFile("search.html");
	
	public static String buildSearch() {
		return Html.buildHtml(searchBar, false);
	}
	
	public static String buildSeachResults(String search) {
		String string = searchBar;
		string += new HtmlTag("h2", "Search results for \"" + search + "\"");
		EntityList<Entity> entities = new EntityList<>();
		Main.getStations().forEach(station -> entities.add(station));
		Main.getLines().forEach(line -> entities.add(line));
		EntityList<Entity> results = entities.sort().searchForDisplayName(search);
		if (results.size() > 0) {
			for (Entity entity : results) {
				if (entity instanceof Station) {
					string += StationHtml.buildStationPreview((Station) entity);
				} else if (entity instanceof Line) {
					string += LineHtml.buildLinePreview((Line) entity);
				}
			}
		} else {
			string += new HtmlTag("div", "No search results found!", "box").toString();
		}
		return Html.buildHtml(string, false);
	}
	
}
