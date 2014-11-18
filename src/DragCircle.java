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


class TextPanel extends JPanel {


    
//    private final class MouseDrag extends MouseAdapter {
//        private boolean dragging = false;
//        private Point last;
//
//        @Override
//        public void mousePressed(MouseEvent m) {
//            last = m.getPoint();
//            x = last.x;
//            y = last.y;
//            JTextArea editor = (JTextArea) m.getSource();
//            Point pt = new Point(m.getX(), m.getY());
//            int pos = editor.viewToModel(pt);
//            repaint();
//        }
//
//        @Override
//        public void mouseDragged(MouseEvent m) {
//            int dx = m.getX() - last.x;
//            int dy = m.getY() - last.y;
//            x += dx;
//            y += dy;
//            last = m.getPoint();
//            repaint();
//        }
//    }
//    private MouseDrag mouseDrag;
//    JTextArea editorPane;
//    public DragCircle() {
//    	editorPane = new JTextArea();
//
//        setBackground(Color.WHITE);
//        mouseDrag = new MouseDrag();
//        editorPane.addMouseListener(mouseDrag);
//        editorPane.addMouseMotionListener(mouseDrag);
//    }

	  JTextArea editorPane;
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
	  

    

}