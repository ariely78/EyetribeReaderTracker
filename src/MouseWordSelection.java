import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
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
    private JTextArea jta = new JTextArea(){
        @Override
        protected void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          g.getColor();
    	  g.setColor(Color.RED);
          g.fillOval(x, y, 10,10);
        } 
    };

    public int wordReadingTime = 0;
    public int fontSize = 50;
    public String fileName;

	
    public MouseWordSelection(String text){
    	
    	Font font = new Font("Verdana", Font.BOLD, fontSize);
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
	            String word = wordChanger.getWord(caretPosition, jta);
	            wordChanger.ChangeWords(caretPosition,jta);
	            TextLineNumber tln = new TextLineNumber(jta);

	            System.out.println("Word: "+word+" Letter:"+jta.getText(caretPosition,1));
	            
	            DocumentReader.writeToTextFile(fileName+".txt", word+"  "
	            				+ jta.getText(caretPosition-1,1)
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
    } 
}

