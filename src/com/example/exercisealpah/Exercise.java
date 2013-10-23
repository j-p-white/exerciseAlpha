package com.example.exercisealpah;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Exercise extends Activity{
	String mySecpath = "rtsp://v8.cache1.c.youtube.com/CjYLENy73wIaLQmFldMgtrB48RMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYLnU4b-EjtG4UQw=/0/0/0/video.3gp";

	public YouTubePlayerView playerView; 
	public YouTubePlayer thePlayer; 
	public EditText myText;

	//sets the prefrences on creation
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//follows the content layout in exercise activity
		setContentView(R.layout.activity_exercise);
		getData(); 
	}// end on create

	// this method is make to handel and stop orientation
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	public void getData(){
		//get the intent data
		Intent intent = getIntent();
		intent.getStringExtra("myGame");
		String exercise = intent.getStringExtra("myExercise");
		exercise = "running";
		if(exercise.equals("running")){

			VideoView videoView = (VideoView) findViewById(R.id.myvideoview);
			MediaController mediaController = new MediaController(this);
			mediaController.setAnchorView(videoView);

			Uri video = Uri.parse(mySecpath);
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(video);
			videoView.requestFocus();
			videoView.start();

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
		}// end if

	}// end method      
}// end class

