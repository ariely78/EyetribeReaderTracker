import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;

import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.GazeData;

/**
 * Gets word from right clicked area
 */
public class EyetrackerWordSelection extends JTextArea {
    private int x;
    private int y;
    int textareaX = 0;
    int textareaY = 0;
    int WordReadingTime = 0;
    int textSize = 50;
    public String fileName;
    JTextArea txtContent;
    private Point last;
	private long lastTimeStamp = 0;

    public EyetrackerWordSelection(String text){
    	setLast(new Point(0,0));
    	txtContent =  this;
    	this.setAlignmentX(CENTER_ALIGNMENT);
    	this.setEditable(false);
    	this.setText(text);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	this.setBounds(0,0, dim.width, dim.height);
    	Font font = new Font("Verdana", Font.BOLD, textSize);
    	this.setFont(font);

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
    	
    public void setCaretPoint(int caretPosition)
    {
        try {
            String word = WordManipulation.getWord(caretPosition, this);
            DocumentReader.writeToTextFile("output.txt", word+"  "
            				+ this.getText(caretPosition-1,1)
            				+ " " +System.currentTimeMillis());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
	
	private class GazeListener implements IGazeListener
    {
        public void onGazeUpdate(GazeData gazeData)
        {   
            x = (int)gazeData.smoothedCoordinates.x-textareaX;
            y = (int)gazeData.smoothedCoordinates.y-textareaY;
        
       	 	repaint(x,y,10,10);

            if((gazeData.timeStamp - lastTimeStamp) > WordReadingTime){
            	lastTimeStamp = gazeData.timeStamp;
                this.setCaretPoint(gazeData);
            }
        }
        
        public void setCaretPoint(GazeData gazeData)
        {
        	Point pt = new Point((int)gazeData.smoothedCoordinates.x-textareaX, 
        						(int)gazeData.smoothedCoordinates.y-textareaY);
        	txtContent.setCaretPosition(viewToModel(pt));
            int caretPosition = getCaretPosition();
            try {
                String word = WordManipulation.getWord(caretPosition, txtContent);
                DocumentReader.writeToTextFile(fileName+".txt", word+"  "
                				+ txtContent.getText(caretPosition-1,1)
                				+ " " +gazeData.timeStampString);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.getColor();
      g.setColor(Color.RED);
      g.fillOval(x, y, 10,10);
    }

	public Point getLast() {
		return last;
	}

	public void setLast(Point last) {
		this.last = last;
	} 
}
