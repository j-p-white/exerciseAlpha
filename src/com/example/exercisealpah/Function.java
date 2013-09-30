package com.example.exercisealpah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Function extends Activity {

	Button timeB,errorB;
	EditText timeF,errorF; 
	Editable myTime;
	Editable myError;

	Intent intent = getIntent();
	int length = intent.getIntExtra("arrayLength",1);

	//get the calibation data
	//Calibration myObj = new Calibration();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);

		//get the users time from the screen
		timeB =(Button)findViewById(R.id.SetTime);
		timeF =(EditText)findViewById(R.id.userTime);

		//get the users margin of error
		errorB =(Button)findViewById(R.id.SetErrorButton);
		errorF =(EditText)findViewById(R.id.SetErrorField);

		//when you click the time button
		timeB.setOnClickListener( 
				new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("DEBUG", "timeButton Click");

						//get the biggest time from sensor data


						//this is the biggest time
						//long apples = intent.getLongExtra("time", 1);

						//this is the total time
						long myTotalTime = intent.getLongExtra("totalTime",1);


						//get users data from the TimeField
						int userTime = Integer.parseInt(timeF.getText().toString());

						//get the 1st number of the long
						int totalTimeFixed = (int) (myTotalTime/100000000000000L);

						//get the users time divided by the total time
						int arrayWidth = (userTime/totalTimeFixed);
						
						//see if its being calculated
						Toast tost = Toast.makeText(getApplicationContext(), "fixed time: "+totalTimeFixed+"\n"+"division is: "+ arrayWidth,
								Toast.LENGTH_SHORT);
						tost.show();
					}
				});

		//when you click the set error button
		errorB.setOnClickListener( 
				new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("DEBUG", "errorButton Click");

						//get the users data from the text field
						myError = errorF.getText();
					}

					//call the next intent for userScreen
				});


	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.function, menu);
		return true;
	}

}
