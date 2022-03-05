package eu.girc.informationsystem.html;

public class HeadlineHtml extends Html {
	
	private static String headline = "<div class=\"container\" style=\"padding: 0px; margin-top: 80px;\"><h2>{content}</h2></div>\n";

	public HeadlineHtml(String content) {
		super();
		setString(headline.replace("{content}", content));
	}

}
