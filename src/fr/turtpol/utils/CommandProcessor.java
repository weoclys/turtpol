package fr.turtpol.utils;

import fr.turtpol.models.Action;
import fr.turtpol.models.Shell;
import fr.turtpol.models.Turtle;

public class CommandProcessor {

	/*
	 * 
	 * use to execute a command the processCommand automatically check the
	 * validity of the command
	 * 
	 */

	public static void processCommand(Turtle turtle, Shell shell) {

		if (CommandChecker.checkCommandValidity(shell)) {
			String cmd = shell.getText();
			// we don't want to execute the command when we are in function mode
			if (!shell.isFunctionMode())
				CommandProcessor.proccessCommand(cmd, 1, turtle);
			else {
				// we don't want POUR, FIN, END or a simple '' in the
				// instructions of the function
				if (!cmd.startsWith("POUR") && !cmd.startsWith("FIN") && !cmd.startsWith("END") && !cmd.equals("")) {
					shell.getFunction().getInstructions().add(cmd);
				}
			}
		}
	}

	public static void proccessCommand(String cmd, int repeat, Turtle turtle) {
		String[] acmd = cmd.split(" ");

		// how many time we need to repeat the current command
		for (int i = 0; i < repeat; i++) {
			for (int y = 0; y < acmd.length; y++) {
				// if the repete we are testing is in the top
				// can be multiple ex:
				// REPETE 4 [ repete 2 [...] ] av 27 REPETE 5 [repete 5 [...]]
				// repete on upper case are in the top
				// use to avoid to process repete inside the top repete,
				// we only process the tops repete
				if (acmd[y].equals("REPETE") && !CommandProcessor.isInTopRepeat(cmd, y)) {

					String repeteCmd = CommandProcessor.getTopRepeteCommandAimed(cmd,
							Utils.getLengthToTargetWord(acmd, y));
					int repeteRpt = Integer.parseInt(acmd[y + 1]);
					CommandProcessor.proccessCommand(repeteCmd, repeteRpt, turtle);
				}

				/*
				 * we don't want to process command who are inside a repete who
				 * are not in the top repete
				 * 
				 * ex: av 50 REPETE 4 [AV 50 TD 90] this function is going to
				 * process "av 50" and call herself but with cmd = "av 50 td 90"
				 * and repeat = "4" this mean the function already process the
				 * command 4 time
				 * 
				 */
				if (!CommandProcessor.isInTopRepeat(cmd, y)) {

					if (acmd[y].equals("AV")) {
						double value = Double.parseDouble(acmd[y + 1]);
						turtle.getActions().add(new Action(value));
					} else if (acmd[y].equals("RE")) {
						double value = Double.parseDouble(acmd[y + 1]);
						turtle.getActions().add(new Action(-value));
					} else if (acmd[y].equals("TD")) {
						double value = Double.parseDouble(acmd[y + 1]);
						turtle.getActions().add(new Action((float) value));
					} else if (acmd[y].equals("TG")) {
						double value = Double.parseDouble(acmd[y + 1]);
						turtle.getActions().add(new Action((float) -value));
					} else if (acmd[y].equals("FCC")) {
						String value = acmd[y + 1];
						turtle.getActions().add(new Action(value));
					} else if (acmd[y].equals("LC")) {
						turtle.getActions().add(new Action("path", false));
					} else if (acmd[y].equals("BC")) {
						turtle.getActions().add(new Action("path", true));
					} else if (acmd[y].equals("VE")) {
						turtle.getActions().add(new Action());
					} else if (acmd[y].equals("CT")) {
						turtle.getActions().add(new Action("turtle", false));
					} else if (acmd[y].equals("MT")) {
						turtle.getActions().add(new Action("turtle", true));
					} else if (acmd[y].equals("STOP")) {
						turtle.destroyWorkingPath();
						turtle.getActions().clear();
					}
				}
				/**/
			}
		}
	}

	
	public static boolean isInTopRepeat(String cmd, int index) {
		int pcount = 0;
		String[] acmd = cmd.split(" ");
		for (int i = 0; i < acmd.length; i++) {
			if (acmd[i].equals("[")) {
				if (pcount == 0) {
					pcount++;
					continue;
				}
				pcount++;
			}
			if (acmd[i].equals("]")) {
				pcount--;
				if (pcount == 0)
					break;
			}

			// if true this mean we don't have an unclosed '[' before the repete
			if (pcount > 0 && i == index) {
				return true;
			}
		}
		return false;
	}

	// get the command inside a repete
	public static String getTopRepeteCommandAimed(String cmd, int index) {
		int pcount = 0;
		char[] acmd = cmd.toCharArray();
		String repeteCmd = "";
		boolean copy = false;

		for (int i = index + 1; i < acmd.length; i++) {
			if (acmd[i] == '[') {
				if (pcount == 0) {
					// we can start to copy
					copy = true;
					pcount++;
					continue; // we don't want to count the first '['
				}
				pcount++;
			}
			if (acmd[i] == ']') {
				pcount--;
				// if it's the last ']', we stop
				if (pcount == 0)
					break;
			}
			if (copy)
				repeteCmd += acmd[i];
		}

		return repeteCmd;
	}

}
