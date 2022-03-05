package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.Entity;
import eu.girc.informationsystem.util.EntityList;

public class SearchHtml extends Html {

	private static String searchBar = Resource.getTextFile("search.html");
	
	public SearchHtml() {
		super(searchBar, false);
	}
	
	public SearchHtml(String search) {
		super(buildSeachResults(search), false);
	}
	
	public static String buildSeachResults(String search) {
		String string = searchBar;
		string += new HeadlineHtml("Seach results for \"" + search + "\"");
		EntityList<Entity> entities = new EntityList<>();
		Main.getStations().forEach(station -> entities.add(station));
		Main.getLines().forEach(line -> entities.add(line));
		EntityList<Entity> results = entities.alphabeticalSort().searchForDisplayName(search);
		if (results.size() > 0) {
			for (Entity entity : results) {
				if (entity instanceof Station) {
					string += StationListHtml.buildStationPreviewHtml((Station) entity);
				} else if (entity instanceof Line) {
					string += LineListHtml.buildLinePreviewHtml((Line) entity);
				}
			}
		} else {
			string += new BoxHtml("No search results found!").toString();
		}
		return string;
	}
	
}
