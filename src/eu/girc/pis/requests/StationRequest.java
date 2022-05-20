package eu.girc.pis.requests;

import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;
import eu.girc.pis.util.Html;
import eu.girc.pis.util.HtmlTag;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class StationRequest implements HttpHandler {
	
	private static String stationPreview = Resource.getTextFile("station-preview.html");
	private static String stationComponent = Resource.getTextFile("station-component.html");

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, buildStationList(Main.getStations()));
		} else if (args.length == 2 && Main.getStations().contains(args[1])) {
			RequestHandler.sendHtml(exchange, buildStationComponent(Main.getStations().get(args[1])));
		}
	}
	
	public static String buildStationPreview(Station station) {
		String string = stationPreview;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		return string;
	}
	
	public static String buildStationList(EntityList<Station> stations) {
		if (stations.size() > 0) {
			String string = "";
			for (Station station : stations.sort()) {
				string += buildStationPreview(station);
			}
			return Html.buildHtml("stations", string, true);
		} else {
			return new HtmlTag("div", "No stations found!", "box").toString();
		}
	}

	private static String buildStationComponent(Station station) {
		String string = stationComponent;
		if (station.getBox() != null) string = station.getBox().toHtml() + "\n" + string;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		string = string.replace("{plattforms}", Integer.toString(station.getPlatforms()));
		string += new HtmlTag("h2", "Lines via station " + station.getDisplayName());
		string += LineRequest.buildLineList(station);
		return Html.buildHtml("stations", string, true);
	}

}
