import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class MainEyeTrackerFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    final MouseWordSelection mousePanel;
    final EyetrackerWordSelection eyetrackerPanel;
    final ParentPanel parentPanel;
	ProcessEyeTracker calibrationProcess;

    public MainEyeTrackerFrame(final MouseWordSelection mousePanel, final EyetrackerWordSelection eyetrackerPanel) {
    	
		this.mousePanel = mousePanel;
		this.eyetrackerPanel = eyetrackerPanel;
        setTitle("Copyright Ben Smith (c) 2014");

    	setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setLocation(0, 0);
		System.out.println("MainFrame: (" + getWidth() + "," + getHeight()+")");
		
		parentPanel = new ParentPanel(getWidth(), getHeight(), this, eyetrackerPanel, mousePanel);
		parentPanel.addComponentToPane(getContentPane());
//		GraphicsDevice gd =
//	            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		setUndecorated(true);
//		gd.setFullScreenWindow(this);
		//Display the window.
	

    }
   
}
