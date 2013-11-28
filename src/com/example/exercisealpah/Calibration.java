package com.example.exercisealpah;

import java.io.Serializable;
import java.lang.reflect.Type; //this is the correct type
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Calibration extends Activity implements SensorEventListener,
OnClickListener,Serializable {
        
        public static final String PREF_NAME = "caliPrefs";
 
        // this stuff is for the dispaly
        Uri notification;
        Ringtone myRing;
        private SensorManager sensorManager;
        private Button btnStart, btnStop,btnRead;
        private boolean started = false;
        private ArrayList<AccelData> sensorData; 
        private LinearLayout layout;
        long myT,biggestTime,totalTime;
        int max;
        int maxPoints =0;
        double [] gravity;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 3*1000;
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
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
               myRing = RingtoneManager.getRingtone(getApplicationContext(), notification);

                myValues = new ArrayList<Double>();
                times = new ArrayList<Long>();
                gravity = new double[3];
                
                btnStart = (Button) findViewById(R.id.btnStart);
                btnStop = (Button) findViewById(R.id.btnStop);
                btnRead = (Button) findViewById(R.id.btnUpload);
                btnStart.setOnClickListener(this);
                btnStop.setOnClickListener(this);
                btnRead.setOnClickListener(this);
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnRead.setEnabled(true);
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
               if (started ) {
                        
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
                       
                }
               if(sensorData.size() == 37){
            	   max = sensorData.size();
            	   Log.d("calibrationCount", "calibCount: "+sensorData.size());
            	   sensorManager.unregisterListener(this);
            	   myRing.play();
            	   started = false;
               }
        }
        
        
        @SuppressLint("NewApi")
        public void sharedPrefrenceSave(){
                SharedPreferences sPrefs = getSharedPreferences(PREF_NAME,0);
                SharedPreferences.Editor sEdit = sPrefs.edit();
                Gson gson = new Gson();
                //converts the arraylist to string 
                String accelVals = gson.toJson(sensorData);
                sEdit.putString("myData", accelVals).commit();//save the arraylist as a string
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
                
                
                // make the type for Gson
                return temp ;// get the Json object back to a acclData obj
                
                
        }
                

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                case R.id.btnStart:
                        btnStart.setEnabled(false);
                        btnStop.setEnabled(true);
                        btnRead.setEnabled(true);
                        sensorData = new ArrayList<AccelData>();
                        // save prev data if available
                        started = true;
					try {
						Thread.sleep(3*1000);
					} catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
                        Log.d("start", "START");
                        myRing.play();
                        
                        	Sensor accel = sensorManager
                                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        	sensorManager.registerListener(this, accel,
                                        SensorManager.SENSOR_DELAY_NORMAL);
                        	
                        break;
                case R.id.btnStop:
                        btnStart.setEnabled(true);
                        btnStop.setEnabled(false);
                        btnRead.setEnabled(true);
                        started = false;
                        sensorManager.unregisterListener(this);
                        layout.removeAllViews();
                        sharedPrefrenceSave();
                        
                        //pass intent to function screen
                        Intent intent = new Intent(Calibration.this,UserTime.class);

                        intent.putExtra("arrayLength", max);

                        //start the activity
                        startActivity(intent);

                        // show data in chart
                        break;
                case R.id.btnUpload:
                        btnStart.setEnabled(true);
                        btnStop.setEnabled(false);
                        btnRead.setEnabled(true);
                        sensorData = readSharedPrefrence();
                        Intent intent2 = null;
                        if(sensorData == null){
                                Log.e("DEBUG", "no data in here");
                                intent2 = new Intent(Calibration.this,Calibration.class);
                                intent2.putExtra("readFailed", "readFailed");
                                startActivity(intent2);
                                finish();
                        }
                        else{
                                
                                        Log.e("DEBUG", "readbutton, we have data!");
                                //pass intent to function screen
                                intent2 = new Intent(Calibration.this,UserTime.class);
        
                                //give function the calibration sensor array
                                intent2.putExtra("time", biggestTime);
        
                                ///pass on the total time
                                intent2.putExtra("totalTime",totalTime);
        
                                intent2.putExtra("arrayLength", max);
        
                                //start the activity
                                startActivity(intent2);
                        }
                        
                        
                        break;
                default:
                        break;
                }

        }

}