package com.example.exercisealpah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//to remove the title
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.e("DEBUG","BEFORE layout");
		setContentView(R.layout.activity_welcome);
		Thread thread = new Thread()
		{
			@Override
			public void run() {
				try {
					sleep(3000);

					Intent intent = new Intent(Welcome.this,Exercise.class);
					Log.d("DEBUG", "starting exercise?");
					startActivity(intent);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();		
	}//end oncreate

}// end class
