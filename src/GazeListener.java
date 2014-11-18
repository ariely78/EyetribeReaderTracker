import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;


class GazeListener implements IGazeListener
{
    public void onGazeUpdate(GazeData gazeData)
    {
        System.out.println(gazeData.smoothedCoordinates.x);

        int x = (int)gazeData.smoothedCoordinates.x;
        int y = (int)gazeData.smoothedCoordinates.y;

        
    }
    
}
