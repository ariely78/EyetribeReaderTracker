import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainClass {
    public static void main(String[] args) throws IOException
    {

        final JFrame frame = new JFrame();
        final JButton startButton = new JButton("Start Mouse");
        startButton.setSize(100, 50);
        startButton.setLocation(300, 100);

        final JButton startEyetrackerButton = new JButton("Start Eyetracker");
        startEyetrackerButton.setSize(100, 50);
        startEyetrackerButton.setLocation(100, 100);
        
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setTitle("Copyright Ben Smith (c) 2014");
        frame.setSize(dim.width, dim.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startEyetrackerButton);
        frame.add(startButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());       
        //Add action listener to button
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
            	final MouseWordSelection mousetxtContent = new MouseWordSelection(DocumentReader.readTextFile("text.txt"));
                //Execute when button is pressed
                String reply = JOptionPane.showInputDialog(null, "Please test name",
                		"Press ok to start test",
                		JOptionPane.OK_OPTION);
                
                Path path = Paths.get(reply+".txt");

                if (Files.notExists(path) && !reply.isEmpty()) {
                  // file is not exist
                	frame.remove(startButton);  
                	frame.remove(startEyetrackerButton);                	
                	frame.repaint();

                	mousetxtContent.fileName = reply;
                    frame.add(mousetxtContent);
                	frame.repaint();
                } else {
                    if (Files.exists(path)) {
                    	JOptionPane.showMessageDialog(null, "File exists with this name try again");
                    }
                }
            }
        });
        
        //Add action listener to button
        startEyetrackerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
            	final EyetrackerWordSelection EyetrackerTxtContent = new EyetrackerWordSelection(DocumentReader.readTextFile("text.txt"));
                //Execute when button is pressed
                String reply = JOptionPane.showInputDialog(null, "Please test name",
                		"Press ok to start test",
                		JOptionPane.OK_OPTION);
                
                Path path = Paths.get(reply+".txt");

                if (Files.notExists(path) && !reply.isEmpty()) {
                  // file is not exist
                	frame.remove(startButton);  
                	frame.remove(startEyetrackerButton); 
                	frame.repaint();

                	EyetrackerTxtContent.fileName = reply;
                    frame.add(EyetrackerTxtContent);
                	frame.repaint();
                } else {
                    if (Files.exists(path)) {
                    	JOptionPane.showMessageDialog(null, "File exists with this name try again");
                    }
                }
            }
        });
    }
}
