

/*
 * Fall 2022 COSC 20203
 * Lab 3: Program execution through MINIBASIC Programming Language
 * @author: Peter Ho <hoquanglong0910@gmail.com>
 * @credit: some codes are take from Dr Sanchez. 
 * @version JDK 18.0.1.1
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Control extends View implements ActionListener, WindowListener

{

	Hashtable<String, String> nextLine = new Hashtable<String, String>();
	Hashtable<String, String> code = new Hashtable<String, String>();
	String startLine;
	InterpreterFrame myInterpreter;
	final boolean verbose = true;

	/**
	 * @param args[]
	 */
	public static void main(String args[]) throws IOException {
		new Control();
	}

	/**
	 * Add action listener for button
	 */
	public Control() {
		read.addActionListener(this);
		save.addActionListener(this);
		execute.addActionListener(this);
		reset.addActionListener(this);
		this.addWindowListener(this);

	}

	/**
	 * Add action performed for button
	 */
	public void actionPerformed(ActionEvent e) {
		String whichButton = e.getActionCommand(); // determines which button generated the event event
		if (whichButton.equals("Read")) {
			processRead(getFileName(true));
		}

		if (whichButton.equals("Save")) {
			processSave(getFileName(false));

		}

		if (whichButton.equals("Run")) {

			processExecute();
		}

		if (whichButton.equals("Reset"))
			processReset();
	}

	/**
	 * Execute method to call myInterpreter to run.
	 */
	public void processExecute() {
		Scanner sc1 = new Scanner(display.getText());
		startLine = sc1.next();
		if (verbose)
			System.out.println("Processing Execute" + startLine);
		myInterpreter = new InterpreterFrame(startLine, this);
		myInterpreter.runProgram(startLine, myInterpreter.generateNextLineHash(display.getText(), nextLine),
				myInterpreter.generateCodeHash(display.getText(), code));
		// myInterpreter.Test();

	}

	/**
	 * Read file which is chosen from the dialog to JTextArea display.
	 * 
	 * @param fileName file fileName want to read.
	 */
	public void processRead(String fileName) {
		try {
			BufferedReader in;
			in = new BufferedReader(new FileReader(fileName));
			String line, word;

			// reading line by line
			while ((line = in.readLine()) != null) {
				display.append(line + "\n");
				// display.setText(line + "\n"); (override)

			}
		} catch (Exception e) {
			display.setText("We got an ERROR: " + e);

		}
		if (verbose)
			System.out.println("Read Method goes here ");
	}

	/**
	 * Save the text on JTextArea screen (display) to a new file
	 * 
	 * @param fileName file file want to save
	 */
	public void processSave(String fileName) {
		// Try block to check for exceptions
		try {

			// Step 1: Create an object of BufferedWriter
			BufferedWriter f_writer = new BufferedWriter(new FileWriter(fileName));

			// Step 2: Write text(content) to file
			String text = display.getText();
			f_writer.write(text);

			// Step 3: Printing the content inside the file
			// on the terminal/CMD
			System.out.print(text);

			// Step 4: Display message showcasing
			// successful execution of the program
			System.out.print(
					"File is created successfully with the content.");

			// Step 5: Close the BufferedWriter object
			f_writer.close();
		}

		// Catch block to handle if exceptions occurs
		catch (IOException e) {

			// Print the exception on console
			// using getMessage() method
			System.out.print(e.getMessage());
		}
	}

	/**
	 * Reset all the texts on the JTextArea Screen (display).
	 */
	public void processReset() {
		if (verbose)
			System.out.println("Reset Method ");
		display.setText("");
		if (myInterpreter != null)
			myInterpreter.dispose();
	}

	public String getFileName(boolean opt) {
		// Put up a file dialog to allow the user to select an input file

		FileDialog myFD;
		if (opt)
			myFD = new FileDialog(this, "Input Data", FileDialog.LOAD);
		else
			myFD = new FileDialog(this, "Save Data", FileDialog.SAVE);
		myFD.setDirectory(".");
		myFD.setVisible(true);
		String name = myFD.getFile();
		String dir = myFD.getDirectory();

		return dir + name; // if path desired
	}

	/**
	 * Public Methods
	 * Used to control the windows
	 **/
	public void windowClosing(WindowEvent e) {
		dispose();
		System.exit(0);
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

} /// end Frame1
