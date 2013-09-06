package com.example.tictacanimals;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An Object that represents a Player for a TicTacAnimals game. Includes a name, score, and a 
 * Bitmap the user may have selected as his unit.
 * 
 * @author Syed Jafri
 *
 */
public class Player implements Parcelable {
	//TODO In the future I would like to implement Parcelable but right now I'm having trouble understanding it
	
	private int points =0;
	private String name ="";
	
	//The bitmap of the picture the player chose
	private Bitmap bm;
	
	//The resource number of the Animal the player choose. Will remove later
	private int resAnimal;
	
	public Player(){
		
	}
	public Player(String nameIn){
		name = nameIn;
	}
	
	/**
	 * 
	 * @param nameIn Player's name
	 * @param resAnimal The resource number of the Animal the player choose
	 */
	public Player(String nameIn, int resAnimalIn){
		name = nameIn;
		resAnimal = resAnimalIn;
	}
	public Player(String nameIn, Bitmap bmIn){
		name = nameIn;
		bm = bmIn;
	}
	/**
	 * Add a point
	 */
	public void addPoint(){
		points++;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the animal resource id
	 */
	public int getResAnimal() {
		return resAnimal;
	}

	/**
	 * @param name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getBitmap() {
		return bm;
	}

	public void setBitmap(Bitmap bm) {
		this.bm = bm;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		//Can I write any Object?
	}
}
