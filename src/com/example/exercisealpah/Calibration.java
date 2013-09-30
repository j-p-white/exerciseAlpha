package com.example.exercisealpah;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class Calibration extends Activity implements SensorEventListener,
OnClickListener {

	// this stuff is for the dispaly
	private SensorManager sensorManager;
	private Button btnStart, btnStop;
	private boolean started = false;
	private ArrayList<AccelData> sensorData; 
	private LinearLayout layout;
	private View mChart;

	long myT,biggestTime,totalTime;
	int max;
	// this is used for calucation
	// stors the Y values 
	public ArrayList<Double> myValues;
	public ArrayList<Long> times;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibration);
		layout = (LinearLayout) findViewById(R.id.chart_container);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorData = new ArrayList<AccelData>();
		myValues = new ArrayList<Double>();
		times = new ArrayList<Long>();

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		//btnUpload = (Button) findViewById(R.id.btnUpload);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		//btnUpload.setOnClickListener(this);
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		if (sensorData == null || sensorData.size() == 0) {
			//	btnUpload.setEnabled(false);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

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
			double x = event.values[0];
			double y = event.values[1];
			double z = event.values[2];
			long timestamp = System.currentTimeMillis();
			AccelData data = new AccelData(timestamp, x, y, z);
			sensorData.add(data);
		}
	}
	/**************************************
	 * this method is used to calculate lifting
	 * it gets the lagrest and smallest values from the array
	 * this method needed a lot of comments and 
	 * does not make a lot of sense what i did here
	 * i think there is a physics problem for this
	 *******************************************/
	public void calcLift(ArrayList<AccelData> liftingObj){

		//in this method i could also make the make gradient method
		// this method would call my array of data points 
		// modify all the points in the array by a little bit 
		// and gave them into a positive gradent and negative gradent array

		//get variables 

		double myY;
		max = liftingObj.size();
		//double gravity = 9.81; // still no idea why i need gravity 
		//maybe a physics thing like a ball for example,time it takes 
		//for a ball to hit the ground and then bounce back up? 

		// gets all the why values of my object. for some reason
		for(int i =0; i < max; i++){
			//get the accel data out of the array
			AccelData currentObj = liftingObj.get(i);

			//pick only the Y data
			myY = currentObj.getY();

			//pick only the time
			myT = currentObj.getTimestamp();

			//store my times
			times.add(myT);

			//store all they Y data in its own array
			myValues.add(myY);

		}
		//for each value in the times array
		for(long i: times){

			//store the values in total time
			totalTime = totalTime + i;
		}




		//these method are really for the rep class/unction class
		//they are used to show when the user is a point a(min) and point b(max)
		// when they are near min and max that means they completed 1 rep

		//get the biggest time 
		double Biggest =  Collections.max(myValues);

		//get the biggest time
		biggestTime = Collections.max(times);




		// finds the samllest Y value in the array, Bottom 	
		double Smallest = Collections.min(myValues);

		//displays the biggest and smallest values to see if it was working
		Toast toast = Toast.makeText(getApplicationContext(), "Biggest:"+ Biggest +"\n"+"Smallest:"+ Smallest
				+"\n"+"BiggestIme: "+ biggestTime +" \n"+"totalTime: "+totalTime, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			sensorData = new ArrayList<AccelData>();
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
			//btnUpload.setEnabled(true);
			started = false;
			sensorManager.unregisterListener(this);
			layout.removeAllViews();
			calcLift(sensorData);
			//openChart();
			//pass intent to function screen
			Intent intent = new Intent(Calibration.this,Function.class);

			//give function the calibration sensor array
			intent.putExtra("time", biggestTime);

			///pass on the total time
			intent.putExtra("totalTime",totalTime);

			intent.putExtra("arrayLength", max);

			//start the activity
			startActivity(intent);

			// show data in chart
			break;
		case R.id.btnUpload:

			break;
		default:
			break;
		}

	}

}
