import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    final MouseWordSelection mousePanel;
    final EyetrackerWordSelection eyetrackerPanel;
    final CalibrationPane calibrationPanel;
    
    public MainFrame(final MouseWordSelection mousePanel, 
    		final EyetrackerWordSelection eyetrackerPanel, 
    		final CalibrationPane calibrationPanel) {

    	this.mousePanel = mousePanel;
    	this.eyetrackerPanel = eyetrackerPanel;
    	this.calibrationPanel = calibrationPanel;
    	
        final JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout(10, 10));

//        final JPanel childPanel1 = new JPanel();
//        childPanel1.setBackground(Color.red);
//        childPanel1.setPreferredSize(new Dimension(300, 40));
//
//        final JPanel childPanel2 = new JPanel();
//        childPanel2.setBackground(Color.blue);
//        childPanel2.setPreferredSize(new Dimension(800, 600));

        JButton myButton = new JButton("Add Component ");
        myButton.addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(ActionEvent e) {
                parentPanel.remove(calibrationPanel);
                parentPanel.add(mousePanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
                pack();
            }
        });
        setTitle("My Empty Frame");
        setLocation(10, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentPanel.add(calibrationPanel, BorderLayout.CENTER);
        parentPanel.add(myButton, BorderLayout.SOUTH);
        add(parentPanel);
        pack();
        setVisible(true);
    }
}
