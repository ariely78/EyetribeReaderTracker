import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
    public String fileName;
    FontMetrics metric;
    Font font;
    JTextArea txtContent = new JTextArea(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          metric = this.getFontMetrics(font);
          g.getColor();
    	  g.setColor(Color.RED);
          g.fillOval(x, y, 10,10);
        } 
    };
    
    private Point last;
	private long lastTimeStamp = 0;
	boolean wordChanged = false;
	WordManipulation wordChanger = new WordManipulation();
	final SettingsPanel settingsPanel;
    private int testNumber;
    private String text;

    public EyetrackerWordSelection(final String text, final SettingsPanel settingsPanel){
    	this.settingsPanel = settingsPanel;
    	this.text = text;
    	testNumber = 1;
    	this.last = new Point(0,0);
    	font = new Font("Courier New", Font.PLAIN, fontSize);

    	txtContent.setFont(font);
    	txtContent.setEditable(false);
    	txtContent.setHighlighter(null);

    	//... Set textarea's initial text, scrolling, and border.
    	txtContent.setText(text);
//        JScrollPane scrollingArea = new JScrollPane(txtContent);

        //... Get the content pane, set layout, add to center
        this.setLayout(new BorderLayout());
        this.add(txtContent, BorderLayout.CENTER);
        
        txtContent.addKeyListener(new KeyListener(){ 

            public void keyPressed(KeyEvent ke){ 

                 if(ke.getKeyCode()==KeyEvent.VK_SPACE){
                	 wordChanged = false;
                	 testNumber += 1;
                	 settingsPanel.testWindow.parentPanel.calibrateAfterTest(testNumber);
                	 txtContent.setText(DocumentReader.readTextFile("text"+(testNumber)+".txt"));
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
		
//		if (GazeManager.getInstance().isActivated()){
//			GazeManager.getInstance().deactivate();
//		}
//		
//		GazeManager.getInstance().activate(ApiVersion.VERSION_1_0, ClientMode.PUSH, "localhost", 6555);
//		
//		final GazeListener gazeListener = new GazeListener();
//		GazeManager.getInstance().addGazeListener(gazeListener);
//		Runtime.getRuntime().addShutdownHook(new Thread()
//		{
//			@Override
//			public void run()
//			{
//				GazeManager.getInstance().removeGazeListener(gazeListener);
//				GazeManager.getInstance().deactivate();
//			}
//		});
		
		
//        final GazeManager gm = GazeManager.getInstance();
//        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
//        final GazeListener gazeListener = new GazeListener();
//        gm.addGazeListener(gazeListener);
//        CalibrationResult result = gm.getLastCalibrationResult();
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
//        		char ch = txtContent.getText(caretPosition,1).charAt(0);

        		caretPosition = wordChanger.letterTracked(txtContent, caretPosition, new Point(x,y),metric.getHeight());
	        	char ch = wordChanger.charAtPosition(txtContent, caretPosition);
//        		System.out.println("character returned" +ch);

        		if(wordChanger.isCharALetter(txtContent, caretPosition))
	        	//if (Character.isLetter(ch)) 
	        	{
//	            	txtContent.setCaretPosition(txtContent.viewToModel(pt));
	            	String word = wordChanger.getWord(caretPosition, txtContent);
		            wordChanger.ChangeWords(caretPosition,txtContent);
	                DocumentReader.writeToTextFile(fileName, 
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
	

//    
//    @Override
//    protected void paintComponent(Graphics g)
//    {
//      super.paintComponent(g);
//      g.getColor();
//      g.setColor(Color.RED);
//      g.fillOval(x, y, 10,10);
//      
//
//    }
//
//	public Point getLast() {
//		return last;
//	}
//
//	public void setLast(Point last) {
//		this.last = last;
//	} 
//	

}
