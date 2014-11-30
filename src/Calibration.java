import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.ICalibrationProcessHandler;
import com.theeyetribe.client.ICalibrationResultListener;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.data.CalibrationResult;

public class Calibration {
	
	public void startCalibration() {
        final GazeManager gm = GazeManager.getInstance();
        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
        CalibrationHandler calibration = new CalibrationHandler();
        CalibrationResultListener calibrationResult = new CalibrationResultListener();
        gm.addCalibrationResultListener(calibrationResult);
        gm.calibrationStart(9, calibration);
	}
	
    private class CalibrationHandler implements ICalibrationProcessHandler
    {

		@Override
		public void onCalibrationProcessing() {
			// TODO Auto-generated method stub
			System.out.println("Processing");
		}

		@Override
		public void onCalibrationProgress(double arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCalibrationResult(CalibrationResult arg0) {
			// TODO Auto-generated method stub
			System.out.println("Result"+arg0.toString());

		}

		@Override
		public void onCalibrationStarted() {
			// TODO Auto-generated method stub
			System.out.println("Started");
		}
    	
    }

    private class CalibrationResultListener implements ICalibrationResultListener
    {

		@Override
		public void onCalibrationChanged(boolean arg0, CalibrationResult arg1) {
			// TODO Auto-generated method stub
	       System.out.println("Result: "+arg1.toString());
		}
    
    }
}
