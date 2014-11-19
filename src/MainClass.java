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
        JFrame frame = new JFrame();
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setSize(dim.width, dim.height);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(txtContent.txtContent);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    	
    }

}
