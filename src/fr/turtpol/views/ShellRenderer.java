package fr.turtpol.views;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fr.turtpol.models.Dimension;
import fr.turtpol.models.Shell;

public class ShellRenderer {

	
	/*
	 * 
	 * @see: Shell class
	 * 
	 * use to render on screen the shell: the history and the current text 
	 * 
	 */
	
	private Shell shell;
	private String fontColor;
	private String backgroundColor;
	private Graphics graphics;
	private Dimension dimension;
	
	public ShellRenderer(Shell shell, Dimension dimension){
		this.shell = shell;
		this.dimension = dimension;
		this.fontColor = "#000000";
		this.backgroundColor = "#ffffff";
	}
	
	public void render(Dimension gameDimension) {
		
		Shell shell = this.getShell();
		
		this.getGraphics().setColor(Color.decode(this.getBackgroundColor()));
		this.getGraphics().fillRect(0, gameDimension.getHeight(), this.getDimension().getWidth(),
				this.getDimension().getHeight());

		int histArSize = shell.getHistory().size();
		this.getGraphics().setColor(Color.decode(this.getFontColor()));
		//display history
		if (histArSize > 0)
			for (int i = Math.min(histArSize, 7); i >= 0; i--) {
				int index = histArSize - 1 - i;
				if (index >= 0) {
					//change the font color if it's an error/warning or regular
					if (shell.getHistory().get(index).startsWith(shell.getStartText())
							|| shell.getHistory().get(index).startsWith("> "))
						this.getGraphics().setColor(Color.decode(this.getFontColor()));
					if (shell.getHistory().get(index).startsWith("ERROR"))
						this.getGraphics().setColor(Color.red);
					if (shell.getHistory().get(index).startsWith("WARNING"))
						this.getGraphics().setColor(Color.orange);
					this.getGraphics().drawString(shell.getHistory().get(histArSize - 1 - i), 10,
							gameDimension.getHeight() + this.getDimension().getHeight() - ((i + 2) * 21));
				}
			}

		//display current text
		this.getGraphics().setColor(Color.decode(this.getFontColor()));
		String beforeTxt = shell.getText().substring(0, shell.getCursorIndex()) + shell.getCursor();
		String afterTxt = shell.getText().substring(shell.getCursorIndex(), shell.getText().length());
		this.getGraphics().drawString(shell.getStartText() + beforeTxt + afterTxt, 10,
				gameDimension.getHeight() + this.getDimension().getHeight() - 25);
	}
	
	
	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}
	
}
