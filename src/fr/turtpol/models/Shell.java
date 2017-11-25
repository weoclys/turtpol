package fr.turtpol.models;

import java.util.ArrayList;

public class Shell {

	/*
	 * @see: ShellRenderer
	 * 
	 * @param:
	 * 
	 * 	commandsHistory :contains only commands written by the player
	 * 	history: contains texts display on screen (commands, errors, warnings)
	 * 	startText: hint on the current mode, are not part of the commands
	 *  text: the commands (text written by the player
	 *  endText: cursor
	 *  timer:  use to make an animation on the cursor
	 *  histIndex: use to go through the history
	 *  cursorIndex: use to know where to add the text (allow the player to change the text easily)
	 *  functionMode: if the shell is in functionMode or not
	 *  function : use only if the shell is in functionMode
	 *
	 */
	
	private ArrayList<String> commandsHistory = new ArrayList<String>();
	private ArrayList<String> history = new ArrayList<String>();

	private String startText;
	private String text;
	private String cursor;
	private int timer;
	private int histIndex;
	private int cursorIndex;
	private boolean functionMode;
	private Function function;

	public Shell() {

		this.startText = "> ";
		this.text = "";
		this.cursor = "_";
		this.timer = 0;
		this.histIndex = 0;

	}

	/*
	 * remove useless space, add useful space, set to upper case, ...
	 */
	public void normalizeCommand() {

		String cmd = this.getText().toUpperCase();
		String newCmd = "";
		char[] arCmd = cmd.toCharArray();

		for (int i = 0; i < arCmd.length; i++) {

			//remove space at the start
			if (newCmd.length() == 0)
				if (arCmd[i] == ' ')
					continue;

			//remove multiple space (keep only one)
			if (arCmd[i] == ' ')
				if (i + 1 < arCmd.length)
					if (arCmd[i + 1] == ' ')
						continue;

			//remove space at the end
			if (arCmd[i] == ' ')
				if (i + 1 == arCmd.length)
					continue;

			//add a space before ']' and '['
			if (arCmd[i] == ']' || arCmd[i] == '[')
				if (i - 1 >= 0)
					if (arCmd[i - 1] != ' ')
						newCmd += ' ';

			newCmd += arCmd[i];

			//add a space after ']' and '['
			if (arCmd[i] == '[' || arCmd[i] == ']')
				if (i + 1 < arCmd.length)
					if (arCmd[i + 1] != ' ')
						newCmd += " ";

		}

		this.setText(newCmd.replaceAll("  ", " "));
	}

	/*
	 * make the cursor blink
	 */
	public void updateTimer() {
		this.timer += 1;
		if (this.timer > 200) {
			this.timer = 0;
			if (this.cursor == "_")
				this.cursor = " ";
			else
				this.cursor = "_";
		}
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public boolean isFunctionMode() {
		return functionMode;
	}

	public void setFunctionMode(boolean functionMode) {
		this.functionMode = functionMode;
	}

	public int getCursorIndex() {
		return this.cursorIndex;
	}

	public void setCursorIndex(int value) {
		this.cursorIndex = value;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getHistIndex() {
		return this.histIndex;
	}

	public void setHistIndex(int value) {
		this.histIndex = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStartText() {
		return startText;
	}

	public void setStartText(String startText) {
		this.startText = startText;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public ArrayList<String> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}

	public ArrayList<String> getCommandsHistory() {
		return commandsHistory;
	}

	public void setCommandsHistory(ArrayList<String> commandsHistory) {
		this.commandsHistory = commandsHistory;
	}

}
