import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import javax.swing.JFrame;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
//    	JFrame frame = new JFrame();
//    	//more initialization code here
//    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//    	frame.setSize(300,700);
//    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    	frame.setVisible(true);
    	
    	
        final WordSelection txtContent = new WordSelection(DocumentReader.readTextFile("text.txt"));
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize(dim.width, dim.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(txtContent);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    	
    }

}
