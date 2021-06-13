package calculator;
import postfix.*;

public class CalcEngineString extends CalcEngine {

	private Postfix runner;
	private String displayString;
	private boolean isHex; //true for hex mode, false for decimal mode
	
	//Constructor
	public CalcEngineString() {
		runner = new Postfix();
		isHex = false;
	}
	
	public String getDisplayString() {
		return displayString;
	}
	
	public void setDisplayString(String a) {
		displayString = a;
	}
	
	public void setMode(boolean isHex) {
		this.isHex = isHex;
	}
	
	public void equals() throws StackUnderflowException {
		try {
			if(isHex) {
				//check that the displayString is correctly formatted
				if(runner.isFormattedHex(displayString)) {
					//convert the displayString from infix to postfix
					String pfx = runner.infixToPostfix(displayString);
					//evaluate the postfix expression
					double result = runner.evaluate(pfx);
					//update the displayString to hold the result as a HexString
					displayString = Integer.toHexString((int) result).toUpperCase();
					buildingDisplayValue = false;
				}else{
						throw new IncorrectFormatException("Incorrectly formatted infix expression.");
				}
			} else {
				if(runner.isFormatted(displayString)) {
					String pfx = runner.infixToPostfix(displayString);
					double result = runner.evaluate(pfx);
					displayString = Double.toString(result);
					buildingDisplayValue = false;
				} else {
					throw new IncorrectFormatException("Incorrectly formatted infix expression.");
				}				
			}
		}
		catch(IncorrectFormatException e) {
			displayString = "ERROR";
		}	
	}
	
	public void clear() {
		displayString = "";
		buildingDisplayValue = true;
	}
	
}
