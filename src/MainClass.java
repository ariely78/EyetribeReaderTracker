
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
    	JFrame frame = new JFrame();
    	//more initialization code here
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(dim.width, dim.height);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	DragCircle d = new DragCircle();
    	frame.add(d);
    	
        BufferedReader br = new BufferedReader(new FileReader("text.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.console().readLine());
                line = br.readLine();
            }
            String everything = sb.toString();
        	DrawText dt = new DrawText(everything,100,300);
        	dt.repaint();
        } finally {

            br.close();
        }
    	
    }



}
