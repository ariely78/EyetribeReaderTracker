
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class MainClass {
    public static void main(String[] args)
    {
    	JFrame frame = new JFrame();
    	//more initialization code here
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(dim.width, dim.height);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	DragCircle d = new DragCircle();
    	frame.add(d);
    }



}
