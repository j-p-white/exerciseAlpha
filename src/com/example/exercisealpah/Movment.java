//almost done, use calibration data in display

package com.example.exercisealpah;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
 
public class Movment extends Activity implements SensorEventListener,
        OnClickListener {
	public static final String PREF_NAME = "caliPrefs";
    private SensorManager sensorManager;
    private Button btnStart, btnStop, btnUpload;
    private boolean started = false;
    private ArrayList<AccelData> sensorData,calibration,displayMaster;
    private ArrayList<ArrayList<AccelData>> masterArray;
    		double [] gravity;
    private LinearLayout layout;
    private View mChart;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movment);
        layout = (LinearLayout) findViewById(R.id.chart_container);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        gravity = new double[3];
        sensorData = new ArrayList<AccelData>();
        calibration = new ArrayList<AccelData>();
        displayMaster = new ArrayList<AccelData>();
        masterArray = new ArrayList<ArrayList<AccelData>>();
        calibration = readSharedPrefrence();//read in the array from the file
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        if (sensorData == null || sensorData.size() == 0) {
            btnUpload.setEnabled(false);
        }
 
    }
 
    @Override
    protected void onResume() {
        super.onResume();
 
    }
    
	/**************************************
	 * this method is used to calculate lifting
	 * it gets the lagrest and smallest values from the array
	 * this method needed a lot of comments and 
	 * does not make a lot of sense what i did here
	 * i think there is a physics problem for this
	 *******************************************/
	public void calcLift(ArrayList<AccelData> liftingObj){
		double myY;
		ArrayList<Double> caliYValues = new ArrayList<Double>();
		int max = liftingObj.size();

		// gets all the why values of my object. for some reason
		for(int i =0; i < max; i++){
			
			//get the accel data out of the array
			AccelData currentObj = liftingObj.get(i);

			//pick only the Y data
			myY = currentObj.getY();

			//store all they Y data in its own array
			caliYValues.add(myY);

		}
		//displays the biggest and smallest values to see if it was working
		Toast toast = Toast.makeText(getApplicationContext(),"have calibration", Toast.LENGTH_LONG);
		toast.show();
	}
    
    
    
    
    public ArrayList<AccelData> readSharedPrefrence(){
		SharedPreferences sPrefs =  getSharedPreferences(PREF_NAME,0);
		Gson gson = new Gson();
		String json = sPrefs.getString("myData", "failed");
		if(json.equals("failed")){
			return null;
		}
		Type type = new TypeToken<ArrayList<AccelData>>(){}.getType();
		
		ArrayList<AccelData> temp = gson.fromJson(json,type);
		calcLift(temp);
		
		// make the type for Gson
		return temp ;// get the Json object back to a acclData obj
		
		
	}
    
 
    @Override
    protected void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }
    }
 
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
 
    }
 
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
			//get the gravity of the events 
			final float alpha = (float) 0.8;
			
			gravity[0] = alpha *gravity[0] +(1 -alpha) *event.values[0];
			gravity[1] = alpha *gravity[1] +(1 -alpha) *event.values[1];
			gravity[2] = alpha *gravity[2] +(1 -alpha) *event.values[2];
        	
            double x = event.values[0] - gravity[0];
            double y = event.values[1] - gravity[1];
            double z = event.values[2] - gravity[2];
            long timestamp = System.currentTimeMillis();
            AccelData data = new AccelData(timestamp, x, y, z);
            sensorData.add(data);
            if(sensorData.size() == calibration.size()){
            	masterArray.add(sensorData);//store the sensor data arrays
            }
        }
 
    }
 
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnStart:
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            btnUpload.setEnabled(false);
            sensorData = new ArrayList();
            // save prev data if available
            started = true;
            Sensor accel = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accel,
                    SensorManager.SENSOR_DELAY_FASTEST);
            break;
        case R.id.btnStop:
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            btnUpload.setEnabled(true);
            started = false;
            sensorManager.unregisterListener(this);
            layout.removeAllViews();
            openChart2();
 
            // show data in chart
            break;
        case R.id.btnUpload:
 
            break;
        default:
            break;
        }
 
    }
 
    private void openChart2(){
    	if (calibration != null || calibration.size() > 0) {
            long t =  calibration.get(0).getTimestamp();
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
 
            //XYSeries xSeries = new XYSeries("X");
            XYSeries ySeries = new XYSeries("Y");
            //XYSeries zSeries = new XYSeries("Z");
            
            XYSeries yMaster = new XYSeries("MY");
 
            for (AccelData data : calibration) {
               // xSeries.add(data.getTimestamp() - t, data.getX());
                ySeries.add(data.getTimestamp() - t, data.getY());
              //  zSeries.add(data.getTimestamp() - t, data.getZ());
            }
            
            displayMaster = masterArray.get(0);
            for(AccelData data : displayMaster){
            	yMaster.add(data.getTimestamp()-t, data.getY());
            }
 
            //dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);
          //  dataset.addSeries(zSeries);
            
            dataset.addSeries(yMaster);//for displaying the other data
 /*
            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.CIRCLE);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);
            
 */         //for master deispay  
            XYSeriesRenderer yMasterRenderer = new XYSeriesRenderer();
            yMasterRenderer.setColor(Color.BLACK);
            yMasterRenderer.setPointStyle(PointStyle.TRIANGLE);
            yMasterRenderer.setFillPoints(true);
            yMasterRenderer.setLineWidth(1);
            yMasterRenderer.setDisplayChartValues(false);
            
 
            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.CIRCLE);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);
 /*
            XYSeriesRenderer zRenderer = new XYSeriesRenderer();
            zRenderer.setColor(Color.BLUE);
            zRenderer.setPointStyle(PointStyle.CIRCLE);
            zRenderer.setFillPoints(true);
            zRenderer.setLineWidth(1);
            zRenderer.setDisplayChartValues(false);
 */
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            multiRenderer.setLabelsColor(Color.RED);
            multiRenderer.setChartTitle("t vs (x,y,z)");
            multiRenderer.setXTitle("Sensor Data vrs calibration");
            multiRenderer.setYTitle("Values of Acceleration");
            multiRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < calibration.size(); i++) {
 
                multiRenderer.addXTextLabel(i + 1, ""
                        + (calibration.get(i).getTimestamp() - t));
            }
            for (int i = 0; i < 12; i++) {
                multiRenderer.addYTextLabel(i + 1, ""+i);
            }
 
       //     multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);
            multiRenderer.addSeriesRenderer(yMasterRenderer);
         //   multiRenderer.addSeriesRenderer(zRenderer);
 
            // Getting a reference to LinearLayout of the MainActivity Layout
 
            // Creating a Line Chart
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);
 
            // Adding the Line Chart to the LinearLayout
            layout.addView(mChart);
 
        }
    }
    
    /*
    private void openChart() {
        if (sensorData != null || sensorData.size() > 0) {
            long t =  sensorData.get(0).getTimestamp();
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
 
            //XYSeries xSeries = new XYSeries("X");
            XYSeries ySeries = new XYSeries("Y");
            //XYSeries zSeries = new XYSeries("Z");
            
            XYSeries yMaster = new XYSeries("MY");
 
            for (AccelData data : sensorData) {
               // xSeries.add(data.getTimestamp() - t, data.getX());
                ySeries.add(data.getTimestamp() - t, data.getY());
              //  zSeries.add(data.getTimestamp() - t, data.getZ());
            }
            
            displayMaster = masterArray.get(0);
            for(AccelData data : displayMaster){
            	yMaster.add(data.getTimestamp()-t, data.getY());
            }
 
            //dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);
          //  dataset.addSeries(zSeries);
            
            dataset.addSeries(yMaster);//for displaying the other data
 
            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.CIRCLE);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);
            
         //for master deispay  
            XYSeriesRenderer yMasterRenderer = new XYSeriesRenderer();
            yMasterRenderer.setColor(Color.BLACK);
            yMasterRenderer.setPointStyle(PointStyle.TRIANGLE);
            yMasterRenderer.setFillPoints(true);
            yMasterRenderer.setLineWidth(2);
            yMasterRenderer.setDisplayChartValues(false);
            
 
            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.CIRCLE);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);
 
            XYSeriesRenderer zRenderer = new XYSeriesRenderer();
            zRenderer.setColor(Color.BLUE);
            zRenderer.setPointStyle(PointStyle.CIRCLE);
            zRenderer.setFillPoints(true);
            zRenderer.setLineWidth(1);
            zRenderer.setDisplayChartValues(false);
 
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            multiRenderer.setLabelsColor(Color.RED);
            multiRenderer.setChartTitle("t vs (x,y,z)");
            multiRenderer.setXTitle("Sensor Data");
            multiRenderer.setYTitle("Values of Acceleration");
            multiRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < sensorData.size(); i++) {
 
                multiRenderer.addXTextLabel(i + 1, ""
                        + (sensorData.get(i).getTimestamp() - t));
            }
            for (int i = 0; i < 12; i++) {
                multiRenderer.addYTextLabel(i + 1, ""+i);
            }
 
       //     multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);
            multiRenderer.addSeriesRenderer(yMasterRenderer);
         //   multiRenderer.addSeriesRenderer(zRenderer);
 
            // Getting a reference to LinearLayout of the MainActivity Layout
 
            // Creating a Line Chart
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);
 
            // Adding the Line Chart to the LinearLayout
            layout.addView(mChart);
 
        }
    }//end method
    */
}