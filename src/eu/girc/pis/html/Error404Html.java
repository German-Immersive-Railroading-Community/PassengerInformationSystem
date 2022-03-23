package eu.girc.pis.html;

import eu.girc.pis.resources.Resource;

public class Error404Html extends Html {

	private static String error404 = Resource.getTextFile("404.html");
	
	public Error404Html() {
		super(error404);
	}

}
