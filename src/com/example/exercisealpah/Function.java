package com.example.exercisealpah;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Function extends Activity {

	Button timeB,nextB;
	EditText timeF; 
	Editable myTime;
	int arrayWidth;
	int length;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);

		//get the users time from the screen
		
		timeF = (EditText)findViewById(R.id.userTime);
		
		buttonListioner();

		buttonChangeScreens();
	}//end on create
	
	
		public void buttonChangeScreens(){
			nextB =(Button)findViewById(R.id.buttonNext);
			
			nextB.setOnClickListener(new OnClickListener(){
				public void onClick(View z){
					Intent newintent = new Intent(Function.this,Movment.class);
					
				//	newintent.putExtra("arrayWidth",arrayWidth);
				//	newintent.putExtra("arrayLength",length);
					
					startActivity(newintent);
					
				}
			});
		}
	
	
		public void buttonListioner(){
	
			timeB =(Button)findViewById(R.id.SetTime);
			
			
			
			
			timeB.setOnClickListener(new OnClickListener(){
				
			
				public void onClick(View v){
					//when you click the time button
					
					if(timeF.getText().toString().equals("") || timeF == null){
						timeF.setHintTextColor(Color.RED);
						timeF.setHint("Input Invalid");
						
						
						return;
					}

						Log.e("DEBUG", "timeButton Click");

						Intent intent = getIntent();
						//get the biggest time from sensor data

						 length = intent.getIntExtra("arrayLength",1);

						//this is the total time
						long myTotalTime = intent.getLongExtra("totalTime",1);


						//get users data from the TimeField
						int userTime = Integer.parseInt(timeF.getText().toString());

						//get the 1st number of the long
						int totalTimeFixed = (int) (myTotalTime/100000000000000L);

						//get the users time divided by the total time
						 arrayWidth = 2* (userTime/totalTimeFixed);
						
						//see if its being calculated
						Toast tost = Toast.makeText(getApplicationContext(), "fixed time: "+totalTimeFixed+"\n"+"division is: "+ arrayWidth,
								Toast.LENGTH_SHORT);
						tost.show();
					}// end on click
			});//end set on click method
		}//end button listenor
	
	
	
	
}//end class
