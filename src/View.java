



import java.io.IOException;
import java.awt.*;
import java.io.*;
import java.net.*; 
import java.awt.event.*;
import javax.swing.*; 
import java.util.*;


public class View extends JFrame 

{   final boolean verbose = true;
    JButton read = new JButton("Read");
    JButton save = new JButton("Save");
    JButton execute = new JButton("Run");
    JButton reset = new JButton("Reset");
	JTextArea display = new JTextArea(30,40);
	JScrollPane jsp = new JScrollPane(display);
    JPanel  displayButtons = new JPanel(new GridLayout(1,4));
    protected Color tcuC = new Color(77,25,121);
	protected Font fb = new Font("Arial", Font.BOLD,16);
    public static void main(String args[]) throws IOException
	  { new View(); }
	public View()
	{   setLayout(new BorderLayout());
	    displayButtons.setBackground(tcuC);
	    display.setBackground(new Color(250,238,205));
	    jsp.setBackground(tcuC);
		setBounds(200,200,600,300);
		displayButtons.add(read);displayButtons.add(execute);
		displayButtons.add(save);displayButtons.add(reset);
		add(displayButtons,BorderLayout.NORTH);
		add(jsp,BorderLayout.CENTER);
		setTitle("Basic Interpreter");
		setForeground(tcuC);
    	setVisible(true); pack();
	}


} ///  end Frame1
