import javax.swing.*;

import java.awt.FontMetrics;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.theeyetribe.client.GazeManager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Gets word from right clicked area
 */
public class MouseWordSelection extends JPanel{
    private int x;
    private int y;
	private long lastTimeStamp = 0;
    private Point last = new Point(0,0);
	WordManipulation wordChanger = new WordManipulation();
	FontMetrics fm;
	Rectangle bounds;
	String word = "";
    JTextPane jta = new JTextPane(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          fm = this.getFontMetrics(font);
          g.getColor();
    	  g.setColor(Color.RED);
          g.fillOval(x, y, 10,10);
        } 
    };

    public int wordReadingTime = 0;
    public int fontSize = 40;
    Font font;
    final SettingsPanel settingsPanel;
    private int testNumber;

    public MouseWordSelection(final SettingsPanel settingsPanel){
    	this.settingsPanel = settingsPanel;
    	testNumber = 1;
        jta.requestFocus();
        MutableAttributeSet set = new SimpleAttributeSet(jta.getParagraphAttributes());
        StyleConstants.setLineSpacing(set, Settings.lineSpacing);
        jta.setParagraphAttributes(set, false);
        
        jta.addKeyListener(new KeyListener(){ 
            public void keyPressed(KeyEvent ke){ 

                 if(ke.getKeyCode()==KeyEvent.VK_SPACE){
                	 testNumber += 1;
                	 if(DocumentReader.doesFilePathExist("text"+(testNumber)+".txt"))
                	 {
	                	 DocumentReader.writeToTextFile(Settings.fileName, 
	                			 "\nNEXT TEST:" + Settings.fileName +"\n" );
                    	 wordChanger.wordChanged = false;
                    	 CardLayout cl = (CardLayout) (settingsPanel.testWindow.parentPanel.getLayout());
             			 cl.show(settingsPanel.testWindow.parentPanel, "MouseTracker");
	                	 setTextAreaText();
                	 } else {
                    	 settingsPanel.testWindow.parentPanel.calibrateAfterTest(false);
                	 }
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
    	font = new Font("Courier New", Font.PLAIN, fontSize);
    	jta.setFont(font);
    	jta.setEditable(false);
    	jta.setHighlighter(null);
    	//... Set textarea's initial text, scrolling, and border.
    	jta.setText(DocumentReader.readTextFile("text"+(testNumber)+".txt"));
//        JScrollPane scrollingArea = new JScrollPane(jta);
        //... Get the content pane, set layout, add to center
        this.setLayout(new BorderLayout());
        this.add(jta, BorderLayout.CENTER);
        jta.repaint();
         jta.requestFocus();

    }
	
    public void startMouseListener(){
    	jta.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent m) {
                int dx = m.getX() - last.x;
                int dy = m.getY() - last.y;
                x += dx;
                y += dy;
                last = m.getPoint();
                setCaretPoint(jta.viewToModel(m.getPoint()));
                jta.repaint();

            }
    	});
    	jta.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent m) {
                last = m.getPoint();
                x = last.x;
                y = last.y;

                jta.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            	setCaretPoint(jta.viewToModel(e.getPoint()));
            }
        });
    }
    
    public void setCaretPoint(int caretPosition)
    {
    	
    	//Give reader a chance to move onto next word
        if((System.currentTimeMillis() - lastTimeStamp) > wordReadingTime){
        	lastTimeStamp = System.currentTimeMillis();
        	
	        try {

	        	caretPosition = wordChanger.letterTracked(jta, caretPosition, new Point(x,y),fm.getHeight());
	        	char ch = wordChanger.charAtPosition(jta, caretPosition);
        		System.out.println("character returned" +ch);

//        		if(wordChanger.isCharALetter(jta, caretPosition))
	        	{
	        		System.out.println("character returned2" +ch);
		            word = wordChanger.getWord(caretPosition, jta);
		            
		            wordChanger.ChangeWords(caretPosition,jta);
	
		            System.out.println("Word: "+word+" Letter:"+jta.getText(caretPosition,1));
		            
		            DocumentReader.writeToTextFile(Settings.fileName, word+"  "
		            				+ ch
		            				+ " " +System.currentTimeMillis());
	        	}
	        } catch (BadLocationException e1) {
	            e1.printStackTrace();
	        }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
    } 
}

