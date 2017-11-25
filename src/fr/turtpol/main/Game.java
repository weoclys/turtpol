package fr.turtpol.main;

import java.util.Arrays;
import java.util.HashSet;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;

import fr.turtpol.models.Dimension;
import fr.turtpol.models.Shell;
import fr.turtpol.models.Turtle;
import fr.turtpol.utils.CommandProcessor;
import fr.turtpol.views.ShellRenderer;
import fr.turtpol.views.TurtleRenderer;

public class Game extends BasicGame implements InputListener {

	/*
	 * 
	 * main class
	 * 
	 * 
	 */

	private Dimension dimension;
	private String color;

	private Turtle turtle;
	private Shell shell;

	private ShellRenderer shellRenderer;
	private TurtleRenderer turtleRenderer;

	public Game(Turtle turtle, Shell shell) {
		super("Turtpol"); // name of the window

		this.dimension = new Dimension(1520, 680);
		TurtleRenderer turtleRenderer = new TurtleRenderer(turtle);
		ShellRenderer shellRenderer = new ShellRenderer(shell, new Dimension(this.dimension.getWidth(), 200));
		this.turtle = turtle;
		this.shell = shell;
		this.turtleRenderer = turtleRenderer;
		this.shellRenderer = shellRenderer;
		this.color = "#000000";

	}

	public Game() {
		this(new Turtle(), new Shell());
	}

	public static void main(String[] args) throws SlickException {

		Game game = new Game();

		AppGameContainer app = new AppGameContainer(game);
		app.setDisplayMode(game.getDimension().getWidth(),
				game.getDimension().getHeight() + game.getShellRenderer().getDimension().getHeight(), false);
		app.setShowFPS(false);
		app.start();

	}

	/**************************************************************************************/
	/*------------------------------------------------------------------------------------*/
	/**************************************************************************************/

	public void keyPressed(int key, char c) {

		// use to move the cursor
		if (key == Input.KEY_LEFT && this.getShell().getCursorIndex() > 0) {
			this.getShell().setCursorIndex(this.getShell().getCursorIndex() - 1);
		}
		if (key == Input.KEY_RIGHT && this.getShell().getCursorIndex() < this.getShell().getText().length()) {
			this.getShell().setCursorIndex(this.getShell().getCursorIndex() + 1);
		}

		// erase text
		if (key == Input.KEY_BACK && this.getShell().getText().length() > 0) {
			if (this.getShell().getCursorIndex() - 1 >= 0) {
				String beforeTxt = this.getShell().getText().substring(0, this.getShell().getCursorIndex() - 1);
				String afterTxt = this.getShell().getText().substring(this.getShell().getCursorIndex(),
						this.getShell().getText().length());
				this.getShell().setCursorIndex(this.getShell().getCursorIndex() - 1);
				this.getShell().setText(beforeTxt + afterTxt);
				return;
			}
		}

		// process command
		if (key == Input.KEY_ENTER) {

			if (this.getShell().getText().equals(""))
				return;
			if (this.getTurtle().getWorkingPath() == null || this.getShell().getText().toUpperCase().equals("STOP")) {
				this.getShell().setHistIndex(0);
				this.getShell().setCursorIndex(0);
				this.getShell().getHistory().add(this.getShell().getStartText() + this.getShell().getText());
				this.getShell().getCommandsHistory().add(this.getShell().getText());
				CommandProcessor.processCommand(this.getTurtle(), this.getShell());

				this.getShell().setText("");
			} else {
				this.getShell().getHistory().add("WARNING: the turtle is working, wait !");
			}
			return;
		}

		int ascii = (int) c;
		int sizeHist = this.getShell().getCommandsHistory().size();
		int histIndex = this.getShell().getHistIndex();

		// navigate through history with the arrows
		if (key == Input.KEY_UP) {
			if (histIndex < sizeHist) {
				this.getShell().setHistIndex(histIndex + 1);
				this.getShell().setText(this.getShell().getCommandsHistory().get(sizeHist - histIndex - 1));
				this.getShell().setCursorIndex(this.getShell().getText().length());
			}
		} else if (key == Input.KEY_DOWN) {
			if (histIndex > 0) {
				this.getShell().setHistIndex(histIndex - 1);
				this.getShell().setText(this.getShell().getCommandsHistory().get(sizeHist - histIndex));
				this.getShell().setCursorIndex(this.getShell().getText().length());
			}
		}

		// 91, 93, 58, 32, 35
		// [ ] : space #
		Integer[] validAscii = { 91, 93, 58, 32, 35 };
		HashSet<Integer> setValidAscii = new HashSet<Integer>(Arrays.asList(validAscii));

		// regex: [A-Z_a-z_0-9] and those in validAscii
		if ((65 <= ascii && ascii <= 90) || (97 <= ascii && ascii <= 122) || (48 <= ascii && ascii <= 57)
				|| setValidAscii.contains(ascii)) {
			String beforeTxt = this.getShell().getText().substring(0, this.getShell().getCursorIndex()) + c;
			String afterTxt = this.getShell().getText().substring(this.getShell().getCursorIndex(),
					this.getShell().getText().length());
			this.getShell().setCursorIndex(this.getShell().getCursorIndex() + 1);

			this.getShell().setText(beforeTxt + afterTxt);
		}
	}

	public void init(GameContainer gc) throws SlickException {
		// init graphics
		this.getShellRenderer().setGraphics(gc.getGraphics());
		this.getTurtleRenderer().setGraphics(gc.getGraphics());
		this.getTurtle().setImage("images/turtle.png");
		CommandProcessor.proccessCommand("VE", 1, this.getTurtle());

	}

	public void update(GameContainer gc, int delta) throws SlickException {

		// if an action is done, we don't need to keep it in memory
		if (this.getTurtle().getActions().size() > 0)
			this.getTurtle().getActions().removeIf(a -> a.isDone());

		// if there is an action to perform ...
		if (this.getTurtle().getActions().size() > 0)
			this.getTurtle().getActions().get(0).Perform(this.getTurtle(), this.getDimension());

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

		g.setBackground(Color.decode(this.getBackgroundColor()));
		this.getTurtleRenderer().render();
		this.getShellRenderer().render(this.getDimension());
		// cursor animation
		this.getShell().updateTimer();

	}

	/**************************************************************************************/
	/*------------------------------------------------------------------------------------*/
	/**************************************************************************************/

	public Shell getShell() {
		return this.shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Turtle getTurtle() {
		return turtle;
	}

	public void setTurtle(Turtle turtle) {
		this.turtle = turtle;
	}

	public void setBackgroundColor(String color) {
		this.color = color;
	}

	public String getBackgroundColor() {
		return this.color;
	}

	public ShellRenderer getShellRenderer() {
		return shellRenderer;
	}

	public void setShellRenderer(ShellRenderer shellRenderer) {
		this.shellRenderer = shellRenderer;
	}

	public TurtleRenderer getTurtleRenderer() {
		return turtleRenderer;
	}

	public void setTurtleRenderer(TurtleRenderer turtleRenderer) {
		this.turtleRenderer = turtleRenderer;
	}

}