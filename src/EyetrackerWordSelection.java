import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Random;

import com.theeyetribe.client.ICalibrationResultListener;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.ICalibrationProcessHandler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * Gets word from right clicked area
 */
public class EyetrackerWordSelection extends JPanel {
    private int x;
    private int y;
    int textareaX = 0;
    int textareaY = 0;
    int wordReadingTime = 0;
    int fontSize = 50;
    public String fileName;
    JTextArea txtContent = new JTextArea(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          g.getColor();
    	  g.setColor(Color.RED);
          g.fillOval(x, y, 10,10);
        } 
    };
    
    private Point last;
	private long lastTimeStamp = 0;
	boolean wordChanged = false;
	WordManipulation wordChanger = new WordManipulation();

    public EyetrackerWordSelection(String text){
    	setLast(new Point(0,0));
    	Font font = new Font("Verdana", Font.BOLD, fontSize);
    	txtContent.setFont(font);
    	txtContent.setEditable(false);
    	txtContent.setHighlighter(null);

    	//... Set textarea's initial text, scrolling, and border.
    	txtContent.setText(text);
        JScrollPane scrollingArea = new JScrollPane(txtContent);

        //... Get the content pane, set layout, add to center
        this.setLayout(new BorderLayout());
        this.add(scrollingArea, BorderLayout.CENTER);

    }
    
	public void startEyetracker() {
        final GazeManager gm = GazeManager.getInstance();
        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
        final GazeListener gazeListener = new GazeListener();
        gm.addGazeListener(gazeListener);
        CalibrationResult result = gm.getLastCalibrationResult();
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

	private class GazeListener implements IGazeListener
    {
        public void onGazeUpdate(GazeData gazeData)
        {   
            x = (int)gazeData.smoothedCoordinates.x-textareaX;
            y = (int)gazeData.smoothedCoordinates.y-textareaY;
            
       	 	repaint(x,y,10,10);
       	 	
            if((gazeData.timeStamp - lastTimeStamp) > wordReadingTime 
            		&& gazeData.state != gazeData.STATE_TRACKING_FAIL
            		&& gazeData.state != gazeData.STATE_TRACKING_LOST){
            	lastTimeStamp = gazeData.timeStamp;
                this.setCaretPoint(gazeData);
            }
        }
        
        public void setCaretPoint(GazeData gazeData)
        {
        	Point pt = new Point((int)gazeData.smoothedCoordinates.x-textareaX, 
        						(int)gazeData.smoothedCoordinates.y-textareaY);
        	try{
                int caretPosition = txtContent.viewToModel(pt);
        		char ch = txtContent.getText(caretPosition,1).charAt(0);

	        	if (Character.isLetter(ch)) 
	        	{
	            	txtContent.setCaretPosition(txtContent.viewToModel(pt));
	            	String word = wordChanger.getWord(caretPosition, txtContent);
		            wordChanger.ChangeWords(caretPosition,txtContent);
	                DocumentReader.writeToTextFile(fileName+".txt", 
	                				word+" , "+ txtContent.getText(caretPosition,1) + 
	                				" , " + caretPosition +
	                				" , " + gazeData.timeStamp + 
	                				" , " + gazeData.smoothedCoordinates.x +
	                				" , " + gazeData.smoothedCoordinates.y);
	        	}
            }catch(Exception ex){
            	ex.printStackTrace();
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
