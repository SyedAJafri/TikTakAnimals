package com.example.tictacanimals;

import java.io.FileNotFoundException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 
 * @author Syed Jafri
 *
 *Setup the game asking for input for the player names and what picture they want to use. They can use
 *pictures from the camera or the gallery depending on what the user chose in the previous activity.
 *
 */
public class SetupGallary extends Activity {

	//TODO Implement onSavedInstanceState()
	/**onActivityResult() request codes****/
	private static final int TAKE_PICTURE1 = 1;
	private static final int TAKE_PICTURE2 = 2;
	private static final int CHOOSE_PICTURE1 = 3;
	private static final int CHOOSE_PICTURE2 = 4;
	/********************************************/
	
	Button readyButton;
	boolean[] isFieldComplete;
	
	AlertDialog checkFieldsAlert;
	AlertDialog.Builder checkFieldsAlertBuilder;
	
	EditText player1ET;
	EditText player2ET;
	
	ImageView player1IV;
	ImageView player2IV;
	
	Bitmap player1BM;
	Bitmap player2BM;
	
	String player1Name;
	String player2Name;
	
	boolean useCamera = false;
	
	PackageManager pm;
	
	Player player1;
	Player player2;
	
	RelativeLayout setupGallaryLayout;
	
	//Initialize listeners
	
	//Listen for any changes made to the ETs for both players
	private TextWatcher playerETListener = new TextWatcher(){
	
		@Override
		public void afterTextChanged(Editable arg0) {
			checkIfCompleteAndSave();
		}
	
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {			
		}
	
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}
	};
	
	//For the checkFieldsAlert
	private OnClickListener checkFieldsPositiveListener = new OnClickListener(){

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			openGame();
		}
		
	};
	private OnClickListener checkFieldsNegativeListener = new OnClickListener(){

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			//Do nothing at all
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_gallary);
		setupGallaryLayout = (RelativeLayout)findViewById(R.id.setupGallaryLayout);
		
		player1 = new Player();
		player2 = new Player();
		
		readyButton = (Button)findViewById(R.id.readyButton);
		
		player1ET = (EditText) findViewById(R.id.editText1);
		player2ET = (EditText) findViewById(R.id.editText2);
		
		player1IV = (ImageView) findViewById(R.id.imageView11);
		player2IV = (ImageView) findViewById(R.id.imageView2);
		
		pm = this.getPackageManager();
		
		//Unclickable till both text fields are filled out and the players have chosen a picture
		readyButton.setVisibility(Button.INVISIBLE);
		readyButton.setClickable(false);
		
		isFieldComplete = new boolean[4];
		
		useCamera = this.getIntent().getExtras().getBoolean("USES_CAMERA");
		
		player1ET.addTextChangedListener(playerETListener);
		player2ET.addTextChangedListener(playerETListener);
		//TODO Add background color of old view
		setupActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup_gallary, menu);
		return true;
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		ColorDrawable actionBarColor = new ColorDrawable(Color.rgb(75,0,130));
		getActionBar().setBackgroundDrawable(actionBarColor);
	}

	/*
	 * Checks if the fields are complete then sets visability to 
	 */
	private void checkIfCompleteAndSave(){
		
		//Get the names if they are any
		player1Name = player1ET.getText().toString();
		player2Name = player2ET.getText().toString();
		
		if(player1Name.isEmpty()==false){
			//field is filled out mark as complete
			isFieldComplete[0]=true;
			System.out.println("player 1 filled out" + player1Name);
		}else{
			isFieldComplete[0]=false;
		}
		
		if(player2Name.isEmpty()==false){
			isFieldComplete[2]=true;
			System.out.println("player 2 filled out" + player2Name);
		}else{
			isFieldComplete[2]=false;

		}
		
		//Check if everything is true in isFieldComplete
		boolean allTrue = true;
		for(int pos =0; pos<4; pos++){
			if(isFieldComplete[pos]==false){
				allTrue =false;
			}
		}
		if(allTrue==true){
			//If the fields are complete then save show readyButton
			readyButton.setVisibility(Button.VISIBLE);
			readyButton.setClickable(true);
		}
	}
	public void onImage1Click(View view){
		if(useCamera){			
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			this.startActivityForResult(cameraIntent, TAKE_PICTURE1);
			
			//TODO Try to use the front camera directly if it has one, if not use the back one
			/*if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
				//Use front camera
				
			}else{
				//No front camera proceed to use other cameras (already checked for cameras in the previous activity)
			}
			*/
			
			
		}else{
			Intent getGallary = new Intent();
			getGallary.setType("image/*");
			getGallary.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(getGallary, CHOOSE_PICTURE1);
		}
	}
	
	public void onImage2Click(View view){
		if(useCamera){			
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			this.startActivityForResult(cameraIntent, TAKE_PICTURE2);
			
			//TODO Try to use the front camera directly if it has one, if not use the back one
			/*if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
				//Use front camera
				
			}else{
				//No front camera proceed to use other cameras (already checked for cameras in the previous activity)
			}
			*/
			
			
		}else{
			Intent getGallary = new Intent();
			getGallary.setType("image/*");
			getGallary.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(getGallary, CHOOSE_PICTURE2);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//returns bitmaps(?)
		if(resultCode == Activity.RESULT_OK){
			if(requestCode ==CHOOSE_PICTURE1){
				try {
					player1BM = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(data.toUri(0))));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				player1BM = processLargeBitmap(player1BM);
				displayPicture(1);
				player1.setBitmap(player1BM);
				//The user chose a picture. data should hold the (URI?) of the picture
				System.out.println("the user has chosen a picture data at" + data.getDataString() );
				
			}else if(requestCode == CHOOSE_PICTURE2){
				try {
					player2BM = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(data.toUri(0))));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				player2BM = processLargeBitmap(player2BM);
				displayPicture(2);
				player1.setBitmap(player2BM);
				//The user chose a picture. data should hold the (URI?) of the picture
				System.out.println("the user has chosen a picture data at" + data.getDataString() );
				
			}else if(requestCode == TAKE_PICTURE1){
				player1BM = (Bitmap) data.getExtras().get("data");
				displayPicture(1);
				player1.setBitmap(player1BM);
			}else if(requestCode == TAKE_PICTURE2){
				player2BM = (Bitmap) data.getExtras().get("data");
				displayPicture(2);
				player2.setBitmap(player2BM);
			}
		}else if(resultCode == Activity.RESULT_CANCELED){
			//User may have canceled
		}
	}
	
	private void displayPicture(int whichIV){
		if(whichIV==1){
			player1IV.setImageBitmap(player1BM);
			isFieldComplete[1]=true;
			checkIfCompleteAndSave();
		}else if(whichIV==2){
			player2IV.setImageBitmap(player2BM);
			isFieldComplete[3]=true;
			checkIfCompleteAndSave();
		}
	}
	
	/**
	 * A method called when the Ready Button is clicked setting up an alert to confirm if the user is satisfied with the data
	 * @param view for the onClick attribute
	 */
	public void readyButtonClicked(View view){
		//Show AlertDialog to prevent person from accidently proceeding when fields aren't filled out correctly
		checkFieldsAlertBuilder = new AlertDialog.Builder(this);
		checkFieldsAlertBuilder.setTitle("Ready to Proceed?");
		checkFieldsAlertBuilder.setMessage("Are you fine with the current settings?");
		checkFieldsAlertBuilder.setPositiveButton("Yes", checkFieldsPositiveListener);
		checkFieldsAlertBuilder.setNegativeButton("No", checkFieldsNegativeListener);
		checkFieldsAlertBuilder.show();
	}
	
	/**
	 * Launch the next activity moving the users info and pic plus an identifier that the game will not use the defaults
	 */
	private void openGame(){
		//TODO Fix player names before going into game. Such as capitalize the first letter and make sure the string is valid.
		Intent openGame = new Intent(this, GameActivity.class);
		openGame.putExtra("IS_SIMPLE_GAME", false);
		openGame.putExtra("PLAYER1_BM", player1BM);
		openGame.putExtra("PLAYER2_BM", player2BM);
		openGame.putExtra("PLAYER1_NAME", player1Name);
		openGame.putExtra("PLAYER2_NAME", player2Name);
		this.startActivity(openGame);
	}
	
	/**
	 * OpenGlRender can only support 2048*2048 max.
	 * This compresses the image and returns it as a Bitmap if it is too large.
	 * 
	 * @param Bitmap playerBM the current player's Bitmap
	 * 
	 * @return Bitmap playerBM The original if it is small enough a scaledBitmap if it is too large
	 */
	private Bitmap processLargeBitmap(Bitmap playerBM){
		if(playerBM.getHeight()>200||playerBM.getWidth()>180){
			return Bitmap.createScaledBitmap(playerBM, 180, 200, false);
		}else{
			//playerBM is small enough to add to texture
			return playerBM;
		}
		
	}
}
