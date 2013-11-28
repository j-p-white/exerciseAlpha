package com.example.exercisealpah;

 
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
 
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
 
public class YouTube extends YouTubeBaseActivity 
 implements YouTubePlayer.OnInitializedListener{
  
 public static final String DEVELOPER_KEY = "AIzaSyDwXjAfmQujdBYMJ-n6eKQ0qTjjUwJWY20";
 private static final int RECOVERY_DIALOG_REQUEST = 1;
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
     youTubeView.initialize(DEVELOPER_KEY, this);
 }
 
   @Override
   public void onInitializationFailure(YouTubePlayer.Provider provider,
       YouTubeInitializationResult errorReason) {
     if (errorReason.isUserRecoverableError()) {
       errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
     } else {
       String errorMessage = String.format(
         "There was an error initializing the YouTubePlayer (%1$s)", 
         errorReason.toString());
       Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
     }
   }
 
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (requestCode == RECOVERY_DIALOG_REQUEST) {
       // Retry initialization if user performed a recovery action
       getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
     }
   }
 
   protected YouTubePlayer.Provider getYouTubePlayerProvider() {
      return (YouTubePlayerView) findViewById(R.id.youtube_view);    
   }
 
   @Override
   public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
       boolean wasRestored) {
     
    if (!wasRestored) {
     player.cueVideo("fhWaJi1Hsfo");  
    }  
   }
 
}