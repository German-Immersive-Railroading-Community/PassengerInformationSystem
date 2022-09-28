package eu.girc.pis.model;

public class Box {
	
	public static final String BLUE = "blue";
	public static final String GREEN = "green";
	public static final String YELLOW = "yellow";
	public static final String RED = "red";
	
	public static final Box BOX_PW_SUCCESS = new Box("Your password has been successfully updated!", GREEN);
	public static final Box BOX_PW_WRONG = new Box("Your old password is wrong!", RED);
	public static final Box BOX_PW_NOT_MATCH = new Box("Your new password does not match the repeated password!", RED);
	
	private final String title;
	private final String text;
	private final String color;

	public Box(String title, String text, String color) {
		this.title = title;
		this.text = text;
		this.color = color;
	}
	
	public Box( String text, String color) {
		this.title = null;
		this.text = text;
		this.color = color;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	public String getColor() {
		return color;
	}
	
}
