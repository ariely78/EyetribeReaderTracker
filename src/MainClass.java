
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.theeyetribe.client.*;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.GazeManager;

public class MainClass {
    public static void main(String[] args)
    {
    	JFrame frame = new JFrame();
    	//more initialization code here
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(dim.width, dim.height);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
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


    private static class GazeListener implements IGazeListener
    {
        public void onGazeUpdate(GazeData gazeData)
        {
            System.out.println(gazeData.toString());
        }

    }
}
