package eu.girc.informationsystem.html;

import eu.girc.informationsystem.resources.Resource;

public class Error404Html extends Html {

	private static String error404 = Resource.getTextFile("404.html");
	
	public Error404Html() {
		super(error404);
	}

}
