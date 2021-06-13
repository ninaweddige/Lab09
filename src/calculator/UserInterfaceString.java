package calculator;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import postfix.*;

public class UserInterfaceString extends UserInterface implements ActionListener, ItemListener{

		private CalcEngineString calcString;
	    private Checkbox decimal;
	    private Checkbox hexadecimal;
	    private JPanel hexButtons;
		
	    //Constructor
	    public UserInterfaceString(CalcEngineString engine)
	    {
	        super(engine);
	        this.calcString = engine;
	        setVisible(false);
	        makeFrameString();
	        greyTheButtons();
	        frame.setVisible(true);
	    }
	    
	    //Makes the window
	    public void makeFrameString() {
	    	frame = new JFrame(calc.getTitle());
	    	JPanel contentPane = (JPanel)frame.getContentPane();
	    	
	    	contentPane.setLayout(new BorderLayout(8, 8));
	    	contentPane.setBorder(new EmptyBorder( 10, 10, 10, 10));
	    	
	    	//input field for the String expression
	    	display = new JTextField();
	    	Font f = new Font("serif", Font.PLAIN, 14);
	    	display.setFont(f);
	    	
	    	contentPane.add(display, BorderLayout.NORTH);
	    	
	    	//JPanel for the buttons
	    	JPanel buttons = new JPanel(new BorderLayout(8, 8));
	    	
	    	//JPanel for the hex buttons
	    	hexButtons = new JPanel(new GridLayout(2, 3));
	    	addButton(hexButtons, "A");
	    	addButton(hexButtons, "B");
	    	addButton(hexButtons, "C");
	    	
	    	addButton(hexButtons, "D");
	    	addButton(hexButtons, "E");
	    	addButton(hexButtons, "F");
	    	
	    	buttons.add(hexButtons, BorderLayout.NORTH);
	    	
	    	//JPanel for the digits and operators
	    	JPanel digits = new JPanel(new GridLayout(4, 4));
	    	addButton(digits, "7");
		    addButton(digits, "8");
		    addButton(digits, "9");
		    addButton(digits, "^");
		        
		    addButton(digits, "4");
	        addButton(digits, "5");
	        addButton(digits, "6");
     	    addButton(digits, "*");
		        
		    addButton(digits, "1");
		    addButton(digits, "2");
		    addButton(digits, "3");
		    addButton(digits, "+");
		        
		    addButton(digits, "0");
		    digits.add(new JLabel(""));
		    addButton(digits, "Clear");
		    addButton(digits, "-");
	    	
		    buttons.add(digits, BorderLayout.CENTER);
		    
	        //JPanel for equals button and checkbox for switching modes
	        JPanel enter = new JPanel(new GridLayout(3, 2));
	        
	        CheckboxGroup grp = new CheckboxGroup();
	        decimal = new Checkbox("decimal", grp, true);
	        hexadecimal = new Checkbox("hexadecimal", grp, false);
	        decimal.addItemListener(this);
	        hexadecimal.addItemListener(this);
	        
            addButton(enter, "=");
            
            enter.add(decimal);
            enter.add(hexadecimal);
            buttons.add(enter, BorderLayout.SOUTH);
            
            contentPane.add(buttons, BorderLayout.CENTER);
            frame.pack();
	    	
	    }
	    
	    //Executes when a button is pressed
	    public void actionPerformed(ActionEvent e) {
	    	String command = e.getActionCommand();
	    	
	    	if (command == "=") {
	    		String expression = display.getText();
	    		calcString.setDisplayString(expression);
	    		try {
					calcString.equals();
				} catch (StackUnderflowException e1) {
					e1.printStackTrace();
				}
	    	}
	    	
	    	else if (command == "Clear") {
	    		calcString.clear();
	    	}
	    	//Clears the display if a result is being shown in the display and a button is pressed,
	    	//then updates the display to show the new input.
	    	else if (!calcString.buildingDisplayValue){
	    		calcString.clear();
	    		calcString.setDisplayString(calcString.getDisplayString() + command);
	    	} else {
	    		calcString.setDisplayString(calcString.getDisplayString() + command);
	    	}
	    	redisplay();
	    }
	    
	    //Executes when a checkbox for switching mode is selected
	    public void itemStateChanged(ItemEvent e)
	    {
	        //switch from hexadecimal to decimal mode
	        if(e.getSource() == hexadecimal)
	        {
	            hexadecimal.setEnabled(false);
	            decimal.setEnabled(true);
	            calcString.setMode(true);
	        }
	        else if (e.getSource() == decimal)
	        {
	            decimal.setEnabled(false);
	            hexadecimal.setEnabled(true);
	            calcString.setMode(false);
	        }
	        greyTheButtons();
	        calcString.clear();
	        redisplay();
	    }
	    
	    //To make the Hexadecimal buttons gray and disable
	    public void greyTheButtons()
	    {
	        for (Component button : hexButtons.getComponents() )
	        {
	            if(button.isEnabled())
	            {
	                button.setEnabled(false);
	            }
	            else
	            {
	                button.setEnabled(true);
	            }
	        }
	    }
	    
	    public void redisplay() {
	    	display.setText(calcString.getDisplayString());
	    }
	    
	    
	    
	    
}
