import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.GazeData;

/**
 * Gets word from right clicked area
 */
public class EyetrackerWordSelection extends JPanel {
    private int x;
    private int y;
    int textareaX = 0;
    int textareaY = 0;
    int wordReadingTime = 0;
    int fontSize = 40;
    FontMetrics metric;
    Font font;
    JTextPane txtContent = new JTextPane(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          metric = this.getFontMetrics(font);
          if(testNumber <= 1)
          {
	          g.getColor();
	    	  g.setColor(Color.RED);
	          g.fillOval(x, y, 10,10);
          }
        } 
    };
    
    private Point last;
	private long lastTimeStamp = 0;
	WordManipulation wordChanger = new WordManipulation();
	final SettingsPanel settingsPanel;
    private int testNumber;

    public EyetrackerWordSelection(final SettingsPanel settingsPanel){
    	this.settingsPanel = settingsPanel;
    	testNumber = 1;
    	this.last = new Point(0,0);

        //... Get the content pane, set layout, add to center
        this.setLayout(new BorderLayout());
        this.add(txtContent, BorderLayout.CENTER);

        txtContent.requestFocus();
        txtContent.addKeyListener(new KeyListener(){ 

            public void keyPressed(KeyEvent ke){ 

                 if(ke.getKeyCode()==KeyEvent.VK_SPACE){
                	 GazeManager.getInstance().deactivate();
                	 testNumber += 1;
                	
                	 if(DocumentReader.doesFilePathExist("text"+(testNumber)+".txt"))
                	 {
                		 DocumentReader.writeToTextFile(Settings.fileName, 
                    			 "\nNEXT TEST :" + testNumber +"\n" );
                    	 wordChanger.wordChanged = false;
                    	 settingsPanel.testWindow.parentPanel.calibrateAfterTest(true);
                    	 setTextAreaText(); 
                	 } else {
                    	 settingsPanel.testWindow.parentPanel.calibrateAfterTest(false);
                	 }
                	 
                 }
                 if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){
                	 System.exit(1);
                 }
            }
 
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
    }
    
    public void setTextAreaText()
    {
        MutableAttributeSet set = new SimpleAttributeSet(txtContent.getParagraphAttributes());
        StyleConstants.setLineSpacing(set, Settings.lineSpacing);
        txtContent.setParagraphAttributes(set, false);
    	font = new Font("Courier New", Font.PLAIN, fontSize);
    	txtContent.setFont(font);
    	txtContent.setEditable(false);
    	txtContent.setHighlighter(null);
    	//... Set textarea's initial text, scrolling, and border.
    	txtContent.setText(DocumentReader.readTextFile("text"+(testNumber)+".txt"));
    }
    
	public void startEyetracker() {

		final GazeManager gm = GazeManager.getInstance();
//		to check if the connection is ok
		boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH, "localhost", 6555);
		System.out.println("success: " + success);
//		to create a listener so to catch any information from the
//		eye tracker
		final GazeListener gazeListener = new GazeListener();	
		gm.addGazeListener(gazeListener);
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
        @Override
		public void onGazeUpdate(GazeData gazeData)
        {   
            x = (int)gazeData.smoothedCoordinates.x-textareaX;
            y = (int)gazeData.smoothedCoordinates.y-textareaY;
            
       	 	repaint(x,y,10,10);
       	 	
            if((gazeData.timeStamp - lastTimeStamp) > wordReadingTime 
            		&& gazeData.state != GazeData.STATE_TRACKING_FAIL
            		&& gazeData.state != GazeData.STATE_TRACKING_LOST){
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

        		caretPosition = wordChanger.letterTracked(txtContent, caretPosition, new Point(x,y),metric.getHeight());
	        	char ch = wordChanger.charAtPosition(txtContent, caretPosition);

        		if(wordChanger.isCharALetter(txtContent, caretPosition))
	        	{
	            	String word = wordChanger.getWord(caretPosition, txtContent);
		            wordChanger.ChangeWords(caretPosition,txtContent);
	                DocumentReader.writeToTextFile(Settings.fileName, 
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
}