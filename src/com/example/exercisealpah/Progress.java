package com.example.exercisealpah;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

//display total error -done
//display average error - done


public class Progress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		
		Intent intent = getIntent();
		double[] myDiff = intent.getDoubleArrayExtra("diffrence");
		String total ="total Error: ";
		String average ="\n"+"averageError: ";
		String results;
		double tError = 0;
		double aError = 0;
		TextView myView = new TextView(this);
		myView.setTextSize(20);
		
		 tError=totalError(myDiff);
		 
		 aError = averageError(myDiff);
		
		 results = total+tError+average+aError;
	
		myView.setText(results);
		setContentView(myView);
		}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress, menu);
		return true;
	}

	//method to find the total error
	public double totalError(double[] myDiff){
		double totalError =0;

		
		for(double result:myDiff){
			totalError+= result;
		
		}
		return totalError;
	}// end totalError method
	
	//method to find the average error
	public double averageError(double[] myDiff){
		double avgError =0;
		
		for(double result:myDiff){
			avgError += result;
		}
		avgError=avgError/myDiff.length;
		return avgError;
		
	}//end averageError
}//end class


	
