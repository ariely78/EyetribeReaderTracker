import java.awt.Dimension;
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
//    	final MouseWordSelection mousetxtContent = new MouseWordSelection(DocumentReader.readTextFile("text.txt"));
    	final EyetrackerWordSelection EyetrackerTxtContent = new EyetrackerWordSelection(DocumentReader.readTextFile("text.txt"));

        final JFrame frame = new JFrame();
        final JButton startButton = new JButton("Start Eyetracker Test");
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width, dim.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //Add action listener to button
        startButton.addActionListener(new ActionListener() {
        
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                String reply = JOptionPane.showInputDialog(null, "Please test name",
                		"Press ok to start test",
                		JOptionPane.OK_OPTION);
                
                Path path = Paths.get(reply+".txt");

                if (Files.notExists(path) && !reply.isEmpty()) {
                  // file is not exist
                	frame.remove(startButton);                	
//                	mousetxtContent.fileName = reply;
//                    frame.add(mousetxtContent);
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
