package eu.girc.pis.html;

import eu.girc.pis.resources.Resource;

public class IndexHtml extends Html {
	
	private static String index = Resource.getTextFile("index.html");

	public IndexHtml() {
		super(index, false);
	}

}
