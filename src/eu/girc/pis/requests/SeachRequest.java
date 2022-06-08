package eu.girc.pis.requests;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.Entity;
import eu.girc.pis.util.EntityList;
import eu.girc.pis.util.Html;
import eu.girc.pis.util.HtmlTag;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class SeachRequest implements HttpHandler {
	
	private static final String searchBar = Resource.getTextFile("search.html");

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, buildSearch());
		} else {
			String search = args[1].replace('&', ' ');
			RequestHandler.sendHtml(exchange, buildSeachResults(search));
		}
	}
	
	private static String buildSearch() {
		return Html.buildHtml(searchBar, false);
	}
	
	private static String buildSeachResults(String search) {
		String string = searchBar;
		string += new HtmlTag("h2", "Search results for \"" + search + "\"");
		final EntityList<Entity> entities = new EntityList<>();
		Main.getStations().forEach(station -> entities.add(station));
		Main.getLines().forEach(line -> entities.add(line));
		EntityList<Entity> results = entities.sort().searchForDisplayName(search);
		if (results.size() > 0) {
			for (Entity entity : results) {
				if (entity instanceof Station) {
					string += StationRequest.buildStationPreview((Station) entity);
				} else if (entity instanceof Line) {
					string += LineRequest.buildLinePreview((Line) entity);
				}
			}
		} else {
			string += new HtmlTag("div", "No search results found!", "box").toString();
		}
		return Html.buildHtml(string, false);
	}

}
