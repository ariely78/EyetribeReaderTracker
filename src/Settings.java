import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Settings {

	public static boolean hideMessages = false;
	public static int calibrateTime = 500;
	public static int timeToMoveToGaze = 500;
	public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static String fileName;
    public static float lineSpacing = 1;
    public static float fontSize = 50;
    
	// Preference key name
	final static String FONT_SIZE = "fontSize";
	final static String Line_Spacing = "lineSpacing";

}
