package fr.turtpol.models;

public class Location {

	private float x;
	private float y;
	private float orientation;
	
	public Location(){
		this(1520/2, 680/2);
	}
	
	public Location(float x, float y) {
		this.x = x;
		this.y = y;
		this.orientation = -90;
	}
	
	public Location(float x, float y, float orientation) {
		this.x = x;
		this.y = y;
		this.orientation = -orientation % 360;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getOrientation() {
		return orientation;
	}
	public void setOrientation(float f) {
		this.orientation = f;
	}
	
	public void addOrientation(float orientation){
		this.orientation = this.orientation + orientation;
		this.orientation = this.orientation % 360;
	}
	
	
}
