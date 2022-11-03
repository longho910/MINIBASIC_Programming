

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class InterpreterFrame extends JFrame implements WindowListener {
   Hashtable<String, Double> symbolTable = new Hashtable<String, Double>();
   JPanel consoleDisplay = new JPanel();
   TextArea consoleDisplay2 = new TextArea(20, 40);
   String lineNumber, codeLine;
   String in;
   int insType;
   StringTokenizer tok, tok2;
   final boolean verbose = false;
   String lineNumber1, nextLineNumber1;
   boolean execFlag;
   Control c;
   String lineNumber2, nextLineNumber2;

   public InterpreterFrame(String startLine, Control fromC) {

      setTitle("CMD Console Output");
      setSize(200, 200);
      this.addWindowListener(this);
      setBounds(300, 300, 500, 500);
      this.add(consoleDisplay);
      consoleDisplay.add(consoleDisplay2);
      consoleDisplay2.setFont(new Font("Courier", Font.PLAIN, 20));
      consoleDisplay2.setBackground(Color.BLACK);
      consoleDisplay2.setForeground(new Color(236, 240, 205));
      setVisible(true);
      pack();
      c = fromC;

   }

   /**
    * Run the code displayed on JTextArea Screen (display).
    * 
    * @param startLine lineNumber first line to read.
    * @param nextLine  hashtable store line number (key) and next line number
    *                  (value).
    * @param code      hashtable store line number (key) and code instruction
    *                  (value).
    */

   public void runProgram(String startLine, Hashtable<String, String> nextLine, Hashtable<String, String> code) {
      consoleDisplay2.append("Running from line: " + startLine + "\n");
      consoleDisplay2.append("------------------------------------\n");
      String nextKey = startLine;
      execFlag = true;
      while (execFlag) {
         lineNumber = nextKey;
         codeLine = code.get(lineNumber).replace(" ", "");
         nextKey = nextLine.get(lineNumber);
         insType = instructionType();
         switch (insType) {
            case 0:
               consoleDisplay2.append("");
               break;
            case 1:
               proAssign();
               break;
            case 2:
               proPrint();
               break;
            case 3:
               String x = proGOTO();
               nextKey = x;
               break;
            case 4:
               String x2 = proIF();
               if (!x2.equals("NOTRANSFER"))
                  nextKey = x2;
               break;
            case 5:
               execFlag = false;
               consoleDisplay2.append("------------------------------------\n");
               consoleDisplay2.append("Program ended properly at line: " + lineNumber + "\n");
               break;
         }
      }
   }

   /**
    * Check the instruction type of each code line.
    * <p>
    * Have 6 types: comment, print, goto, if, assignment and end.
    * <p>
    * 
    * @return integer 0(comment), 1 (assignment), 2 (print), 3 (goto), 4 (if), 5
    *         (end)
    */
   public int instructionType() {
      int result = 0;
      codeLine.replace(" ", "");
      if (codeLine.substring(0, 2).equals("//")) {
         result = 0; // comment
      } else if (codeLine.substring(0, 3).equalsIgnoreCase("end")) {
         result = 5; // end
      } else if (codeLine.substring(1, 2).equals("=")) {
         result = 1; // assignment
      } else if (codeLine.substring(0, 5).equalsIgnoreCase("print")) {
         result = 2; // print
      } else if (codeLine.substring(0, 4).equalsIgnoreCase("goto")) {
         result = 3; // goto
      } else if (codeLine.substring(0, 2).equalsIgnoreCase("if")) {
         result = 4; // if
      }
      return result;
   }

   /**
    * Assignment method to assign expression to variable.
    * <p>
    * Complicated expression will be calculated and
    * assigned to variable.
    * <p>
    */
   public void proAssign() {
      Scanner in = new Scanner(codeLine);
      EvaluatorVar e = new EvaluatorVar(symbolTable);
      boolean execute = true;
      while (in.hasNext()) {
         // System.out.print("Enter Assignment > ");
         String input = in.nextLine();
         input = input.replaceAll("\\s+", ""); // skipping blanks

         Variable value = e.getAssignmentValue(input);

         symbolTable.put(value.LHS, value.RHS); // direct Double casting
         // consoleDisplay2.setText(value.LHS + " = " + value.RHS);
         System.out.println(value.LHS + "=" + value.RHS);
      }
   }

   /**
    * Print according to the code instruction.
    * <p>
    * If the variable is unidentified, print 0.0
    * <p>
    */
   public void proPrint() {
      String var = codeLine.substring(5, 6);
      if ((symbolTable.containsKey(var))) {
         consoleDisplay2.append((Double.toString(symbolTable.get(var))) + "\n");
         System.out.println("Print " + var + "=" + Double.toString(symbolTable.get(var)));
      } else {
         consoleDisplay2.append("0.0\n");
      }
   }

   /**
    * Go to line according to code instruction
    */
   public String proGOTO() {
      String line = codeLine.substring(4);
      return line;
   }

   /**
    * If the condition is met, go to the line according to the instruction
    * 
    * @return line the line to go to if the condition is met.
    */
   public String proIF() {
      int indexGOTO = codeLine.indexOf("goto");
      int indexOfClosedBracket = codeLine.indexOf(")");
      String var = codeLine.substring(3, 4);
      Double value;
      Double value1;
      if (symbolTable.containsKey(var)) {
         value = (symbolTable.get(var)); // value in symbolTable
         value1 = Double.parseDouble(codeLine.substring(5, indexOfClosedBracket)); // value on display

         if (value.compareTo(value1) == 0) {
            return codeLine.substring(indexGOTO + 4);
         } else {
            return "NOTRANSFER";
         }
      } else {
         return "NOTRANSFER";
      }
   }

   /**
    * Generate a hashtable to store line number (key) and code instruction (value).
    * 
    * @param s    codeLine including line number and code instruction.
    * @param code hashtable store line number (key) and code instruction (value).
    * @return hashtable the hashtable has just created
    */
   public Hashtable<String, String> generateCodeHash(String s, Hashtable<String, String> code) {

      Scanner sc = new Scanner(s);
      while (sc.hasNext()) {
         lineNumber = sc.next();
         codeLine = sc.nextLine();
         code.put(lineNumber, codeLine);
      }

      return code;

   }

   /**
    * Generate a hashtable to store line number (key) and next line number (value).
    * 
    * @param s        codeLine including line number and next line number.
    * @param nextLine hashtable store line number (key) and next line
    *                 number(value).
    * @return hashtable the hashtable has just created
    */
   public Hashtable<String, String> generateNextLineHash(String s, Hashtable<String, String> nextLine) {
      Scanner sc = new Scanner(s);
      lineNumber1 = sc.next();
      while (true) {

         String c = sc.nextLine();
         if (c.replaceAll(" ", "").equalsIgnoreCase("end")) {
            break;
         }
         nextLineNumber1 = sc.next();

         nextLine.put(lineNumber1, nextLineNumber1);
         lineNumber1 = nextLineNumber1;
      }

      return nextLine;
   }

   public class Variable {

      public String LHS;
      public double RHS;

      public Variable() {
      }

      public void setRHS(double v) {
         RHS = v;
      }

      public double getRHS(double v) {
         return RHS;
      }

      public void setLHS(String s) {
         LHS = s;
      }

      public String getLHS(double v) {
         return LHS;
      }
   }

   public class ExpressionTokenizerVar {

      private String input;
      private int start;
      private int end;
      final boolean verbose = true;

      /**
       * Constructs a tokenizer.
       * with vairation of storing a variable in a hash table
       * 
       * @param anInput the string to tokenizer
       */
      // public void resetExpressionTokenizer(String anInput) {
      public ExpressionTokenizerVar(String anInput) {
         input = anInput;
         start = 0;
         end = 0;
         nextToken(); // sets start and end for first token
      }

      /**
       * Peeks at the next token without consuming it.
       * 
       * @return the next token or null if there are no more tokens.
       */
      public String peekToken() {
         if (start >= input.length()) {
            if (verbose)
               System.out.println("PEEK TOKEN  with Lenght of " + (end - +start) + " at " + start + " String is "
                     + input.substring(start, end));
            return null;
         }
         return input.substring(start, end);
      }

      /**
       * Gets the next token and moves the tokenizer to the following token.
       * 
       * @return the next token or null if there are no more tokens.
       */
      public String nextToken() {
         String r = peekToken();
         start = end;
         if (verbose)
            System.out.println("next token  with start at " + start);
         if (start >= input.length()) {
            if (verbose)
               System.out.println("token delivered => " + r);
            return r;
         }
         if (nextTokenFloat(r))
            return r;
         if (nextTokenInteger(r))
            return r;
         if (nextTokenVar(r))
            return r;
         if (verbose)
            System.out.println("token delivered => " + r);
         end = start + 1;

         return r;
      }

      /**
       * Gets the next token and moves the tokenizer to the following token of an
       * integer
       * 
       * @return the next token or null if there are no more tokens.
       */
      public boolean nextTokenInteger(String r) {

         if (verbose)
            System.out.println("Digit at location  " + start);
         if (Character.isDigit(input.charAt(start))) {
            end = start + 1;
            while (end < input.length() &&
                  Character.isDigit(input.charAt(end)))
               end++;

            return true;
         } else
            return false;
      }

      /**
       * Gets the next token and moves the tokenizer to the following token of an
       * variable
       * 
       * @return the next token or null if there are no more tokens.
       */
      public boolean nextTokenVar(String r) {

         if (Character.isLetter(input.charAt(start))) {
            if (verbose)
               System.out.println("letter at location " + start);
            end = start + 1;
            while (end < input.length() &&
                  Character.isLetterOrDigit(input.charAt(end)))
               end++;

            return true;
         }

         else
            return false;
      }

      /**
       * Gets the next token and moves the tokenizer to the following token of an
       * integer
       * 
       * @return the next token or null if there are no more tokens.
       */
      public boolean nextTokenFloat(String r) {

         if (verbose)
            System.out.println("Digit at location " + start);
         if (Character.isDigit(input.charAt(start))) {
            end = start + 1;
            while (end < input.length() &&
                  Character.isDigit(input.charAt(end)))
               end++;
            if (end < input.length() && input.charAt(end) == '.') // check if it has a fraction
            {
               end++;
               while (end < input.length() &&
                     Character.isDigit(input.charAt(end)))
                  end++;
            }

            return true;
         } else
            return false;
      }

   }

   public class EvaluatorVar {
      final boolean verbose = true;
      private ExpressionTokenizerVar tokenizer;
      public Hashtable symbolTable; // Hashtable<String,Integer>;

      /**
       * Constructs an evaluator.
       * 
       * @param anExpression a string containing the expression to be evaluated
       */

      public EvaluatorVar(Hashtable<String, Double> symbolTable2) {
         symbolTable = symbolTable2;
      }

      /**
       * Evaluates the expression.
       * 
       * @return the value of the assignment expression
       */
      public Variable getAssignmentValue(String Expression) {
         Variable value = new Variable();
         value.RHS = 0;
         tokenizer = new ExpressionTokenizerVar(Expression);
         String next = tokenizer.peekToken();
         value.LHS = tokenizer.nextToken(); // determine variable name
         next = tokenizer.peekToken();
         if ("=".equals(next)) {
            tokenizer.nextToken(); // discard =
            value.RHS = getExpressionValue(); // determine variable value
         } else
            System.out.println("Assignment error");
         if (verbose)
            System.out.println("value delivered Assign " + value.RHS);
         return value;
      }

      /**
       * Evaluates the expression.
       * 
       * @return the value of the expression
       */
      public double getExpressionValue() {
         double value = getTermValue(); // go search for * / terms
         boolean done = false;
         while (!done) {
            String next = tokenizer.peekToken();
            if ("+".equals(next) || "-".equals(next)) {
               tokenizer.nextToken(); // Discard the "+" or "-"
               double value2 = getTermValue();
               if ("+".equals(next))
                  value += value2;
               else
                  value -= value2;
            } else
               done = true;
         }
         return value;
      }

      /**
       * Evaluates the next term in the expression.
       * 
       * @return the value of the term
       */
      public double getTermValue() {
         double value = getFactorValue(); // go search for ( )
         boolean done = false;
         while (!done) {
            String next = tokenizer.peekToken();
            if ("*".equals(next) || "/".equals(next)) {
               tokenizer.nextToken(); // Discard the "*" or "/"
               double value2 = getFactorValue();
               if ("*".equals(next))
                  value *= value2;
               else
                  value /= value2;
            } else
               done = true;
         }
         return value;
      }

      /**
       * Evaluates the next factor found in the expression.
       * 
       * @return the value of the factor
       */
      public double getFactorValue() {
         double value;
         String variable;
         String next = tokenizer.peekToken();
         if ("(".equals(next)) {
            tokenizer.nextToken(); // Discard the "("
            value = getExpressionValue(); // recursively go back to expression value
            tokenizer.nextToken(); // Discard the ")"
         } else {
            variable = tokenizer.nextToken();
            try {
               value = Double.parseDouble(variable);
            } catch (NumberFormatException e) { // try a set of Coded values for value in the hashtable
               if (symbolTable.containsKey(variable))
                  value = (double) (symbolTable.get(variable));
               else
                  value = 0;
            }
         }
         if (verbose)
            System.out.println("Factor value delivered " + value);
         return value;
      }
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

}
