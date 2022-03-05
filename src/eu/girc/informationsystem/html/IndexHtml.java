package eu.girc.informationsystem.html;

import eu.girc.informationsystem.resources.Resource;

public class IndexHtml extends Html {
	
	private static String index = Resource.getTextFile("index.html");

	public IndexHtml() {
		super(index, false);
	}

}
