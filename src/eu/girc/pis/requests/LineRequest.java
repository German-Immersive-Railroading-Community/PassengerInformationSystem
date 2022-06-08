package eu.girc.pis.requests;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.LineStation;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;
import eu.girc.pis.util.Html;
import eu.girc.pis.util.HtmlTag;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class LineRequest implements HttpHandler{

	private static final String linePreview = Resource.getTextFile("line-preview.html");
	private static final String lineComponent = Resource.getTextFile("line-component.html");
	private static final String stationComponent = Resource.getTextFile("line-station-component.html");
	
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		final String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, buildLineList(Main.getLines()));
		} else if (args.length == 2 && Main.getLines().contains(args[1])) {
			RequestHandler.sendHtml(exchange, buildLineComponent(Main.getLines().get(args[1])));
		}
	}
	
	public static String buildLinePreview(Line line) {
		String string = linePreview;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", buildLineInformation(line, false));
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
		String stationList = "";
		for (int i = 0; i < line.getStations().size() - 1; i++) {
			stationList += line.getStations().get(i).getDisplayName() + " - ";
		}
		stationList += line.getStations().getLast().getDisplayName();
		string = string.replace("{stations}", stationList);
		return string;
	}
	
	public static String buildLineList(EntityList<Line> lines) {
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines.sort()) {
				string += buildLinePreview(line);
			}
			return Html.buildHtml("lines", string, true);
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}
	
	public static String buildLineList(Station station) {
		EntityList<Line> lines = station.getLines();
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines) {
				string += buildLinePreview(line);
			}
			return string;
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}
	
	private static String buildLineComponent(Line line) {
		String string = lineComponent;
		if (line.getBox() != null) string = line.getBox().toHtml() + "\n" + string;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{type}", line.getType());
		string = string.replace("{operator}", line.getOperator().toString());
		string = string.replace("{driver}", line.getDriver().toString());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", buildLineInformation(line, true));
		string = string.replace("{cancelled}", (line.isCancelled()) ? "<label class=\"red\">CANCELLED</label>&nbsp;&nbsp;" : "");
		line.calculateDepartueTimes();
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
		string = string.replace("{content}", buildStationList(line.getStations()));
		return Html.buildHtml("lines", string, true);
	}
	
	private static String buildStationList(EntityList<LineStation> stations) {
		String string = "";
		boolean statusActive = stations.getFirst().hasPassed() && !stations.getLast().hasPassed() && !stations.getFirst().getLine().isCancelled();
		for (LineStation station : stations) {
			String stationComponent = LineRequest.stationComponent;
			stationComponent = stationComponent.replace("{departure}", buildLineString(station.getDeparture().toString("hh:mm"), station, stations));
			stationComponent = stationComponent.replace("{delay}", buildStationDelay(station));
			stationComponent = stationComponent.replace("{name}", station.getName());
			stationComponent = stationComponent.replace("{displayName}", buildLineString(station.getDisplayName(), station, stations));
			if (station.isCancelled()) {
				stationComponent = stationComponent.replace("{plattform}", "<s class=\"red-text\"> Pl." + station.getPlatform() + "</s>");
			} else if (station.getChangedPlatform() != 0 && station.getChangedPlatform() != station.getPlatform()) {
				stationComponent = stationComponent.replace("{plattform}", "<s> Pl." + station.getPlatform() + "</s><span style=\"color: #e60000 !important\"> Pl. " + station.getChangedPlatform() + "</span>");
			} else {
				stationComponent = stationComponent.replace("{plattform}", "Pl. " + station.getPlatform());
			}
			if (statusActive) {
				if (station.hasPassed()) stationComponent = stationComponent.replace("{status}", "blue");
				else stationComponent = stationComponent.replace("{status}", "dark-grey");
			} else {
				stationComponent = stationComponent.replace("{status}", "light-grey");
			}
			string += stationComponent;
		}
		return string;
	}
	
	private static String buildLineInformation(Line line, boolean onlyDelay) {
		int delay = line.getDelay();
		String string = "";
		if (line.isCancelled()) if (!onlyDelay) return "<label class=\"red\">CANCELLED</label>&nbsp;&nbsp;"; else return "";
		if (delay > 2 && delay < 6) string += "<label class=\"yellow\">+" + delay + "</label>" + ((onlyDelay) ? "" : "&nbsp;&nbsp;");
		else if (delay > 5) string += "<label class=\"red\">+" + delay + "</label>" + ((onlyDelay) ? "" : "&nbsp;&nbsp;");
		if (!onlyDelay && line.getBox() != null) string += "<label class=\"" + line.getBox().getColor() + "\">&nbsp;!&nbsp;</label>&nbsp;&nbsp;";
		return string;
	}
	
	private static String buildStationDelay(LineStation station) {
		int delay = station.getDelay();
		if (station.isCancelled() || delay == 0) return "";
		else if (delay < 6) return "<span class=\"yellow-text\">+" + delay + "</span>";
		else return "<span class=\"red-text\">+" + delay + "</span>";
	}
	
	private static String buildLineString(String string, LineStation station, EntityList<LineStation> stations) {
		if (station == stations.getLast()) string = "<b>" + string + "</b>";
		if (station.isCancelled()) string = "<s class=\"red-text\">" + string + "</s>";
		return string;
	}
	
}
