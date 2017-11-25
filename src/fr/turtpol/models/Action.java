package fr.turtpol.models;

import fr.turtpol.utils.ActionType;

public class Action {

	/*
	 * use to define the action of the turtle
	 * 
	 * @see: ActionType enum
	 * 
	 */

	private ActionType actionType;
	private String color;
	private float angle;
	private double distance;
	private boolean pathVisibility;
	private boolean turtleVisibility;
	private boolean done = false;

	public Action() {
		this.actionType = ActionType.RESET;
	}

	public Action(double distance) {
		this.actionType = ActionType.MOVE;
		this.distance = distance;
	}

	public Action(float angle) {
		this.actionType = ActionType.TURN;
		this.angle = angle;
	}

	public Action(String color) {
		this.actionType = ActionType.CHANGE_COLOR;
		this.color = color;
	}

	public Action(String target, boolean visibility) {
		if (target == "path") {
			this.actionType = ActionType.PATH_VISIBILITY;
			this.pathVisibility = visibility;
		} else {
			this.actionType = ActionType.TURTLE_VISIBILITY;
			this.turtleVisibility = visibility;
		}
	}

	/*
	 * perform the action based on the action type can be call multiple time:
	 * depend if the action is done
	 */
	public void Perform(Turtle turtle, Dimension gameDimension) {

		if (this.isActionType(ActionType.CHANGE_COLOR)) {
			turtle.setPathColor(this.getColor());
			this.setDone(true);

		} else if (this.isActionType(ActionType.TURTLE_VISIBILITY)) {
			turtle.setVisibility(this.turtleVisibility);
			this.setDone(true);

		} else if (this.isActionType(ActionType.PATH_VISIBILITY)) {
			if (this.pathVisibility) {
				turtle.penDown();
			} else
				turtle.penUp();
			this.setDone(true);

		} else if (this.isActionType(ActionType.RESET)) {
			turtle.getPaths().clear();
			turtle.setLocation(
					new Location((float) (gameDimension.getWidth() / 2), (float) (gameDimension.getHeight()) / 2));
			this.setDone(true);

		} else if (this.isActionType(ActionType.TURN)) {
			turtle.getLocation().addOrientation(this.angle);
			this.setDone(true);

		} else if (this.isActionType(ActionType.MOVE)) {
			if (turtle.getWorkingPath() == null) {
				Path path = new Path(turtle.getLocation());
				path.getPointToReach(turtle.getLocation().getOrientation(), this.getDistance());
				turtle.setWorkingPath(path);

			} else if (!turtle.getWorkingPath().isReached()) {
				turtle.moveForward();
			} else {
				turtle.getPaths().add(turtle.getWorkingPath());
				turtle.destroyWorkingPath();
				this.setDone(true);
			}
		}

	}

	public boolean isActionType(ActionType actionType) {
		return this.getActionType().getName() == actionType.getName();
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isPathVisibility() {
		return pathVisibility;
	}

	public void setPathVisibility(boolean pathVisibility) {
		this.pathVisibility = pathVisibility;
	}

	public boolean isTurtleVisibility() {
		return turtleVisibility;
	}

	public void setTurtleVisibility(boolean turtleVisibility) {
		this.turtleVisibility = turtleVisibility;
	}

}
