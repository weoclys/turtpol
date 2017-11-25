package fr.turtpol.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Function {

	/*
	 * @param:
	 * 	name: the name of the function 
	 * 	arguments: the arguments of the function (the names, not the values)
	 * 	instructions: the instruction of the function ( variables are not replace with values)
	 * 
	 */
	
	
	private String name;
	private ArrayList<String> arguments = new ArrayList<String>();
	private ArrayList<String> instructions = new ArrayList<String>();

	public Function(String name) {
		this.name = name;
	}

	public Function(String name, ArrayList<String> arguments) {
		this(name);
		this.arguments = arguments;
	}

	public Function(String name, ArrayList<String> arguments, ArrayList<String> instructions) {
		this(name, arguments);
		this.instructions = instructions;
	}

	
	/*
	 * save the function in a folder : functions
	 * 
	 * format :
	 * 
	 * 	[A-Za-z].* [A-Za-z]*\.txt
	 * 	function name with arguments separate with space
	 * 
	 * some words are ban ( words already used)
	 * 
	 * we can't have the same function different time with different arguments
	 * 
	 */
	public void save() {
		String dir = "functions";
		String fileName = this.getName();
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
		}
		if (Function.exist(this.getName()))
			return;

		
		for (String str : this.getArguments())
			fileName += " " + str.replaceAll(":", "");
		fileName += ".txt";

		File file = new File(dir + "/" + fileName);
		if (file.exists())
			return;
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (String str : this.getInstructions()) {
				bw.write(str);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static boolean exist(String name) {
		for (File file : new File("functions").listFiles()){
			String fname = file.getName().replaceAll(".txt", "");
			if(fname.contains(" "))
				fname = fname.split(" ")[0];
			if (fname.toLowerCase().equals(name.toLowerCase()))
				return true;
		}
		return false;
	}

	// replace variables with the right value
	public ArrayList<String> getExecutableInstructions(HashMap<String, Double> argumentsValues) {
		ArrayList<String> execInst = new ArrayList<String>();
		for (String instruction : this.getInstructions()) {
			String fInst = "";
			for (String str : instruction.split(" ")) {
				if (str.startsWith(":")) {
					fInst += argumentsValues.get(str) + " ";
				} else
					fInst += str + " ";
			}
			execInst.add(fInst);
		}

		return execInst;
	}

	public static Function loadFunction(String name) {
		String fileName = "functions/";
		ArrayList<String> arguments = new ArrayList<String>();

		//use to get the arguments of the functions
		for (File file : new File("functions").listFiles()) {
			if (file.getName().startsWith(name)) {
				fileName += file.getName();

				// if the function have arguments ...
				if (!file.getName().replaceAll(name + " ", "").replaceAll(".txt", "").split(" ")[0].equals(name))
					for (String str : file.getName().replaceAll(name + " ", "").replaceAll(".txt", "").split(" "))
						arguments.add(":" + str);
				break;
			}
		}

		if (!Function.exist(name))
			return null;

		ArrayList<String> instructions = new ArrayList<String>();

		// get the instructions in the .txt
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();
			while (line != null) {
				instructions.add(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Function(name, arguments, instructions);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getArguments() {
		return arguments;
	}

	public void setArguments(ArrayList<String> arguments) {
		this.arguments = arguments;
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}

	public void setInstructions(ArrayList<String> instructions) {
		this.instructions = instructions;
	}

}
