import java.util.LinkedList;
import java.util.Queue;
//import Utilities.Point2D;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.ICalibrationProcessHandler;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.CalibrationResult.CalibrationPoint;
import com.theeyetribe.client.data.GazeData;
import com.theeyetribe.client.data.Point2D;

public class Calibration implements ICalibrationProcessHandler{  

   private int number_points;
   private int reSamplingCount;

   Queue<Point2D> calibrationPoints;
   Point2D currentPoint;
   private final int NUM_MAX_CALIBRATION_ATTEMPTS = 2;
   private final int NUM_MAX_RESAMPLE_POINTS = 4;

   CalibrationPane calibrationPane;
   
   Calibration(CalibrationPane calibrationPane){
	  this.calibrationPane = calibrationPane;
      if (GazeManager.getInstance().isActivated()){
         GazeManager.getInstance().deactivate();
      }

      GazeManager.getInstance().activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);

      this.number_points = 9;

      final GazeListener gazeListener = new GazeListener();
      GazeManager.getInstance().addGazeListener(gazeListener);

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

   public void StartCalibration(){

      System.out.println("Start");      

      //Checking if Eye Tracker is OK -> ITrackerStateListener
      //StopAndClose("Error: Device is not in a valid state, cannot calibrate.");

      DoCalibrate();
   }

   public void DoCalibrate(){
      System.out.println("DoCalibrate");
      reSamplingCount = 0;

      System.out.println("isCalibrating: " +GazeManager.getInstance().isCalibrating());

      calibrationPoints = createPointList(); 
      currentPoint = PickNextPoint();      

      if (GazeManager.getInstance().isCalibrating()){ //I am not sure if this is the best way to do it
         GazeManager.getInstance().calibrationAbort();
         GazeManager.getInstance().calibrationClear();
      }

      // Signal tracker server that we're about to start a calibration
      GazeManager.getInstance().calibrationStart(number_points, this);

   }

   public void Step(Point2D point){
	  this.calibrationPane.newPosition((int)point.x, (int)point.y); //Put a new point graphically
      
      try{Thread.sleep(500);}catch (Exception e){}; //wait for the gaze to meet the point
      GazeManager.getInstance().calibrationPointStart((int)point.x,(int)point.y); //start the calibration process
      
      try{Thread.sleep(500);}catch (Exception e){}; //wait for the calibration process to take the data
      GazeManager.getInstance().calibrationPointEnd(); //end the calibration process
   }

   @Override
   public void onCalibrationStarted() {
      // tracker engine is ready to calibrate - check if we can start to calibrate
      //calibrationServiceReady = true;
      if (currentPoint != null)//if there is another poitn to calibrate, do it
         Step(currentPoint);
      else{
         StopAndClose("onCalibrationStarted: currentPoint is null"); //otherwise Close
      }
   }

   @Override
   public void onCalibrationProgress(double progress) { 
      if (calibrationPoints.size() > 0){ //The process will be done as long as there are points to be calibrarted
         currentPoint = PickNextPoint();
         Step(currentPoint);
      }        
   }

   @Override
   public void onCalibrationProcessing() {
      System.out.println("--------------onCalibrationProcessing");
   }

   @Override
   public void onCalibrationResult(CalibrationResult res) {
      System.out.println("--------------onCalibrationResult ");

      //If there is not result, Stop
      if (res == null || res.calibpoints == null){
         StopAndClose("Error: Calibration result is empty.");
      }

      //There might points that need to be recalibrated
      for (CalibrationPoint calPoint : res.calibpoints){
         if (calPoint == null || calPoint.coordinates == null)
            continue;

         //information is taken from the Tracker
         if (calPoint.state == CalibrationPoint.STATE_RESAMPLE || calPoint.state == CalibrationPoint.STATE_NO_DATA){
            calibrationPoints.add(new Point2D((float)calPoint.coordinates.x, (float)calPoint.coordinates.y));
         }
      }

      //The process will not run forever. 
      //-> there is a fixed number for recalibration
      //-> the calibration is done again if there are not so many points to be recalibrated
      if (reSamplingCount++ >= NUM_MAX_CALIBRATION_ATTEMPTS || calibrationPoints.size() >= NUM_MAX_RESAMPLE_POINTS){
         StopAndClose("Failure: Unable to calibrate.");
      }

      currentPoint = null;
      if (calibrationPoints.size() > 0)
         currentPoint = PickNextPoint();

      if (currentPoint != null){
         Step(currentPoint);
      }else{
         StopAndClose(RatingFunction(res.averageErrorDegree));
      }
   }

   private void StopAndClose(String msg){
      System.out.println("Done!");
      //View.end_calibration(msg); //tell the graphics to stop
      return;
      //View.close(); //close the window
      //System.exit(0);
   }

   public String RatingFunction(double accuracy){
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
      Queue<Point2D> res = new LinkedList<Point2D>();
      //Create the points taking into account the size of the current screen
      return res;
   }

   private Point2D PickNextPoint(){
      if (calibrationPoints == null)
         calibrationPoints = createPointList();

      if (calibrationPoints.size() != 0)
         return calibrationPoints.remove();

      return null;
   }
}


class GazeListener implements IGazeListener
{

   @Override
   public void onGazeUpdate(GazeData gazeData) {
   }

}
