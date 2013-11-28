package com.example.exercisealpah;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class Progress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		
		Intent intent = getIntent();
		double[] myDiff = intent.getDoubleArrayExtra("diffrence");
		String val ="value: ";
		
		TextView myView = new TextView(this);
		myView.setTextSize(20); 
		
		for(double result:myDiff){
		
		    
			myView.setText(val+result);
	
		//myView.setText(ArraySize+myDiff.length);
			
		}
		setContentView(myView);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress, menu);
		return true;
	}

}
