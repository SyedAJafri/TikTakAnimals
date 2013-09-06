package com.example.tictacanimals;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class BluetoothGameActivity extends Activity {
	
	/*
	 * Still working on this section don't have access to a second android device at the moment.
	 * 
	 * Check if the other device is an android device and not a headset
	 * Make sure the other android device is running the game
	 * Figure out who will be player1 and player2 in the BT connection
	 * 
	 * [Will both applications screw up if there both running the same BT code at the same time?]
	 * [Learn about threads and using them to do the BT parts do reduce frame-skipping!]
	 */

	//Constant for onActivityResult for enabling BT
	private static final int REQUEST_ENABLE_BT = 600;
	
	//A BroadcastReceiver that detects when a BT device is found
	private final BroadcastReceiver mReciever = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//When BT discovery finds a device
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				//Get the BluetoothDevice object from the intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			}
		}
		
	};
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_game);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Register a BroadcastReceiver for when a BT device is found
		IntentFilter filterBTFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReciever, filterBTFound);
		
		
		turnOnBT();
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth_game, menu);
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
	
	/*
	 * (non-Javadoc)
	 * Check for bluetooth data
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		//Make sure the data is from the BT request
		if(requestCode==REQUEST_ENABLE_BT){
			if(resultCode==RESULT_OK){
				//Bluetooth is on or turning on
				
			}else{
				//User didn't allow request or there is an error
				
			}
		}
	}
	
	private void turnOnBT(){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(mBluetoothAdapter == null){
			//The Device does not have a Bluetooth adapter
			Toast noBluetooth = Toast.makeText(this, "BLUETOOTH NOT SUPPORTED", Toast.LENGTH_LONG);
			noBluetooth.show();
			Intent goBackIntent = new Intent(this, MainActivity.class);
			startActivity(goBackIntent);
		}else{
			//Device is BT enabled
			if(mBluetoothAdapter.isEnabled()==false){
				//If it is not enabled request the user to enable bluetooth
				Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
			}
			if(mBluetoothAdapter.getState()==BluetoothAdapter.STATE_TURNING_ON){
				//TODO For debugging delete later
				Toast bluetoothToast = Toast.makeText(this, "Bluetooth is On!", Toast.LENGTH_SHORT);
				bluetoothToast.show();
			}
			//TODO Check if it is paired with a device and other stuff
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		unregisterReceiver(mReciever);
	}
}
