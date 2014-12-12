import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.theeyetribe.client.GazeManager;


public class ParentPanel extends JPanel{

	public JFrame mf;
	final EyetrackerWordSelection eyetrackerPanel;
    final MouseWordSelection mousePanel;
    CalibrationPanel calibrationPane;
    ProcessEyeTracker calibrationProcess;
	private int width;
	private int height;
	
	public ParentPanel(int width, int height, JFrame f, EyetrackerWordSelection eyetrackerPanel, MouseWordSelection mousePanel){
		mf = f;
		setLayout(new CardLayout());
		setPreferredSize(new Dimension(width,height));
		this.width = width;
		this.height = height;
		this.eyetrackerPanel = eyetrackerPanel;
		this.mousePanel = mousePanel;
	}

	public void addComponentToPane(Container pane){
		Dimension dim = new Dimension(width,height);
		calibrationPane = new CalibrationPanel(this,dim);

		add(this.eyetrackerPanel,"Eyetracker");
		add(this.calibrationPane,"Calibrate");
		add(this.mousePanel,"MouseTracker");

		pane.add(this, BorderLayout.CENTER);
	}
	
	public void init_calibration_process(boolean mirror){
		calibrationProcess = new ProcessEyeTracker(9,this.calibrationPane);
		calibrationProcess.StartCalibration();
	}
	
	public void stop_calibration(String msg){
    	JOptionPane.showMessageDialog(this, "CalibrationResult:"+calibrationProcess.result+ " " + msg);
    	startEyetracker();

 	   //perfect calibration
    	if (calibrationProcess.result < 0.5 || calibrationProcess.result < 0.7)
    	{
//        	startEyetracker();
    	} else { 	   //bad calibration, do again
//    		calibrationProcess.StartCalibration();
    	}
	}
	
	public void calibrateAfterTest(int testNumber)
	{
		if(testNumber > 4) {
	    	JOptionPane.showMessageDialog(this, "Test Over Thankyou:");
	    	System.exit(1);
		} else {
	    	JOptionPane.showMessageDialog(this, "You will need to recalibrate, please look at the white dot as it appears");
			CardLayout cl = (CardLayout) (this.getLayout());
			cl.show(this, "Calibrate");
			init_calibration_process(false);
		}
	}
	
	public void startEyetracker()
	{
		CardLayout cl = (CardLayout) (this.getLayout());
		cl.show(this, "Eyetracker");
		this.eyetrackerPanel.startEyetracker();
	}
	
	public void showNameInputBox(String textReadingWindow)
    {
    	//Execute when button is pressed
        String reply = JOptionPane.showInputDialog(this, "Please enter your name",
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

        	JOptionPane.showMessageDialog(this, "When you press OK the calibration will start, stare at the dot on the screen, keep your head still and only move your eyes");
            
            if(textReadingWindow.equalsIgnoreCase("mouse")){
            	mousePanel.startMouseListener();
            } else {
       		 	eyetrackerPanel.startEyetracker();
            }
        } else {
            if (Files.exists(path)) {
            	JOptionPane.showMessageDialog(this, "File exists with this name try again");
            }
        }
    }
	
}
