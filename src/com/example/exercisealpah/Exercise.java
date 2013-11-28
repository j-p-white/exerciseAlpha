package com.example.exercisealpah;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class Exercise extends Activity{
	String mySecpath = "rtsp://v8.cache1.c.youtube.com/CjYLENy73wIaLQmFldMgtrB48RMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYLnU4b-EjtG4UQw=/0/0/0/video.3gp";
	VideoView vid;
	MediaController Mcontrol;

	//sets the prefrences on creation
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//follows the content layout in exercise activity
		setContentView(R.layout.activity_exercise);
		getData(); 
	}// end on create

	// this method is make to handel and stop orientation
	public void getData(){
		//get the intent data


			vid = (VideoView) findViewById(R.id.myvideoview);
			vid.setVideoPath(mySecpath);
			
			Mcontrol = new MediaController(this);
			Mcontrol.setMediaPlayer(vid);
			vid.setMediaController(Mcontrol);
			vid.requestFocus();
			//videoView.start();

			//make a new thread 
			new Thread(){

				@Override
				public void run(){
					try{

						sleep(5);//50000
					} catch (Exception e) {

					}  
					Intent myintent = new Intent(Exercise.this,Calibration.class);
					startActivity(myintent);
					finish();
				}
			}.start();  

	}// end method      
}// end class

