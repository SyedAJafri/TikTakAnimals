package com.example.tictacanimals;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
/**
 * The main activity allowing the user to choose between a simple game, a bluetooth game, or advanced game
 * 
 * @author Syed Jafri
 * 
 * Please read the README
 *
 */
public class MainActivity extends Activity {
	
	
	//Tone whenever a button is pressed
    private MediaPlayer mainTone;
    
    //The animator for the buttons
    private ObjectAnimator startAnim;
    
    //The button that will undergo animation
	private Button button1;
	private Button button2;
	private Button button3;
	
	//The currentButton that was just clicked and that will go through a animation
	private Button currentButton;
	
	//
	private AnimatorListener animListener = new AnimatorListener(){

		@Override
		public void onAnimationCancel(Animator arg0) {			
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			//TODO reset currentButton to original so when the user navigates back it will still be visible
			launchActivity();
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {			
		}

		@Override
		public void onAnimationStart(Animator arg0) {			
		}
		
	};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create(Context, ResId)
        mainTone = MediaPlayer.create(this, R.raw.wintonespot2);
        
    	button1 = (Button) findViewById(R.id.button1);
    	button2 = (Button) findViewById(R.id.button2);
    	button3 = (Button) findViewById(R.id.button3);
    	
    }
    

	protected void onStop() {
		super.onStop();
		
    	//Release and null the mediaPlayer (Says to do it on stop
    	mainTone.release();
    	mainTone = null;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		
        mainTone = MediaPlayer.create(this, R.raw.wintonespot2);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void startGameClicked(View view){
    	currentButton = button1;
    	mainTone.start();
    	setupAnim();
    	startAnim.start();
    }
    
    public void bluetoothGameClicked(View view){
    	currentButton = button2;
    	mainTone.start();
    	setupAnim();
    	startAnim.start();
    }
    
    public void advancedGameClicked(View view){
    	currentButton = button3;
    	mainTone.start();
    	setupAnim();
    	startAnim.start();
    }
    
    private void setupAnim(){
        //Animations
        startAnim = ObjectAnimator.ofFloat(currentButton, "rotationX", 0.0F, 90.0F);
        startAnim.setDuration(55);
        startAnim.addListener(animListener);
    }
    
    /**
     * To be used after the button animates
     */
    private void launchActivity(){
    	
		switch(currentButton.getId()){
			case R.id.button1:
		    	Intent gameIntent = new Intent(this, GameActivity.class);
		    	startActivity(gameIntent);
		    	break;
			case R.id.button2:
		    	Intent bluetoothGameIntent = new Intent(this, BluetoothGameActivity.class);
		    	startActivity(bluetoothGameIntent);
		    	break;
			case R.id.button3:
		    	Intent advancedGameIntent = new Intent(this, SetupGame.class);
		    	startActivity(advancedGameIntent);
		    	break;
		    default:
		    	break;
		}
    }
}
