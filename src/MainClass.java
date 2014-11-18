
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        txtContent.setSize(300,400);
        txtContent.setEditable(false);
        JFrame frame = new JFrame();
        frame.setSize(dim.width, dim.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(txtContent);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    	
    }

}
