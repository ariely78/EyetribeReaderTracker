import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CalibrationPane extends JPanel{
	
	private int x,y;
	CalibrationPane()
	{
        this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
	}
	
	public void newPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		repaint();
	}
	
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.setColor(Color.GREEN);
      g.fillOval(x, y, 10,10);
    } 
}
