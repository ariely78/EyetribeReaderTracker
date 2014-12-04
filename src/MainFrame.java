import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    final MouseWordSelection mousePanel;
    final EyetrackerWordSelection eyetrackerPanel;
    final CalibrationPane calibrationPane = new CalibrationPane(this);
    final JPanel parentPanel = new JPanel();

    public MainFrame(final MouseWordSelection mousePanel, 
    		final EyetrackerWordSelection eyetrackerPanel) {

    	this.mousePanel = mousePanel;
    	this.eyetrackerPanel = eyetrackerPanel;
    	
        parentPanel.setLayout(new BorderLayout(10, 10));

//        final JPanel childPanel1 = new JPanel();
//        childPanel1.setBackground(Color.red);
//        childPanel1.setPreferredSize(new Dimension(300, 40));
//
//        final JPanel childPanel2 = new JPanel();
//        childPanel2.setBackground(Color.blue);
//        childPanel2.setPreferredSize(new Dimension(800, 600));
//        parentPanel.add(calibrationPanel, BorderLayout.CENTER);
//        calibrationPanel.setPreferredSize(new Dimension(800, 600));
//        
////        JButton myButton = new JButton("Add Component ");
//        myButton.addActionListener(new ActionListener() {
//
//            @Override
//			public void actionPerformed(ActionEvent e) {
//                parentPanel.remove(calibrationPanel);
//                parentPanel.add(mousePanel, BorderLayout.CENTER);
//                parentPanel.revalidate();
//                parentPanel.repaint();
//                pack();
//            }
//        });

    }
    
    public void showNameInputBox()
    {
    	//Execute when button is pressed
        String reply = JOptionPane.showInputDialog(null, "Please enter your name",
        		"Press ok to start test",
        		JOptionPane.OK_OPTION);
        this.mousePanel.wordChanger.swapWord = reply;
        this.eyetrackerPanel.wordChanger.swapWord = reply;

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        Date date = new Date();
        Path path = Paths.get(reply+"_"+dateFormat.format(date)+".txt");

        if (Files.notExists(path) && !reply.isEmpty()) {
          // file is not exist
        	this.mousePanel.fileName = path.toString();
        	this.eyetrackerPanel.fileName = path.toString();

        	JOptionPane.showMessageDialog(null, "When you press OK the calibration will start, stare at the dot on the screen, keep your head still and only move your eyes");
            parentPanel.add(calibrationPane, BorderLayout.CENTER);
            setTitle("Copyright Ben Smith (c) 2014");
            add(parentPanel);
            pack();
            setVisible(true);
            calibrationPane.startCalibration();
            //... Set window characteristics.
        	//testWindow.add(cali);
//        	testWindow.setContentPane(mousetxtContent);
//        	mousetxtContent.setVisible(false);
//        	testWindow.setTitle("Copyright Ben Smith (c) 2014");
//        	testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        	testWindow.pack();
//        	testWindow.setVisible(true);
        	//cali.afterCalibration();

        } else {
            if (Files.exists(path)) {
            	JOptionPane.showMessageDialog(null, "File exists with this name try again");
            }
        }
    }
    
    
   public void loadTestScreen()
   {
     parentPanel.remove(calibrationPane);
     parentPanel.add(eyetrackerPanel, BorderLayout.CENTER);
     parentPanel.revalidate();
     parentPanel.repaint();
     pack();
   }
}
