package com.example.exercisealpah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class Directions extends Activity {
	Button nextB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		
		buttonChangeScreens();
		
	}//end oncreate 
	
	
	public void buttonChangeScreens(){
		nextB =(Button)findViewById(R.id.button1);
		
		nextB.setOnClickListener(new OnClickListener(){
			public void onClick(View z){
				Intent newintent = new Intent(Directions.this,Calibration.class);
				
				startActivity(newintent);
				
			}//end on click
		});// end the listener
	}//end the method
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directions, menu);
		return true;
	}

}
