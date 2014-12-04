import javax.swing.*;

import java.awt.FontMetrics;

import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
    JTextArea jta = new JTextArea(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          g.getColor();
    	  g.setColor(Color.RED);
          g.fillOval(x, y, 10,10);
          fm = g.getFontMetrics(font);
      
	      Graphics2D g2 = (Graphics2D) g;
//	      Rectangle bounds = getStringBounds(g2, word, x, y);
	      try{
	    	  Rectangle rectangle = jta.modelToView( jta.getCaretPosition() );
//		      Rectangle bounds = getStringBounds(g2, word, rectangle.x, rectangle.y);
	          //g.fillOval(rectangle.x, rectangle.y-50,10,10);

//	          g2.setColor(Color.blue);
//	          g2.draw(rectangle);
	      }
	      catch (BadLocationException e1) {
	            e1.printStackTrace();
	        }


          g2.dispose();
        } 
    };

    public int wordReadingTime = 0;
    public int fontSize = 40;
    public String fileName;
    Font font;
    SettingsPanel settingsPanel;
    
    public MouseWordSelection(String text, SettingsPanel settingsPanel){
    	this.settingsPanel = settingsPanel;
    	font = new Font("Courier New", Font.PLAIN, fontSize);
    	jta.setFont(font);
    	jta.setEditable(false);
    	jta.setHighlighter(null);

    	//... Set textarea's initial text, scrolling, and border.
        jta.setText(text);
        JScrollPane scrollingArea = new JScrollPane(jta);

        //... Get the content pane, set layout, add to center
        this.setLayout(new BorderLayout());
        this.add(scrollingArea, BorderLayout.CENTER);
        
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

        		if(wordChanger.isCharALetter(jta, caretPosition))
	        	{
	        		System.out.println("character returned2" +ch);
		            word = wordChanger.getWord(caretPosition, jta);
		            
		            wordChanger.ChangeWords(caretPosition,jta);
	
		            System.out.println("Word: "+word+" Letter:"+jta.getText(caretPosition,1));
		            
		            DocumentReader.writeToTextFile(fileName, word+"  "
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

