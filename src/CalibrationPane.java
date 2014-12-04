import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CalibrationPane extends JPanel{
	
	private int x,y;
	SettingsPanel controlPanel;
	
	CalibrationPane(SettingsPanel controlPanel)
	{
		this.controlPanel = controlPanel;
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setBounds(0,0, dim.width, dim.height);
        this.setLayout(new BorderLayout());
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
    	try{Thread.sleep(5000);}catch (Exception e){};
    	int result = JOptionPane.showConfirmDialog(null, "Start Test","Click ok to start",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
    	if (result == JOptionPane.OK_OPTION)
    	{
    		this.setVisible(false);
    		controlPanel.mousetxtContent.setVisible(true);
    		controlPanel.mousetxtContent.startMouseListener();
    	}
    }
	
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.setColor(Color.GREEN);
      g.fillOval(x, y, 10,10);
    } 
}
