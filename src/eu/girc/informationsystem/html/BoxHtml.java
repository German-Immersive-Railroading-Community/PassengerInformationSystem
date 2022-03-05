package eu.girc.informationsystem.html;

public class BoxHtml extends Html {
	
	private static String box = "<div class=\"container box\">{content}</div>\n";

	public BoxHtml(String content) {
		super();
		setString(box.replace("{content}", content));
	}

}
