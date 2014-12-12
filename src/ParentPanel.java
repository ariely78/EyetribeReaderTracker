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


public class ParentPanel extends JPanel{

	public JFrame mf;
	final EyetrackerWordSelection eyetrackerPanel;
    final MouseWordSelection mousePanel;
    GraphicsLogicEyetracker graphicsLogicPane;
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
    	graphicsLogicPane = new GraphicsLogicEyetracker(this,dim);

		add(this.eyetrackerPanel,"Eyetracker");
		add(this.graphicsLogicPane,"Calibrate");
		add(this.mousePanel,"MouseTracker");

		pane.add(this, BorderLayout.CENTER);
	}
	
	public void init_calibration_process(boolean mirror){
		ProcessEyeTracker c = new ProcessEyeTracker(9,this.graphicsLogicPane);
		c.StartCalibration();
	}
	
	public void stop_calibration(String msg){
    	JOptionPane.showMessageDialog(null, "Calibration:" + msg);
		eyetrackerPanel.startEyetracker();

//		panel1.calibrate.setVisible(true);
//		panel1.exit.setVisible(true);
//		panel1.msg.setVisible(true);
//		panel1.msg.setText(msg);
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
            
            if(textReadingWindow.equalsIgnoreCase("mouse")){
            	mousePanel.startMouseListener();
            } else {
       		 	eyetrackerPanel.startEyetracker();
            }
        } else {
            if (Files.exists(path)) {
            	JOptionPane.showMessageDialog(null, "File exists with this name try again");
            }
        }
    }
	
   public void loadTestScreen()
   {
	   //perfect calibration
//	if (calibrsationProcess.result < 0.5 || calibrationProcess.result < 0.7)
//	{
//		
//	} else { 	   //bad calibration, do again
//
//	}
   }
}
