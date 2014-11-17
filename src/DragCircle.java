import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;

import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.*;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;

class DragCircle extends JPanel {

    @SuppressWarnings("unused")
	private class GazeListener implements IGazeListener
    {
        public void onGazeUpdate(GazeData gazeData)
        {
            System.out.println(gazeData.smoothedCoordinates.x);

            x = (int)gazeData.smoothedCoordinates.x;
            y = (int)gazeData.smoothedCoordinates.y;

            repaint();
        }

    }
    
    private final class MouseDrag extends MouseAdapter {
        private boolean dragging = false;
        private Point last;

        @Override
        public void mousePressed(MouseEvent m) {
            last = m.getPoint();
            x = last.x;
            y = last.y;
            JTextArea editor = (JTextArea) m.getSource();
            Point pt = new Point(m.getX(), m.getY());
            int pos = editor.viewToModel(pt);
            
//            int offs = editor.viewToModel(pt);
            
//            int ipos = editor.viewToModel(pt);
//            editor.setContentType( "text/html" );
//            PlainDocument pd=(PlainDocument)editor.getDocument();
////            PlainDocument pd = (PlainDocument)editor.getDocument();
//            Element elem = pd.getgetCharacterElement(ipos);
//            int ichar1 = elem.getStartOffset();
//            int ichar2 = elem.getEndOffset();
//            String line = null;
//			try {
//				line = editor.getText( ichar1, (ichar2-ichar1-1) );	
//				} 
//			catch (BadLocationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
            editor.setCaretPosition(editor.viewToModel(pt));
            int caretPosition = editor.getCaretPosition();
            try {
                String word = getWord(caretPosition, editor);
                JOptionPane.showMessageDialog(null, word);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            
//            System.out.println("Word "+line +" Number: "+ ipos);
//            int start = javax.swing.text.Utilities.getWordStart(editor, offs);
//            int end = javax.swing.text.Utilities.getWordEnd(editor, offs);
//            String word = getText(start, end-start+1);
            
            repaint();
        }
        
        private String getWord(int caretPosition, JTextArea txtContent) throws BadLocationException {
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
        public void mouseDragged(MouseEvent m) {
            int dx = m.getX() - last.x;
            int dy = m.getY() - last.y;
            x += dx;
            y += dy;
            last = m.getPoint();
            repaint();
        }
    }


    
    private int x;
    private int y;

    private MouseDrag mouseDrag;
    JTextArea editorPane;
    public DragCircle() {
    	editorPane = new JTextArea();

        setBackground(Color.WHITE);
        mouseDrag = new MouseDrag();
        editorPane.addMouseListener(mouseDrag);
        editorPane.addMouseMotionListener(mouseDrag);
        
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
		      editorPane.setEditable(false);

		      editorPane.setText(text);

		      GridBagConstraints constraints = new GridBagConstraints();
		      constraints.gridx = 0;
		      constraints.gridy = i;
		      constraints.fill = GridBagConstraints.VERTICAL;
		      constraints.weightx = 1.0;
		      constraints.insets.bottom = 5;

		      this.add(editorPane, constraints);
		    }

//		    return panel;
	}
	  
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, 5, 5);
    }
    

}