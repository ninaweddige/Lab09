package postfix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Postfix {
	
	/*constructor*/
	public Postfix() {
		
	}

	/** 
	 * @param pfx - A postfix expression
	 * @return - the double the postfix expression evaluates to
	 * @throws StackUnderflowException 
	 */
	public double evaluate(String pfx) throws StackUnderflowException {
		
		if (pfx == null) {return 0;}
		
		StackAsList<Double> operands = new StackAsList<Double>();
		
		double result = 0;
		
		for (int i = 0; i < pfx.length(); i++) {
			char t = pfx.charAt(i);
			//t is an operand -> push it to operands stack
			if(Character.isDigit(t)) {
				operands.push((double)(Character.getNumericValue(t)));
			//t is a hex digit
			}else if (isHexDigit(t)) {
				switch (t) {
					case 'A': operands.push(10.0);
							  break;
					case 'B': operands.push(11.0);
							  break;
					case 'C': operands.push(12.0);
							  break;
					case 'D': operands.push(13.0);
							  break;
					case 'E': operands.push(14.0);
							  break;
					case 'F': operands.push(15.0);
				}
			//t is whitespace -> do nothing, move on to next character
			}else if (t == ' '){
				
			//t is (maybe) an operator
			}else{
				double rhs = operands.pop();
				double lhs = operands.pop();
				switch (t) {
					case '+': 
						result = lhs + rhs;
						break;
					case '-':
						result = lhs - rhs;
						break;
					case '/':
						result = lhs / rhs;
						break;
					case '*':
						result = lhs * rhs;
						break;
					case '^':
						result = Math.pow(lhs, rhs);
						break;						
				}
				operands.push(result);
			}
		}
		return result;
	}
	
	public String infixToPostfix(String ifx) throws StackUnderflowException, IncorrectFormatException {
		
		String pfx = "";
		if (ifx.isBlank()) {return pfx;}
		
		HashMap<Character, Integer> precedences = new HashMap<>();
		precedences.put('(', 0);
		precedences.put('+', 1);
		precedences.put('-', 1);
		precedences.put('/', 2);
		precedences.put('*', 2);
		precedences.put('^', 3);
	
		StackAsList<Character> operators = new StackAsList<Character>();
		
		for (int i = 0; i < ifx.length(); i++) {
			Character t = ifx.charAt(i);
			
			if (Character.isDigit(t) || Character.isAlphabetic(t)) {
				pfx += t;
				
			}else if(t == '(') {
				operators.push(t);
				
			}else if(t == ')') {
				while(operators.peek() != '(') {
					pfx += operators.pop();
				}
				operators.pop(); //to pop the '(' off without adding it to the pfx string
			
			}else if (isOperator(t)) {
				//no operators on the stack yet -> add to stack
				if (operators.empty()) {operators.push(t);}
				else{
					//a is negative if t is higher precedence, 0 if same, positive if t is lower precedence
					int a = precedences.get(operators.peek()) - precedences.get(t);
					
					while(a >= 0) {
						pfx += operators.pop();
						if(!operators.empty() && operators.peek() != '(') {
							a = precedences.get(operators.peek()) - precedences.get(t);
						}else {
							a = -1;
						}
					}
					operators.push(t);
				}
			}
		}
		
		while(!operators.empty()) {
			pfx += operators.pop();
		}
		return pfx;
	}
	
	public void evaluateInfixFromConsole() throws StackUnderflowException, IOException, IncorrectFormatException {
		System.out.print("Enter an infix expression here: ");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String pfx = infixToPostfix(br.readLine());
		br.close();
		System.out.println("Result: " + evaluate(pfx));
	}
	
	public boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {return true;}
		else {return false;}
	}
	
	public boolean isHexDigit(char c) {
		if (c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F') {return true;}
		else {return false;}
	}
	
	/**
	 * @param infix - An infix expression
	 * @return - True if the expression is correctly formatted. May not contain alphabetic characters.
	 * @throws StackUnderflowException
	 */
	public boolean isFormatted(String infix) throws StackUnderflowException {
		if (infix.isBlank()) {return true;}

		String ifx = infix.replaceAll("\\s+",""); //remove all whitespace

		//length of string is even -> incorrectly formatted
		if(ifx.length() % 2 == 0) {return false;}
		
		char c = ifx.charAt(0);
		
		//Characters other than A - F -> incorrectly formatted
		if(Character.isAlphabetic(c)) {return false;}
		
		//String starts with ')' or an operator -> incorrectly formatted
		if(c == ')' || isOperator(c)) {return false;}
		
		//String ends with '(' or an operator -> incorrectly formatted
		c = ifx.charAt(ifx.length() - 1);
		if(c == '(' || isOperator(c)) {return false;}
		
		//Parentheses are not balanced -> incorrectly formatted
		if(!checkParentheses(ifx)) {return false;}
		
		for (int i = 0; i < ifx.length() - 1; i++) {
			c = ifx.charAt(i);
			char d = ifx.charAt(i + 1);
			
			//c is a digit or hex digit, d should b operator or ')'
			if(Character.isDigit(c) && (Character.isDigit(d) || d == '(')) {return false;}
			//c is '(', d should be digit or '('
			if(c == '(' && (isOperator(d) || d == ')')) {return false;}
			//c is ')', d should be operator or ')'
			if(c == ')' && (Character.isDigit(d) || d == '(')) {return false;}
			//c is an operator, d should be digit, hex digit, or '('
			if(isOperator(c) && (isOperator(d) || d == ')')) {return false;}
		}
		
		return true;
	}
	
	/**
	 * @param ifx - An infix expression
	 * @return - True if the expression is correctly formatted. May contain letters A-F.
	 * @throws StackUnderflowException 
	 */
	public boolean isFormattedHex(String infix) throws StackUnderflowException {
		if (infix.isBlank()) {return true;}

		String ifx = infix.replaceAll("\\s+",""); //remove all whitespace

		//length of string is even -> incorrectly formatted
		if(ifx.length() % 2 == 0) {return false;}
		
		char c = ifx.charAt(0);
		
		//Characters other than A - F -> incorrectly formatted
		if(Character.isAlphabetic(c) && !isHexDigit(c)) {return false;}
		
		//String starts with ')' or an operator -> incorrectly formatted
		if(c == ')' || isOperator(c)) {return false;}
		
		//String ends with '(' or an operator -> incorrectly formatted
		c = ifx.charAt(ifx.length() - 1);
		if(c == '(' || isOperator(c)) {return false;}
		
		//Parentheses are not balanced -> incorrectly formatted
		if(!checkParentheses(ifx)) {return false;}
		
		for (int i = 0; i < ifx.length() - 1; i++) {
			c = ifx.charAt(i);
			char d = ifx.charAt(i + 1);
			
			//c is a digit or hex digit, d should b operator or ')'
			if((Character.isDigit(c) || isHexDigit(c)) && (Character.isDigit(d) || d == '(')) {return false;}
			//c is '(', d should be digit or '('
			if(c == '(' && (isOperator(d) || d == ')')) {return false;}
			//c is ')', d should be operator or ')'
			if(c == ')' && (Character.isDigit(d) || isHexDigit(d) || d == '(')) {return false;}
			//c is an operator, d should be digit, hex digit, or '('
			if(isOperator(c) && (isOperator(d) || d == ')')) {return false;}
		}
		
		return true;
	}
	
	public boolean checkParentheses(String infix) throws StackUnderflowException {
		
		StackAsList<Character> parentheses = new StackAsList<Character>();
		
		for(int i = 0; i < infix.length(); i++) {
			char c = infix.charAt(i);
			
			if(c == '(') {parentheses.push(c);}
			
			if(c == ')') {
				if(parentheses.empty()) {return false;}
				else {parentheses.pop();}
			}
		}
		
		if (parentheses.empty()) {return true;}
		//if there are any opening parentheses left on stack -> not balanced
		else {return false;}
	}
		
}


