package fr.turtpol.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.turtpol.models.Function;
import fr.turtpol.models.Shell;

public class CommandChecker {
	
	/*
	 * 
	 * use to check the validity of a command
	 * 
	 */

	public static boolean checkCommandValidity(Shell shell) {

		shell.normalizeCommand();
		String cmd = shell.getText();

		if (cmd.length() == 0)
			return false;

		// check if player can go in function mode if he intend to (if name and
		// arguments are correct)
		if (cmd.startsWith("POUR") && !shell.isFunctionMode())
			return CommandChecker.checkFunctionEntryValidity(shell);

		// should be in CommandProcessor but it's easier to do it here
		// allow the player to stop writing a function and go back to normal
		// mode
		if (shell.isFunctionMode()) {
			if (cmd.startsWith("CANCEL")) {
				shell.setStartText("> ");
				shell.setFunctionMode(false);
				shell.setFunction(null);
				return true;
			}
			if (cmd.startsWith("FIN") || cmd.startsWith("END") || cmd.equals("")) {
				shell.setStartText("> ");
				shell.setFunctionMode(false);
				shell.getFunction().save();
				return true;
			}
		}
		if (!CommandChecker.checkRepeteValidity(shell))
			return false;
		if (!CommandChecker.checkBasicCommandValidity(shell)) {
			return false;
		}

		return true;
	}

	public static boolean checkFunctionEntryValidity(Shell shell) {
		String cmd = shell.getText();
		// chars who are not allow in the function name and arguments
		String nvchar = "#:[]()/*-+.0123456789";
		String[] arrayCmd = cmd.split(" ");

		// check if name is present
		if (arrayCmd.length <= 1) {
			shell.getHistory().add("ERROR: the name of the function is missing !");
			return false;
		}

		// check if function exist
		if (Function.exist(arrayCmd[1])) {
			shell.getHistory().add("ERROR: a function with this name already exist !");
			return false;
		}

		// check if contains unwanted char
		for (char c : nvchar.toCharArray()) {
			if (arrayCmd[1].contains(c + "")) {
				shell.getHistory().add("ERROR: The name of the function can't contain \"" + c + "\"");
				return false;
			}
		}

		// check if contains unwanted word
		String[] alreadyUsed = { "AV", "RE", "TD", "TG", "FCC", "LC", "BC", "VE", "CT", "MT", "REPETE", "STOP", "END",
				"FIN", "POUR" };
		for (String s : alreadyUsed) {
			if (arrayCmd[1].equals(s)) {
				shell.getHistory().add("ERROR: the word '" + s + "' is already used in Turtpol vocabulary !");
				return false;
			}
		}

		// arguments check
		ArrayList<String> params = new ArrayList<String>();
		for (int i = 2; i < arrayCmd.length; i++) {

			if (arrayCmd[i].length() == 1) {
				shell.getHistory().add("ERROR: word at index " + i + " isn't correct !");
				return false;
			}
			if (!arrayCmd[i].startsWith(":")) {
				shell.getHistory().add("ERROR: word at index " + i + " have to start with \":\"");
				return false;
			}
			if (params.contains(arrayCmd[i])) {
				shell.getHistory().add("ERROR: you can't have more than two parameters with the same name !");
				return false;
			}
			if (arrayCmd[i].replaceFirst(":", "").equals(arrayCmd[1])) {
				shell.getHistory().add("ERROR: an argument can't have the name of the function name !");
				return false;
			}

			String tmpParam = arrayCmd[i].replaceFirst(":", "");
			for (char c : nvchar.toCharArray()) {
				if (tmpParam.contains(c + "")) {
					shell.getHistory().add("ERROR: The parameter \"" + arrayCmd[i]
							+ "\" can't contain those character \"" + nvchar + "\", and he can only start with \":\"");
					return false;
				}
			}

			for (String s : alreadyUsed) {
				if (tmpParam.equals(s)) {
					shell.getHistory().add("ERROR: the word '" + s + "' is already used in Turtpol vocabulary !");
					return false;
				}
			}

			params.add(arrayCmd[i]);
		}
		shell.setFunction(new Function(arrayCmd[1], params));
		shell.setFunctionMode(true);
		shell.setStartText("   | ");

		return true;
	}

	public static boolean checkBasicCommandValidity(Shell shell) {
		String cmd = shell.getText().toUpperCase();
		// fcmd is a copy of cmd, use to replace function name and her arguments
		// with her instructions and values
		// we do a copy to avoid conflict in the for loop
		String fcmd = cmd;

		// check words who need an integer/double (player can't write double but
		// double is supported)
		if (cmd.contains("AV") || cmd.contains("RE") || cmd.contains("TD") || cmd.contains("TG")) {
			String[] astr = cmd.split(" ");

			for (int i = 0; i < astr.length; i++) {
				String str = astr[i];
				if (str.equals("AV") || str.equals("RE") || str.equals("TD") || str.equals("TG")) {

					try {
						if (i + 1 >= astr.length)
							throw new Exception();

						if (!astr[i + 1].startsWith(":"))
							Double.parseDouble(astr[i + 1]);

					} catch (Exception e) {
						String intType = "pixels";
						if (str.startsWith("T"))
							intType = "degrees";
						shell.getHistory().add("ERROR: Wrong use of " + str);
						shell.getHistory().add("use: " + str + " <number of " + intType + ">");
						shell.getHistory().add("word at index " + (i + 2) + " must be an integer !");
						return false;
					}

				}
			}

		}

		// check if the format of the color is correct
		if (cmd.contains("FCC")) {
			String[] astr = cmd.split(" ");

			for (int i = 0; i < astr.length; i++) {
				String str = astr[i];

				if (str.equals("FCC")) {

					if (i + 1 < astr.length) {
						str += " " + astr[i + 1];
					}

					String regxFCC = "FCC #[a-f_A-F_0-9]{6}";
					Pattern patt1 = Pattern.compile(regxFCC);
					Matcher m1 = patt1.matcher(str);

					if (!m1.find()) {
						shell.getHistory().add("ERROR: Wrong use of FCC");
						shell.getHistory().add("use: FCC <color in RGB format>");
						shell.getHistory().add("example: FCC #ff0000 for the red color");
						return false;
					}

				}
			}
		}

		String[] validWord = { "AV", "RE", "TD", "TG", "FCC", "LC", "BC", "VE", "CT", "MT", "REPETE", "[", "]",
				"STOP" };
		String[] validNumberWord = { "AV", "RE", "TD", "TG", "REPETE" };
		HashSet<String> setValidword = new HashSet<String>(Arrays.asList(validWord));
		HashSet<String> setValidNumberword = new HashSet<String>(Arrays.asList(validNumberWord));
		String[] astr = cmd.split(" ");

		// for each word in the command
		for (int i = 0; i < astr.length; i++) {
			String str = astr[i];

			// if the word is an integer, check if the word before is one of the
			// "validNumberWord" or a function
			if (Utils.isAnInteger(str) || Utils.isAnDouble(str)) {
				if (i - 1 >= 0) {
					if (!setValidNumberword.contains(astr[i - 1]) && !Function.exist(astr[i - 1])) {
						shell.getHistory()
								.add("ERROR: the word \"" + astr[i - 1] + "\" before \"" + str + "\" is not valid");
						return false;
					}else if(Function.exist(astr[i - 1])){
						Function fc = Function.loadFunction(astr[i-1]);
						int nbrArg = fc.getArguments().size();
						if(nbrArg == 0){
							shell.getHistory().add("ERROR: the function \"" + fc.getName() + "\" need 0 argument ! ");
							return false;
						}
					}
				} else {
					shell.getHistory().add("ERROR: a valid word is missing before \"" + str + "\"");
					return false;
				}
			}
			// if we are in function mode, check if the word is in the arguments
			// of the function
			if (shell.isFunctionMode() && str.startsWith(":")) {
				if (!shell.getFunction().getArguments().contains(str)) {
					shell.getHistory().add("ERROR: the argument \"" + str + "\" is not valid !");
					return false;
				}
			}

			if (str.startsWith("#")) {
				if (i - 1 >= 0) {
					if (!astr[i - 1].equals("FCC")) {
						shell.getHistory().add("ERROR: the word \"FCC\" is missing before \"" + str + "\"");
						return false;
					}
				} else {
					shell.getHistory().add("ERROR: the word \"FCC\" is missing before \"" + str + "\"");
					return false;
				}

			}
			// function check
			if (!setValidword.contains(str)) {
				if (Function.exist(str)) {

					Function fc = Function.loadFunction(str);
					int nbrArg = fc.getArguments().size();

					if (i + nbrArg >= astr.length) {
						shell.getHistory().add("ERROR: the function \"" + str + "\" need " + nbrArg + " argument(s) !");
						return false;
					}
					
					
					if (nbrArg > 0)
						for (int k = 1; k <= nbrArg; k++) {
							if (!Utils.isAnInteger(astr[i + k]) && !Utils.isAnDouble(astr[i + k])) {

								shell.getHistory().add(
										"ERROR: the arguments of the function \"" + str + "\" need to be integer !");
								return false;
							}

						}

					// replace the function name and her values with the
					// function instructions(replace variables with correct
					// values too) in the command
					String instructionConcat = "";
					for (String instruction : fc.getInstructions()) {
						String curInst = "";
						for (String word : instruction.split(" ")) {

							if (fc.getArguments().contains(word)) {
								int indexArg = fc.getArguments().indexOf(word) + 1;
								curInst += astr[i + indexArg] + " ";
							} else
								curInst += word + " ";
						}
						instructionConcat += curInst;
					}
					
				
					// build the function name + the values of her arguments
					// with that, it's easier to replace the function name &
					// args by "instructionConcat"
					String functionNA = fc.getName() + " ";
					for (int z = 1; z <= nbrArg; z++) {
						functionNA += astr[i + z] + " ";
					}
					functionNA = functionNA.substring(0, functionNA.length() - 1);
					// remove
					// the
					// last
					// space
					if (i + nbrArg != astr.length - 1) // if the function
														// doesn't have
														// arguments
						functionNA += " ";
					// replace in the cmd copy
					fcmd = fcmd.replaceAll(functionNA, instructionConcat);
			
					// we jump the check of the arguments values because we
					// already did it
					i += nbrArg;

					// we already did the # check
				} else if (!str.startsWith("#")) {
					// : is only available in function mode (or in POUR command)
					if (str.startsWith(":")) {
						if (!shell.isFunctionMode()) {
							shell.getHistory().add("ERROR: I don't understand the word \"" + str + "\"");
							return false;
						}
						if (!shell.getFunction().getArguments().contains(str)) {
							shell.getHistory().add("ERROR: I don't understand the word \"" + str + "\"");
							return false;
						}
						// here the only possibility of the command to be
						// correct is that the word need to be
						// an integer (or a double but the player can't write
						// double so we only check integer)
					} else {
						if (!Utils.isAnInteger(str)) {
							shell.getHistory().add("ERROR: I don't understand the word \"" + str + "\"");
							return false;
						}
					}
				}
			}
		}

		// we replace cmd with fcmd in shell text
		if (!fcmd.isEmpty())
			shell.setText(fcmd);

		return true;
	}

	public static boolean checkRepeteValidity(Shell shell) {
		String cmd = shell.getText().toUpperCase();
		if (cmd.contains("[") || cmd.contains("]") || cmd.contains("REPETE")) {

			// we first check if there is the right amount of [ and ] (need to
			// be equals)
			long occurancePo = cmd.chars().filter(c -> c == '[').count();
			long occurancePf = cmd.chars().filter(c -> c == ']').count();
			if (occurancePo != occurancePf) {
				char c = '[';
				if (occurancePo > occurancePf)
					c = ']';
				shell.getHistory().add("ERROR: one or more '" + c + "' is missing ! ");
				return false;
			}

			// regex: keep only [ and ]
			Pattern p = Pattern.compile("[^\\[\\]]");
			Matcher m = p.matcher(cmd);
			String as = m.replaceAll("");

			
			// and we check if we don't close more than we open
			boolean bracketValidity = true;
			int tmp = 0;
			for (char c : as.toCharArray()) {
				if (c == '[')
					tmp++;
				else
					tmp--;
				if (tmp < 0)
					bracketValidity = false;
			}

			if (!bracketValidity) {
				shell.getHistory().add("ERROR: Wrong use of REPETE");
				shell.getHistory().add("use: REPETE <number of time> [<instructions>]");
				return false;
			}

			String[] astr = cmd.split(" ");
			// number of time REPETE is present
			int occuranceRe = 0;

			// for each "REPETE" we check if the word after is an integer, and
			// the word after the integer is an "["
			// we don't check if the REPETE end with "]" because with the
			// verification we did before, it's supposed to be already the case
			for (int i = 0; i < astr.length; i++) {
				String str = astr[i];
				if (str.equals("REPETE")) {
					if (i + 2 >= astr.length) {
						shell.getHistory().add("ERROR: Wrong use of REPETE");
						shell.getHistory().add("use: REPETE <number of time> [<instructions>]");
						return false;
					}

					try {
						if (!shell.isFunctionMode())
							Integer.parseInt(astr[i + 1]);
					} catch (Exception e) {
						shell.getHistory().add("ERROR: word at index " + (i + 2) + " must be an integer !");
						return false;
					}

					if (!astr[i + 2].startsWith("[")) {
						shell.getHistory()
								.add("ERROR: statement at index " + (i + 3) + " must start with \'[' and end with ']'");
						return false;
					}

					occuranceRe++;
				}
			}

			if (occurancePo > occuranceRe) {
				shell.getHistory()
						.add("ERROR: the keyword \"REPETE\" is missing " + (occurancePo - occuranceRe) + " time.");
				return false;
			} else if (occurancePo < occuranceRe) {
				shell.getHistory().add("ERROR: at least " + (occuranceRe - occurancePo) + " brackets are missing.");
				return false;
			}

		}
		return true;
	}

}
