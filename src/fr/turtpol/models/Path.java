package fr.turtpol.models;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import fr.turtpol.utils.Utils;

public class Path {

	/*
	 * 
	 * Represent the path followed by the turtle
	 * 
	 * @param:
	 * 	line: graphic representation of the path (class from Slick) (see function increaseDistance)
	 *	start: location where the path start
	 *	toReach: location where the path end ( see function getPointToReach )
	 *	color: the color of the line
	 *	visibility: if the path is show or not (pen down or pen up)
	 * 
	 */
	
	
	private Line line;
	private Location start;
	private Location toReach;
	private String color;
	private boolean visibility;

	public Path(Location start) {
		this.start = start;
		this.line = new Line(start.getX(), start.getY(), start.getX(), start.getY());
		this.visibility = true;
		this.color = "#ffffff";
	}

	public Path(Location start, Location toReach) {
		this.start = start;
		this.line = new Line(start.getX(), start.getY(), start.getX(), start.getY());
		this.visibility = true;
		this.color = "#ffffff";
		this.toReach = toReach;
	}

	/*
	 * use to make an 'animation'
	 */
	public void increaseDistance(int speed) {
		Line cp = this.getLine();
		
		//if the line reach the end or if the turtle is too fast, set the exact location of "toReach"
		if (this.isReached() || speed >= 1000) {
			this.getLine().set(cp.getStart(), new Vector2f(this.getToReach().getX(), this.getToReach().getY()));
			return;
		}

		Vector2f ve = cp.getEnd();
		Vector2f vtr = new Vector2f(this.getToReach().getX(), this.getToReach().getY());
		// get the length between the location to reach and the end of the line
		// lenAb allow the turtle to alway drawn at the same speed
		float lenAb = (float) Math.sqrt(Utils.sqr(vtr.getY() - ve.getY()) + Utils.sqr(vtr.getX() - ve.getX()));
		// calculate the next position of the "end" of the line with the speed of the turtle and the lenAb
		Vector2f v = new Vector2f((float) (ve.getX() + (vtr.getX() - ve.getX()) / lenAb * (0.01 * speed)),
				(float) (ve.getY() + (vtr.getY() - ve.getY()) / lenAb * (0.01 * speed)));
		this.getLine().set(cp.getStart(), v);
		
		if (this.isReached()) {
			this.getLine().set(cp.getStart(), new Vector2f(this.getToReach().getX(), this.getToReach().getY()));
		}
	}

	/*
	 * use to get the point to reach with only one location (start of the line), the orientation and the distance
	 */
	public void getPointToReach(float orientation, double distance) {

		Line cp = this.getLine();
		Vector2f vs = cp.getStart();
		float xDistance = (float) (Math.cos(Math.toRadians(orientation)) * distance);
		float yDistance = (float) (Math.sin(Math.toRadians(orientation)) * distance);
		this.setToReach(new Location(vs.getX() + xDistance, vs.getY() + yDistance));
	}

	public boolean isReached() {
		//get the distance remaining with pythagore
		float d = (float) Math.sqrt(Utils.sqr(this.toReach.getY() - this.getLine().getEnd().getY())
				+ Utils.sqr(this.toReach.getX() - this.getLine().getEnd().getX()));
		return (-1.5 <= d && d <= 1.5);
	}

	public void hide() {
		this.visibility = false;
	}

	public void show() {
		this.visibility = true;
	}

	public void setVisibility(boolean visible){
		this.visibility = visible;
	}
	
	public boolean isVisible() {
		return this.visibility;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Location getToReach() {
		return toReach;
	}

	public void setToReach(Location toReach) {
		this.toReach = toReach;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	
}
