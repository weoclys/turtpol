package fr.turtpol.models;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;

public class Turtle {

	/*
	 * 
	 * @params:
	 * 	location: the current location of the turtle
	 * 	speed: the speed of the turtle if > 1000, the turtle draw instantly
	 *  image: the image who represent the turtle
	 *  paths: basically what the turtle have draw
	 *  workingPath: the current path we are working on
	 *  actions: list of undone action, once an action is done, remove it from this list
	 *  visibility: if the turtle is display or not
	 *  write: is pendown or penup
	 *  color: turtle color (not use)
	 *  pathcolor: the color of the current path
	 * 
	 */
	
	
	private Location location;
	private int speed;
	private Image image;
	private ArrayList<Path> paths = new ArrayList<Path>();
	private Path workingPath;
	private ArrayList<Action> actions = new ArrayList<Action>();
	private boolean visibility;
	private boolean write;
	private String color;
	private String pathColor;

	public Turtle() {
		this.location = new Location();
		this.speed = 150;
		this.color = "#ffffff";
		this.pathColor = "#ffffff";
		this.show();
		this.penDown();
	}

	public Turtle(Location location, int speed) {
		this.location = location;
		if (speed < 0)
			speed = -speed;
		this.speed = speed;
		this.color = "#ffffff";
		this.pathColor = "#ffffff";
		this.show();
		this.penDown();
	}

	// call in the function "perform" in the Action class
	// use to move the turtle and the workingPath in the same location
	public void moveForward() {
		
		if(this.getWorkingPath() == null) return ;
		
		Path cp = this.getWorkingPath();
		cp.setColor(this.getPathColor());
		if (this.isPenDown())
			cp.show();
		else
			cp.hide();

		//first we move the line 
		cp.increaseDistance(this.getSpeed());
		
		
		// change location of the turtle 
		// her orientation is change in the function perform of the class Action
		Line currentPath = cp.getLine();
		this.getLocation().setX(currentPath.getX2());
		this.getLocation().setY(currentPath.getY2());
		
		this.setWorkingPath(cp);
	}


	public Path getWorkingPath() {
		return this.workingPath;
	}
	
	public void setWorkingPath(Path path){
		this.workingPath = path;
	}

	public void destroyWorkingPath() {
		this.workingPath = null;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	public void setPathColor(String color) {
		this.pathColor = color;
	}

	public String getPathColor() {
		return this.pathColor;
	}

	public void penUp() {
		this.write = false;
	}

	public void penDown() {
		this.write = true;
	}

	public boolean isPenDown() {
		return this.write;
	}

	public void hide() {
		this.visibility = false;
	}

	public void show() {
		this.visibility = true;
	}

	public boolean isVisible() {
		return this.visibility;
	}
	
	public void setVisibility(boolean visibility){
		this.visibility = visibility;
	}

	public ArrayList<Path> getPaths() {
		return this.paths;
	}

	public Path getLatestPath() {
		return this.getPaths().get(this.getPaths().size() - 1);
	}

	public void setImage(String path) {
		try {
			this.image = new Image(path);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Image getImage() {
		return this.image;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		if (speed < 0)
			speed = -speed;
		this.speed = speed;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
	
}
