import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    final MouseWordSelection mousePanel;
    final EyetrackerWordSelection eyetrackerPanel;
    final GraphicsLogic graphicsLogicPane = new GraphicsLogic(this, Toolkit.getDefaultToolkit().getScreenSize());
    //final JPanel parentPanel = new JPanel();
	Process calibrationProcess = new Process(9, graphicsLogicPane);

    public MainFrame(final MouseWordSelection mousePanel, 
    		final EyetrackerWordSelection eyetrackerPanel) {
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setPreferredSize(dim);

	    this.mousePanel = mousePanel;
    	this.eyetrackerPanel = eyetrackerPanel;
//    	this.setContentPane(parentPanel);
//        parentPanel.setLayout(new BorderLayout(10, 10));

    }
    
	public void showNameInputBox(String textReadingWindow)
    {
    	//Execute when button is pressed
        String reply = JOptionPane.showInputDialog(null, "Please enter your name",
        		"Press ok to start test",
        		JOptionPane.OK_OPTION);
        this.mousePanel.wordChanger.swapWord = reply;
        this.eyetrackerPanel.wordChanger.swapWord = reply;

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        Date date = new Date();
        Path path = Paths.get(reply+"_"+dateFormat.format(date)+".txt");

        if (Files.notExists(path) && !reply.isEmpty()) {
          // file is not exist
        	this.mousePanel.fileName = path.toString();
        	this.eyetrackerPanel.fileName = path.toString();

        	JOptionPane.showMessageDialog(null, "When you press OK the calibration will start, stare at the dot on the screen, keep your head still and only move your eyes");
//            add(graphicsLogicPane, BorderLayout.CENTER);
            setTitle("Copyright Ben Smith (c) 2014");
            
            if(textReadingWindow.equalsIgnoreCase("mouse")){
            	add(mousePanel);
            	mousePanel.startMouseListener();
            } else {
//            	//set fullscreen
//            	GraphicsDevice gd =
//        	            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        		setUndecorated(true);
//        		gd.setFullScreenWindow(this);
        		
        		//add our calibration graphics window
            	add(graphicsLogicPane);
            	//create and start a calibration process
        		calibrationProcess = new Process(9, graphicsLogicPane);
        		calibrationProcess.StartCalibration();
            }
            pack();
            setVisible(true);
        } else {
            if (Files.exists(path)) {
            	JOptionPane.showMessageDialog(null, "File exists with this name try again");
            }
        }
    }
    
   public void loadTestScreen()
   {
	   //perfect calibration
	if (calibrationProcess.result < 0.5 || calibrationProcess.result < 0.7)
	{
		 remove(graphicsLogicPane);
		 add(eyetrackerPanel);//, BorderLayout.CENTER);
		 revalidate();
		 repaint();
//		 parentPanel.add(mousePanel);
//	     mousePanel.startMouseListener();
	     eyetrackerPanel.startEyetracker();
	} else { 	   //bad calibration, do again

		calibrationProcess.StartCalibration();

	}
   }
}
