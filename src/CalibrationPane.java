import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CalibrationPane extends JPanel{
	Calibration calibrationController = new Calibration(this);
	private int x,y;
	MainEyeTrackerFrame mainFrame;
	
	CalibrationPane(MainEyeTrackerFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setPreferredSize(dim);
//        this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
	}
	
	public void newPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		repaint();
	}
	
    public void afterCalibration()
    {
    	mainFrame.loadTestScreen();
    }
    
    public void startCalibration()
    {
    	calibrationController.StartCalibration();
    }
	
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.setColor(Color.GREEN);
      g.fillOval(x, y, 10,10);
    } 
}
