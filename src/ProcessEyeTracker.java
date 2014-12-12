
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.ICalibrationProcessHandler;
import com.theeyetribe.client.IConnectionStateListener;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.ITrackerStateListener;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.CalibrationResult.CalibrationPoint;
import com.theeyetribe.client.data.GazeData;

public class ProcessEyeTracker implements ICalibrationProcessHandler, IConnectionStateListener, ITrackerStateListener{  

	public boolean p = true;
	private int number_points;
	private int reSamplingCount;
	private String hostname;
	private int port;
	static GraphicsLogicEyetracker View;
	Queue<Point2D> calibrationPoints;
	Point2D currentPoint;
	private boolean trackeStateOK = true;
	private boolean isAborting = false;
	//private boolean isAbortedByUser = false;
	//private boolean calibrationServiceReady = false;
	private final int NUM_MAX_CALIBRATION_ATTEMPTS = 2;
	private final int NUM_MAX_RESAMPLE_POINTS = 4;
	private final double TARGET_PADDING = 0.1;
	public double result;
	private boolean mirror;

	ProcessEyeTracker(int number_points, GraphicsLogicEyetracker SC){
		this.mirror = false;//mirror;
		this.hostname = "localhost";//hostname;
		this.port = 6555;//port;
		
		if (GazeManager.getInstance().isActivated()){
			GazeManager.getInstance().deactivate();
		}
		
		GazeManager.getInstance().activate(ApiVersion.VERSION_1_0, ClientMode.PUSH, this.hostname, this.port);
		
		this.number_points = number_points;

		final GazeListener gazeListener = new GazeListener();
		GazeManager.getInstance().addGazeListener(gazeListener);
//		GazeManager.getInstance().addTrackerStateListener(new ITrackerStateListener(GazeManager.TrackerState));
		
		View = SC;

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				GazeManager.getInstance().removeGazeListener(gazeListener);
				GazeManager.getInstance().deactivate();
			}
		});


	}
	
	public void init(){
		
	}

	public void StartCalibration(){
		
		System.out.println("Start");		
		
		if (trackeStateOK != true){
			StopAndClose("Error: Device is not in a valid state, cannot calibrate.");
		}

		try{
			DoCalibrate();
		}
		catch (Exception ex){
			StopAndClose("Error: An errror occured during calibration. \nMessage: " + ex.getMessage());
		}
	}

	public void DoCalibrate(){
		System.out.println("DoCalibrate");
		reSamplingCount = 0;
		isAborting = false;
        //isAbortedByUser = false;
        
		System.out.println("isCalibrating: " +GazeManager.getInstance().isCalibrating());

		calibrationPoints = createPointList(); 
		currentPoint = PickNextPoint();		

		if (GazeManager.getInstance().isCalibrating()){
		System.out.println("==> Abort");
		GazeManager.getInstance().calibrationAbort();
		System.out.println("==> Clear");
		GazeManager.getInstance().calibrationClear();
		System.out.println("==> Ready to Go");
		}
		
		// Signal tracker server that we're about to start a calibration
		GazeManager.getInstance().calibrationStart(this.number_points, this);
		
	}

	public void Step(Point2D point, int i){
		System.out.println("--------------Step ("+point.getX()+","+point.getY()+"): " + i);
		View.newPos((int)point.getX(),(int)point.getY());
		try{Thread.sleep(500);}catch (Exception e){}; //wait for the gaze to meet the point
		if (mirror){
			GazeManager.getInstance().calibrationPointStart((int)get_mirror(point.getX()),(int)point.getY()); //start the calibration process
		}else{
			GazeManager.getInstance().calibrationPointStart((int)point.getX(),(int)point.getY()); //start the calibration process
		}

		try{Thread.sleep(500);}catch (Exception e){}; //wait for the calibration process to take the data
		GazeManager.getInstance().calibrationPointEnd(); //end the calibration process
	}

	@Override
	public void onCalibrationStarted() {
		System.out.println("--------------onCalibrationStarted");
		// tracker engine is ready to calibrate - check if we can start to calibrate
		//calibrationServiceReady = true;
		if (currentPoint != null)
			Step(currentPoint, 1);
		else{
			StopAndClose("onCalibrationStarted: currentPoint is null");
		}
	}

	@Override
	public void onCalibrationProgress(double progress) { 
		System.out.println("--------------onCalibrationProgress");
		if (calibrationPoints.size() > 0){
			currentPoint = PickNextPoint();
			Step(currentPoint, 2);
		}        
	}

	@Override
	public void onCalibrationProcessing() {
		System.out.println("--------------onCalibrationProcessing");
	}

	@Override
	public void onCalibrationResult(CalibrationResult res) {
		System.out.println("--------------onCalibrationResult ");
		
		//System.out.println("rating: " + RatingFunction(res.averageErrorDegree));

		// No result?
		if (res == null || res.calibpoints == null){
			StopAndClose("Error: Calibration result is empty.");
		}

		// Success, check results for poor points (resample)
		for (CalibrationPoint calPoint : res.calibpoints){
			if (calPoint == null || calPoint.coordinates == null)
				continue;

			// Tracker tells us to resample this point, enque it
			if (calPoint.state == CalibrationPoint.STATE_RESAMPLE || calPoint.state == CalibrationPoint.STATE_NO_DATA){
				calibrationPoints.add(new Point2D((int)calPoint.coordinates.x, (int)calPoint.coordinates.y));
			}
		}

		// Time to stop?
		if (reSamplingCount++ >= NUM_MAX_CALIBRATION_ATTEMPTS || calibrationPoints.size() >= NUM_MAX_RESAMPLE_POINTS){
			StopAndClose("Failure: Unable to calibrate.");
		}

		// If there is a point enqued for resampling we do that, otherwise we are done
		currentPoint = null;
		if (calibrationPoints.size() > 0)
			currentPoint = PickNextPoint();

		if (currentPoint != null){
			Step(currentPoint,4);
		}else{
			this.result = res.averageErrorDegree;
			StopAndClose(RatingFunction(res.averageErrorDegree));
		}
	}

	@SuppressWarnings("unused")
	private void printPoints(){
		System.out.println("*** printPoints ***");
		for (Point2D point: calibrationPoints)
			System.out.println(point.toString());
		System.out.println("*** printPoints ***");
	}

	private void StopAndClose(String msg){
		System.out.println("Done!");
		GazeManager.getInstance().deactivate();
		View.end_calibration(msg);
		return;
		//View.close(); //close the window
		//System.exit(0);
	}

	@Override
	public void onConnectionStateChanged(boolean isConnected) {
		System.out.println("onConnectionStateChanged: " + isConnected);
		View.pp.startEyetracker();
	}

	public String RatingFunction(double accuracy){
		/*if (result == null)
            return "";*/

		if (accuracy < 0.5)
			return "Calibration Quality: PERFECT";

		if (accuracy < 0.7)
			return "Calibration Quality: GOOD";

		if (accuracy < 1)
			return "Calibration Quality: MODERATE";

		if (accuracy < 1.5)
			return "Calibration Quality: POOR";

		return "Calibration Quality: REDO";
	}

	public Queue<Point2D> createPointList(){
		System.out.println("createPointList()");

		Dimension size = View.getSize();

		double scaleW = 1.0;
		double scaleH = 1.0;
		double offsetX = 0.0;
		double offsetY = 0.0;


		// add some padding 
		double paddingHeight = TARGET_PADDING;
		double paddingWidth = (size.getHeight() * TARGET_PADDING) / (double)size.getWidth(); // use the same distance for the width padding
		int PointCount = 9;
		double columns = Math.sqrt(PointCount);
		double rows = columns;

		//TODO To check this part


		ArrayList<Point2D> points = new ArrayList<Point2D>();
		for (int dirX = 0; dirX < columns; dirX++)
		{
			for (int dirY = 0; dirY < rows; dirY++)
			{

				double x = Lerp(paddingWidth, 1 - paddingWidth, dirX / (columns - 1));
				double y = Lerp(paddingHeight, 1 - paddingHeight, dirY / (rows - 1));
				points.add(new Point2D((offsetX + x * scaleW), (offsetY + y * scaleH)));
			}
		}

		Queue<Point2D> res = new LinkedList<Point2D>();
		Queue<Point2D> res2 = new LinkedList<Point2D>();
		int[] order = new int[PointCount];

		for (int c=0; c<PointCount; c++)
			order[c] = c;

		order = Shuffle(order);

		for(int number : order)
			res2.add(points.get(number));

		// De-normalize points to fit the current screen
		for(Point2D point : res2){
			point.setX( (point.getX() * View.getWidth()));
			point.setY((point.getY() * View.getHeight()));
		}

		for (Point2D p: res2)
			res.add(new Point2D((int)p.getX(),(int)p.getY()));


		return res;
	}

	public static double Lerp(double value1, double value2, double amount){
		return value1 + (value2 - value1) * amount;
	}

	private int[] Shuffle(int[] a){
		int[] array = a;
		if (array == null)
			return null;

		Random rnd = new Random();

		for (int i=array.length; i>1; i--){
			int j = rnd.nextInt(i);
			int tmp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = tmp;
		}
		return array;
	}

	private Point2D PickNextPoint(){
		if (calibrationPoints == null)
			calibrationPoints = createPointList();

		if (calibrationPoints.size() != 0)
			return calibrationPoints.remove();

		return null;
	}

	@Override
	public void onTrackerStateChanged(int trackerState) {
		trackeStateOK = false;
		String errorMessage = "";

		if (trackerState == 0)
			trackeStateOK = true;
		else if (trackerState == 1)
			errorMessage = "Device not connected.";
		else if (trackerState == 2)
			errorMessage = "A firmware updated is required.";
		else if (trackerState == 3)
			errorMessage = "Device connected to a USB2.0 port";
		else if (trackerState == 4)
			errorMessage = "No data coming out of the sensor.";	

		System.out.println(trackeStateOK + " --> " + errorMessage);
		if (isAborting){
			StopAndClose(errorMessage);
		}


		if (trackeStateOK == false){
			// Lost device, abort calib now (raise event)
			AbortCalibration(errorMessage);
		}		
	}


	@Override
	public void OnScreenStatesChanged(int screenIndex,
			int screenResolutionWidth, int screenResolutionHeight,
			float screenPhysicalWidth, float screenPhysicalHeight) {
	}

	private void AbortCalibration(String errorMessage){
		AbortCalibration("Abort", errorMessage);
	}

	private void AbortCalibration(String type, String errorMessage){
		if (isAborting)
			return; // Only one call is needed

		isAborting = true;
		//isAbortedByUser = true;
		GazeManager.getInstance().calibrationAbort();
		
		StopAndClose("(AbortCalibration) " + type + " - " + errorMessage);
	}
	
	private double get_mirror(double x){
		return View.getSize().width-x;
	}

}


class GazeListener2 implements IGazeListener
{

	@Override
	public void onGazeUpdate(GazeData gazeData) {
		
		// Start or stop tracking lost animation
		/*if ((gazeData.state & GazeData.STATE_TRACKING_GAZE) == 0 &&
				(gazeData.state & GazeData.STATE_TRACKING_PRESENCE) == 0) return;*/

		//Left = 10;

	}

}