import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

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

            repaint();
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

    public DragCircle() {
        setBackground(Color.WHITE);
        mouseDrag = new MouseDrag();
        addMouseListener(mouseDrag);
        addMouseMotionListener(mouseDrag);
        
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, 5, 5);
    }
}