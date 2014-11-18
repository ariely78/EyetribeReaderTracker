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
import com.sun.jmx.snmp.Timestamp;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.IGazeListener;

/**
 * Gets word from right clicked area
 */
public class WordSelection extends JPanel {
    private int x;
    private int y;
    int textareaX = 0;
    int textareaY = 0;

    JTextArea txtContent;
    private Point last;
	private long lastTimeStamp = 0;

    public WordSelection(String text){
    	last = new Point(0,0);
    	txtContent =  new JTextArea()
    	  {
            @Override
            protected void paintComponent(Graphics g)
            {
              super.paintComponent(g);
              g.setColor(g.getColor().RED);
              g.fillOval(x, y, 25, 25);
//              g.drawLine(last.x, last.y, x, y);
            }   
      };
    	txtContent.setEditable(false);
    	txtContent.setText(text);
    	Font font = new Font("Verdana", Font.BOLD, 30);
    	txtContent.setFont(font);
    	txtContent.setBounds(textareaX, 0, 300, 500);
    	
    	txtContent.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent m) {
                int dx = m.getX() - last.x;
                int dy = m.getY() - last.y;
                x += dx;
                y += dy;
                last = m.getPoint();
            	txtContent.setCaretPosition(txtContent.viewToModel(m.getPoint()));
	            int caretPosition = txtContent.getCaretPosition();
	            try {
	                String word = getWord(caretPosition, txtContent);
	                if((System.currentTimeMillis() - lastTimeStamp) > 500){
	                	lastTimeStamp = System.currentTimeMillis();
//	                	JOptionPane.showMessageDialog(null, "Word: "+word + " Letter:" +txtContent.getText(caretPosition-1,1));
	                	DocumentReader.writeToTextFile("output.txt", word+"  "+ txtContent.getText(caretPosition-1,1));
	                }
	            } catch (BadLocationException e1) {
	                e1.printStackTrace();
	            }
	            txtContent.repaint(0,0,300,100);
            }
    	});
    	txtContent.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent m) {
                last = m.getPoint();
                x = last.x;
                y = last.y;

                txtContent.repaint();
            }


            
            @Override
            public void mouseClicked(MouseEvent e) {
//            	txtContent.setCaretPosition(txtContent.viewToModel(e.getPoint()));
//                int caretPosition = txtContent.getCaretPosition();
//                try {
//                    String word = getWord(caretPosition, txtContent);
//                    JOptionPane.showMessageDialog(null, "Word: "+word + " Letter:" +txtContent.getText(caretPosition-1,1));
//                    DocumentReader.writeToTextFile("output.txt", word+"  "+ txtContent.getText(caretPosition-1,1));
//                } catch (BadLocationException e1) {
//                    e1.printStackTrace();
//                }
            }
        });
    	this.add(txtContent);

        final GazeManager gm = GazeManager.getInstance();
        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
        
        final GazeListener gazeListener = new GazeListener();
        gm.addGazeListener(gazeListener);
        
        //TODO: Do awesome gaze control wizardry
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                gm.removeGazeListener(gazeListener);
                gm.deactivate();
            }
        });

    }
    
	public void createPanel(String text) {
	    this.setLayout(new GridBagLayout());

	    for (int i = 0; i < 1; i++) {
			txtContent.setEditable(false);
			txtContent.setText(text);
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.weightx = 1.0;
			constraints.insets.bottom = 5;
			this.add(txtContent, constraints);
	    }
	}
	

    @SuppressWarnings("unused")
	private class GazeListener implements IGazeListener
    {
        public void onGazeUpdate(GazeData gazeData)
        {   
            x = (int)gazeData.smoothedCoordinates.x-textareaX;
            y = (int)gazeData.smoothedCoordinates.y-textareaY;
        
            
            if((gazeData.timeStamp - lastTimeStamp) > 0){
            	 txtContent.repaint(x-25,y-25,50,50);
            	lastTimeStamp = gazeData.timeStamp;
//                this.setCaretPoint(gazeData);
            }
        }
        
        public void setCaretPoint(GazeData gazeData)
        {
        	Point pt = new Point((int)gazeData.smoothedCoordinates.x-textareaX, 
        						(int)gazeData.smoothedCoordinates.y-textareaY);
        	txtContent.setCaretPosition(txtContent.viewToModel(pt));
            int caretPosition = txtContent.getCaretPosition();
            try {
                String word = getWord(caretPosition, txtContent);
                DocumentReader.writeToTextFile("output.txt", word+"  "
                				+ txtContent.getText(caretPosition-1,1)
                				+ " " +gazeData.timeStampString);
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
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, 5, 5);
    }
}
