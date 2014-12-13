import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Settings {

	public static boolean hideMessages = false;
	public static int calibrateTime = 500;
	public static int timeToMoveToGaze = 500;
	public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
}
