import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.sun.javafx.geom.Point2D;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.IGazeListener;

/**
 * Gets word from right clicked area
 */
public class WordSelection extends JTextArea {
    private int x;
    private int y;
    JTextArea txtContent;
    
    public WordSelection(String text){
	    this.setEditable(false);
	    this.setText(text);
    	txtContent = this;
    	Font font = new Font("Verdana", Font.BOLD, 20);
    	this.setFont(font);
    	this.setBounds(100, 0,300,500);
    	this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	txtContent.setCaretPosition(txtContent.viewToModel(e.getPoint()));
                int caretPosition = txtContent.getCaretPosition();
                try {
                    String word = getWord(caretPosition, txtContent);
//                    JOptionPane.showMessageDialog(null, "Word: "+word + " Letter:" +txtContent.getText(caretPosition-1,1));
                    DocumentReader.writeToTextFile("output.txt", word+"  "+ txtContent.getText(caretPosition-1,1));
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
//        final GazeManager gm = GazeManager.getInstance();
//        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
//        
//        final GazeListener gazeListener = new GazeListener();
//        gm.addGazeListener(gazeListener);
//        
//        //TODO: Do awesome gaze control wizardry
//        
//        Runtime.getRuntime().addShutdownHook(new Thread()
//        {
//            @Override
//            public void run()
//            {
//                gm.removeGazeListener(gazeListener);
//                gm.deactivate();
//            }
//        });

    }
    
	public void createPanel(String text) {
	    this.setLayout(new GridBagLayout());

	    for (int i = 0; i < 1; i++) {
	      this.setEditable(false);

	      this.setText(text);

	      GridBagConstraints constraints = new GridBagConstraints();
	      constraints.gridx = 0;
	      constraints.gridy = i;
	      constraints.fill = GridBagConstraints.VERTICAL;
	      constraints.weightx = 1.0;
	      constraints.insets.bottom = 5;

	      this.add(this, constraints);
	    }
	}
	  
    @SuppressWarnings("unused")
	private class GazeListener implements IGazeListener
    {

        public void onGazeUpdate(GazeData gazeData)
        {   
            System.out.println(gazeData.smoothedCoordinates.x);
            System.out.println(gazeData.smoothedCoordinates.y);

            x = (int)gazeData.smoothedCoordinates.x;
            y = (int)gazeData.smoothedCoordinates.y;
            
//            this.setCaretPoint(gazeData.smoothedCoordinates);
        }
        
        public void setCaretPoint(Point2D pointGaze)
        {
        	Point pt = new Point((int)pointGaze.x, (int)pointGaze.y);
        	txtContent.setCaretPosition(txtContent.viewToModel(pt));
            int caretPosition = txtContent.getCaretPosition();
            try {
                String word = getWord(caretPosition, txtContent);
                DocumentReader.writeToTextFile("output.txt", word+"  "+ txtContent.getText(caretPosition-1,1));
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
    
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawOval(x, y, 5, 5);
//    }
    
    private static String getWord(int caretPosition, JTextArea txtContent) throws BadLocationException {
        int startIndex;
        int endIndex;
        int i = 0;
        while (!txtContent.getText(caretPosition + i, 1).equals(" ")
                && !txtContent.getText(caretPosition + i, 1).equals("\n")) {
            i++;
        }
        endIndex = caretPosition + i;
        int j = 0;
        while (j < caretPosition && !txtContent.getText(caretPosition - j - 1, 1).equals(" ")) {
            j++;
        }
        startIndex = caretPosition - j;
        return txtContent.getText(startIndex, endIndex - startIndex);
    }
}
