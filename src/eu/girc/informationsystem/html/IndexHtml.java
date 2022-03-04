package eu.girc.informationsystem.html;

import eu.girc.informationsystem.main.Main;

public class IndexHtml extends Html {

	public IndexHtml() {
		super(LineListHtml.buildLineList(Main.getLines()));
	}

}
