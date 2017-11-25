package fr.turtpol.views;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fr.turtpol.models.Path;
import fr.turtpol.models.Turtle;

public class TurtleRenderer {

	/*
	 * @see: Turtle class
	 */
	
	
	private Turtle turtle;
	private Graphics graphics;

	public TurtleRenderer(Turtle turtle) {
		this.turtle = turtle;
	}

	public void render() {
		Turtle turtle = this.getTurtle();

		//display the working path if exist
		if (turtle.getWorkingPath() != null) {
			if (!turtle.getWorkingPath().isReached()) {
				if (turtle.getWorkingPath().isVisible()) {
					this.getGraphics().setColor(Color.decode(turtle.getWorkingPath().getColor()));
					this.getGraphics().draw(turtle.getWorkingPath().getLine());
				}
			}

		}
		
		//display all paths
		for (Path path : turtle.getPaths()) {
			if (path.isVisible()) {
				this.getGraphics().setColor(Color.decode(path.getColor()));
				this.getGraphics().draw(path.getLine());
			}
		}

		//display the turtle
		Color color = Color.decode(turtle.getColor());
		turtle.getImage().setImageColor(color.getRed(), color.getGreen(), color.getBlue());
		turtle.getImage().setRotation(turtle.getLocation().getOrientation());
		if (turtle.isVisible()) {
			this.getGraphics().drawImage(turtle.getImage(), turtle.getLocation().getX() - 25,
					turtle.getLocation().getY() - 25);
		}
	}

	public Turtle getTurtle() {
		return turtle;
	}

	public void setTurtle(Turtle turtle) {
		this.turtle = turtle;
	}

	public void setPathColor(Color pathColor) {
		this.getGraphics().setColor(pathColor);
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

}
