package fr.turtpol.utils;

public enum ActionType {

	/*
	 * 
	 * @see : Action class
	 * 
	 * Contains the different actions of the turtle 
	 * 
	 */
	
	
	MOVE("move"),
	TURN("turn"),
	RESET("reset"),
	CHANGE_COLOR("change_color"),
	PATH_VISIBILITY("path_visibility"),
	TURTLE_VISIBILITY("turtle_visibility");
	
	
	private String name;
	
	ActionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return this.name();
	}
}
