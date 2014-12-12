
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import com.theeyetribe.client.data.Point2D;

@SuppressWarnings("serial")
public class CalibrationPanel extends JPanel{
	
	public boolean p = false;
	//private Rectangle2D.Float ball; //figure to look at
	private Ellipse2D.Float ball; //figure to look at
	private Point2D pos = new Point2D(0,0); //position to put the figure
	ParentPanel pp;
	
	public CalibrationPanel(ParentPanel pp, Dimension dim){
	    this.setPreferredSize(dim);
	    this.setSize(dim);
		this.pp = pp;
		System.out.println("height:"+this.getHeight()+" width:"+this.getWidth());
		ball = new Ellipse2D.Float();
		ball.setFrame(0, 0, 50, 50);
		setBackground(Color.black);
		repaint();
	}
	
	private void doStep() {
		//logic
		ball.setFrame(pos.x, pos.y, ball.getWidth(), ball.getHeight());
	}
	
	private void drawSquare(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fill(ball);
		g2.draw(ball);
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);

		//Dimension size = getSize();
		
        doStep();
        drawSquare(g2);    
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	public void newPos(int x, int y){
		pos = new Point2D(x,y);
		repaint();
	}

	/*@Override
	public void actionPerformed(ActionEvent e) {
		
		repaint();
	}*/

	public void end_calibration(String msg){

		pp.stop_calibration(msg);
	}

}