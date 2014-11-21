import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Gets word from right clicked area
 */
public class MouseWordSelection extends JTextArea {
    private int x;
    private int y;
    int textareaX = 0;
    int textareaY = 0;
    int WordReadingTime = 200;
    int textSize = 30;
    public String fileName;
    JTextArea txtContent;
    private Point last;
	private long lastTimeStamp = 0;
	boolean wordChanged = false;
	WordManipulation wordChanger = new WordManipulation();

    public MouseWordSelection(String text){
    	last = new Point(0,0);
    	txtContent =  this;
    	
    	this.setEditable(false);
    	this.setText(text);
    	this.setHighlighter(null);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	this.setBounds(0,0, dim.width, dim.height);
    	Font font = new Font("Verdana", Font.BOLD, textSize);
    	this.setFont(font);
    	
    	this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent m) {
                int dx = m.getX() - last.x;
                int dy = m.getY() - last.y;
                x += dx;
                y += dy;
                last = m.getPoint();
                setCaretPoint(viewToModel(m.getPoint()));
            }
    	});
    	this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent m) {
                last = m.getPoint();
                x = last.x;
                y = last.y;

                repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            	setCaretPoint(txtContent.viewToModel(e.getPoint()));
            }
        });
    }
	
    public void setCaretPoint(int caretPosition)
    {
        if((System.currentTimeMillis() - lastTimeStamp) > WordReadingTime){
        	lastTimeStamp = System.currentTimeMillis();
	        try {
	            String word = wordChanger.getWord(caretPosition, this);
	            System.out.println("currentword "+wordChanger.getWord(caretPosition, this));
	            System.out.println("wordToReplace "+wordChanger.wordToReplace);
	            System.out.println("wordToInsert "+wordChanger.wordToInsert);

	            //if we changed a word and the current word selected is same as the word we changed to, change it back!
	            if(wordChanged && wordChanger.getWord(caretPosition, this).equalsIgnoreCase(wordChanger.wordToInsert))
	            {
	            	wordChanger.changeWordXWordsInfront(wordChanger.wordToReplace, 0, caretPosition, txtContent);
	            	wordChanged = false;
	            }
	          //only change word if selected word is "to"
	        	if(wordChanger.getWord(caretPosition, this).equalsIgnoreCase("to") && !wordChanged)
	        		wordChanged = wordChanger.changeWordXWordsInfront("Ben", 2, caretPosition, txtContent);
	        	
	            DocumentReader.writeToTextFile(fileName+".txt", word+"  "
	            				+ this.getText(caretPosition-1,1)
	            				+ " " +System.currentTimeMillis());
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
}
