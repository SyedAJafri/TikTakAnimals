package com.example.tictacanimals;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameActivity extends Activity {
		
	//For key-Value pairs later for onInstanceState
	private final static String PLAYER_1_POINTS = "player1Points";
	private final static String PLAYER_2_POINTS = "player2Points";	
	private final static String PLAYER_1_NAME = "player1Name";
	private final static String PLAYER_2_NAME = "player2Name";	
	private final static String PLAYER_1_BM = "player1BM";
	private final static String PLAYER_2_BM = "player2BM";	
	
	//Possible key-value pairs after I learn how to use parcelable
	private final static String PLAYER_GRID = "playerGrid";
	private final static String CURR_PLAYER = "currPlayer";
	private final static String BOXES = "boxes";
	
	//Sounds for winning, tieing, or pressing on a spot
	MediaPlayer winTone;
	MediaPlayer pressTone;
	MediaPlayer tieTone;
	
	//Animation whenever a player places a unit in a box
	ObjectAnimator yRotateAnimate;
	
	//The players used in the game
	private Player player1;
	private Player player2;
	
	
	//int array representing whichPlayer is in a space if there is none it is 0 by defualt.
	//1 represents player1 and 2 represents player 2
	private Player[][] playerGrid = new Player[3][3];
	//The currentPlayer
	private Player currPlayer;
		
	//The boxes that the users will place their units on
	ImageView box1;
	ImageView box2;
	ImageView box3;
	ImageView box4;
	ImageView box5;
	ImageView box6;
	ImageView box7;
	ImageView box8;
	ImageView box9;
	
	//Maybe change this to a 3*3 array to point to objects in threeInARow()
	ImageView[][] boxes;
	Vibrator vibrate;
	
	LinearLayout mainLayout;
	
	
	/****For winning animations****/
	ImageView[] winningBoxes;
	ObjectAnimator wonAnimation;
	ImageView currBoxForWonAnimate;
	
	
	AnimatorListener wonAnimationListener = new AnimatorListener(){

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {
			animateWinningBoxes();
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}

		@Override
		public void onAnimationStart(Animator animation) {
			
			stopClicking=true;
			
		}

	};
	
	/****End of winning animation related code****/
	
	//If this game is a simple game we will use the default values if not we can get them from the SetupGallary Activity 
	boolean isSimpleGame;
	
	//Prevents the player from the time a game is won till the game is cleared
	private boolean stopClicking =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		//If their is no Extra called isSimpleGame then it will remain true and we will have a simple game with the default players
		isSimpleGame = getIntent().getBooleanExtra("IS_SIMPLE_GAME", true);
		
		System.out.println("is simple? " + isSimpleGame);
		if(savedInstanceState!=null){
			//Recreating original game current solution does not retrieve the current game
			//TODO Implement parcelable on Player class and retrieve it instead to also recreate game
			player1 = new Player();		
			player2 = new Player();
			player1.setBitmap((Bitmap) savedInstanceState.getParcelable(PLAYER_1_BM));
			player2.setBitmap((Bitmap) savedInstanceState.getParcelable(PLAYER_2_BM));
			player1.setPoints(savedInstanceState.getInt(PLAYER_1_POINTS));
			player2.setPoints(savedInstanceState.getInt(PLAYER_2_POINTS));
			player1.setName(savedInstanceState.getString(PLAYER_1_NAME));
			player2.setName(savedInstanceState.getString(PLAYER_2_NAME));
		}else{
			if(isSimpleGame){
				player1 = new Player("Sly Fox");		
				player2 = new Player("Wary Bird");
				//Get Bitmap from resources
				player1.setBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.animal1));
				player2.setBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.animal2));
			}else{
				//TODO After implementing parcelable get players from other activity instead of fields
				player1 = new Player(getIntent().getStringExtra("PLAYER1_NAME"));		
				player2 = new Player(getIntent().getStringExtra("PLAYER2_NAME"));
				player1.setBitmap((Bitmap) getIntent().getParcelableExtra("PLAYER1_BM"));
				player2.setBitmap((Bitmap) getIntent().getParcelableExtra("PLAYER2_BM"));
			}
		}

		//Initialize other variables
		box1 = (ImageView) findViewById(R.id.ImageView1);
		
		//Odd number for resource since altering ImageView2's id name messes up it's placement for some odd reason
		box2 = (ImageView) findViewById(R.id.ImageView21);
		
		box3 = (ImageView) findViewById(R.id.ImageView3);
		box4 = (ImageView) findViewById(R.id.ImageView4);
		box5 = (ImageView) findViewById(R.id.ImageView5);
		box6 = (ImageView) findViewById(R.id.ImageView6);
		box7 = (ImageView) findViewById(R.id.ImageView7);
		box8 = (ImageView) findViewById(R.id.ImageView8);
		box9 = (ImageView) findViewById(R.id.ImageView9);
		
		mainLayout = (LinearLayout) findViewById(R.id.MainLinearLayout);
		
		vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		winTone = MediaPlayer.create(this, R.raw.wintone);
		pressTone = MediaPlayer.create(this, R.raw.spottonefiltered);
		tieTone = MediaPlayer.create(this, R.raw.tietone);
		
		//Create ArrayList to hold all TicTacToe boxes 
		boxes = new ImageView[3][3];
		
		boxes[0][0] = box1;
		boxes[0][1] = box2;
		boxes[0][2] = box3;
		boxes[1][0] = box4;
		boxes[1][1] = box5;
		boxes[1][2] = box6;
		boxes[2][0] = box7;
		boxes[2][1] = box8;
		boxes[2][2] = box9;
	
		
		winningBoxes = new ImageView[3]; 
		
		
		setupActionBar();
		
		//Starting game for the first time so set all spots to null
		clearGame();

	}

	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getActionBar().setTitle(player1.getName()+"'s Turn" );
		//Display name and points of every player
		getActionBar().setSubtitle(player1.getName()+": "+ player1.getPoints()+" | "+player2.getName()+ ": "
				+ player2.getPoints());
		
		ColorDrawable actionBarColor = new ColorDrawable(Color.rgb(238, 221, 130));
		getActionBar().setBackgroundDrawable(actionBarColor);
	}

	private void updateActionBar(){
		
		getActionBar().setSubtitle(player1.getName()+": "+ player1.getPoints()+" | "+player2.getName()+ ": "
				+ player2.getPoints());
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(PLAYER_1_POINTS, player1.getPoints());
		outState.putInt(PLAYER_2_POINTS, player2.getPoints());
		outState.putParcelable(PLAYER_1_BM, player1.getBitmap());
		outState.putParcelable(PLAYER_2_BM, player2.getBitmap());
		outState.putString(PLAYER_1_NAME, player1.getName());
		outState.putString(PLAYER_2_NAME, player1.getName());
		//outState.putBLANKArray(PLAYER_GRID, playerGrid);
		
		//Use Serializable or Parcelable to save Objects (parceable is faster but a bit ugly apparently) 
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
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
	
	//All View Clickables that represents each of the TicTakToe boxes
	public void view1Clicked(View view){
		masterClicked(box1,0,0);
	}
	public void view2Clicked(View view){
		masterClicked(box2,0,1);
	}
	public void view3Clicked(View view){
		masterClicked(box3,0,2);
	}
	public void view4Clicked(View view){
		masterClicked(box4,1,0);
	}
	public void view5Clicked(View view){
		masterClicked(box5,1,1);
	}
	public void view6Clicked(View view){
		masterClicked(box6,1,2);
	}
	public void view7Clicked(View view){
		masterClicked(box7,2,0);
	}
	public void view8Clicked(View view){
		masterClicked(box8,2,1);
	}
	public void view9Clicked(View view){
		masterClicked(box9,2,2);
	}
	
	/**
	 * Checks to see if a space is occupied if not it will place an animal there plus animate it. If the space is 
	 * taken it will display a Toast informing the user and vibrate.
	 * @param box The ImageView that was clicked
	 * @param row The row of the ImageView
	 * @param col The col of the ImageView
	 */
	private void masterClicked(ImageView box, int row, int col){
		
		//Start the game if it hasn't already
		if(currPlayer==null){
			nextTurn();
		}
		//Check if threeInARow before running (Prevents user from clicking 
		if(stopClicking==false){
			//gameRunner();
			if(playerGrid[row][col]==null){
				
				/**Error on Nexus S giving"start called in state 0" and "error (-38, 0)"**/
				//Can use onPrepared(MediaPlayer player) and threads... (?)
				if(pressTone.isPlaying()==false){
					pressTone.start();
				}
				
				ImageView currBox = box;
				int i =0;
				
				currBox.setImageBitmap(currPlayer.getBitmap());
				
				playerGrid[row][col]= currPlayer;
				
				//animate Object slightly
				yRotateAnimate = ObjectAnimator.ofFloat(currBox,"rotationY",0.0F, -30.0F, 30.0F, 0.0F);
				yRotateAnimate.setDuration(300);
				yRotateAnimate.start();
				
				gameRunner();
				
				nextTurn();
			}else{
				vibrate.vibrate(20);
				Toast spaceTakenToast = Toast.makeText(this, "Space taken by " + currPlayer.getName(), Toast.LENGTH_SHORT);
				spaceTakenToast.show();
			}
		}
	}
	
	private void clearGame(){
		
		stopClicking=false;
		//Iterate through the boxes clearing it
		for(int x =0; x<3; x++){
			for(int y=0; y<3; y++){
				boxes[x][y].setImageBitmap(null);
			}
		}
		//New game create so create clear space
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				playerGrid[x][y] = null;
			}
		}
		for(int pos =0 ; pos<3; pos++){
			winningBoxes[pos]=null;
		}
		currBoxForWonAnimate= null;
		
		//TODO Temp solution till I use threads(?)
		pressTone.release();
		pressTone = null;
		pressTone = MediaPlayer.create(this, R.raw.spottonefiltered);
		
		//Allow the player to click now
		stopClicking=false;
	}
	
	/**
	 * Finds if a player won or not then adds a point to that player if it won
	 * @return Player that won
	 */
	private Player threeInARow(){
		//Check if the current Player won
		//Check horizontal
		for(int row=0; row<3; row++){
			if(playerGrid[row][0]==currPlayer&&playerGrid[row][1]==currPlayer&&playerGrid[row][2]==currPlayer){
				winningBoxes[0] = boxes[row][0];
				winningBoxes[1] = boxes[row][1];
				winningBoxes[2] = boxes[row][2];
				return currPlayer;
			}
		}
		//Check vertical
		for(int col=0; col<3; col++){
			if(playerGrid[0][col]==currPlayer&&playerGrid[1][col]==currPlayer&&playerGrid[2][col]==currPlayer){
				winningBoxes[0] = boxes[0][col];
				winningBoxes[1] = boxes[1][col];
				winningBoxes[2] = boxes[2][col];
				return currPlayer;
			}
		}		
		
		
		//Check diagonal
		if(playerGrid[0][0]==currPlayer&&playerGrid[1][1]==currPlayer&&playerGrid[2][2]==currPlayer){
			winningBoxes[0] = boxes[0][0];
			winningBoxes[1] = boxes[1][1];
			winningBoxes[2] = boxes[2][2];
			return currPlayer;
		}
		
		//Check opposing diagonal
		if(playerGrid[0][2]==currPlayer&&playerGrid[1][1]==currPlayer&&playerGrid[2][0]==currPlayer){
			winningBoxes[0] = boxes[0][2];
			winningBoxes[1] = boxes[1][1];
			winningBoxes[2] = boxes[2][0];
			return currPlayer;
		}
		
		//No one won keep going
		return null;
	}
	
	/**
	 * Move to the next turn changing the currPlayer number
	 */
	private void nextTurn(){
		if(currPlayer==null){
			currPlayer =player1;
		}else if(currPlayer==player1){
			currPlayer=player2;
		}else{
			currPlayer=player1;
		}
		
		//update the players turn
		getActionBar().setTitle(currPlayer.getName()+"'s Turn" );
	}
	private void gameRunner(){

		if(threeInARow()!=null){
			//Added to prevent the winTone and pressTone from simultaneously playing
			if(pressTone.isPlaying()){
				pressTone.stop();
			}
			//Play winning tone, update points, and display Toast
			System.out.println(currPlayer.getName() + " won");
			currPlayer.addPoint();
			setupWinningAnimateBoxes();
			animateWinningBoxes();
			Toast gameWon = Toast.makeText(this, currPlayer.getName() + " won!", Toast.LENGTH_LONG);
			gameWon.show();
			updateActionBar();
			winTone.start();
		}else{
			//The number of spaces used when it reaches 9 then the board is full
			int spacesUsed =0;
			for(int x=0; x<3; x++){
				for(int y=0; y<3; y++){
					if(playerGrid[x][y]!=null){
						spacesUsed++;
					}
				}
			}
			//Check if all the spaces are used and if no one has a match
			if(spacesUsed==9&&threeInARow()==null){
				Log.d("GameActivity.gameRunner", "Game Tied threeInARow returned null");
				tieTone.start();
				//The game is full and no one won so we can clear the game and toast that info
				Toast gameTied = Toast.makeText(this, "Game Tied", Toast.LENGTH_LONG);
				gameTied.show();
				clearGame();
				
			}
		}
	}
	
	/**
	 * Setup the box animation for winning
	 */
	private void setupWinningAnimateBoxes(){
		wonAnimation = ObjectAnimator.ofFloat(currBoxForWonAnimate, "rotationX", 0.0F, 180.0F, -180.0F, 0.0F);
		wonAnimation.setDuration(250);
		wonAnimation.addListener(wonAnimationListener);
	}
	
	/**
	 * Animate the winning boxes one by one. Called after each animation ending till all three have been
	 * animated then clear the game
	 */
	private void animateWinningBoxes(){
		if(currBoxForWonAnimate == null){
			Log.d("GameActivity.animateWinningBoxes", "CurrBox null");
			if(winningBoxes[0]==null){
				Log.d("GameActivity.animateWinningBoxes", "WinningBoxes[0] null");
			}
			currBoxForWonAnimate = winningBoxes[0];
			setupWinningAnimateBoxes();
			wonAnimation.start();
		}else if(currBoxForWonAnimate == winningBoxes[0]){
			currBoxForWonAnimate = winningBoxes[1];
			setupWinningAnimateBoxes();
			wonAnimation.start();
		}else if(currBoxForWonAnimate == winningBoxes[1]){
			currBoxForWonAnimate = winningBoxes[2];
			setupWinningAnimateBoxes();
			wonAnimation.start();
		}else if(currBoxForWonAnimate == winningBoxes[2]){
			clearGame();
		}
	}
	
	/**
	 * Save all the data to XML to retrieve later. Will only save if it is a custom game.
	 */
	private void saveInfoToXML(){
		if(isSimpleGame==false){
			//TODO Save info play with XML parser
			
		}
	}
}
