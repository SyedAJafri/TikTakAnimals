package com.example.tictacanimals;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;


/**
 * Allows the user to pick a more complicated game. Options to use another set of animals, pics from the gallery
 * or the users own picture.
 * 
 * @author Syed Jafri
 *
 */
public class SetupGame extends Activity {
	
	TextView openPic;
	
	PackageManager pm;
	
	ObjectAnimator viewAnim;
	
	MediaPlayer viewSound;
	
	View view1;
	View view2;
	View view3;
	
	View currView;
	
	AnimatorListener viewAnimListener = new AnimatorListener(){
		@Override
		public void onAnimationCancel(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			launchActivity();
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationStart(Animator animation) {
			viewSound.start();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_game);
		// Show the Up button in the action bar.
		setupActionBar();
		
		view1 = findViewById(R.id.viewSetup1);
		view2 = findViewById(R.id.viewSetup2);
		view3 = findViewById(R.id.viewSetup3);
		viewSound = MediaPlayer.create(this, R.raw.spottonefiltered);
		pm = this.getPackageManager();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		ColorDrawable actionBarColor = new ColorDrawable(Color.rgb(255,160,122));
		getActionBar().setBackgroundDrawable(actionBarColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onView1Clicked(View view){
		currView = view1;
		startAnim();
	}
	
	public void onView2Clicked(View view){
		currView = view2;
		startAnim();
	}
	
	public void onView3Clicked(View view){
		if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			currView = view3;
			startAnim();
		}else{
			//The device does not have a camera
			Toast noCameraToast = Toast.makeText(this, "No Camera in this device", Toast.LENGTH_SHORT);
			noCameraToast.show();
			System.out.println("No camera");
		}
	}
	
	private void startAnim(){
		viewAnim = ObjectAnimator.ofFloat(currView, "scaleX", 1.0F, 0.1F, 1.0F);
		viewAnim.addListener(viewAnimListener);
		viewAnim.start();
	}
	
	/**
	 * To be launched after animation ends
	 */
	private void launchActivity(){
		if(currView == view1){
			//TODO
		}else if(currView == view2){
			Intent gallerySetupIntent = new Intent(this, SetupGallary.class);
			boolean useCamera = false;
			gallerySetupIntent.putExtra("USES_CAMERA", useCamera);
			startActivity(gallerySetupIntent);
		}else if(currView == view3){
			Intent gallerySetupIntent = new Intent(this, SetupGallary.class);
			boolean useCamera = true;
			gallerySetupIntent.putExtra("USES_CAMERA", useCamera);
			startActivity(gallerySetupIntent);
		}
	}
}
