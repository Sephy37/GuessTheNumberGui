import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.security.KeyStore.PrivateKeyEntry;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.Random;
import javax.swing.Timer;

public class GuessTheNumber extends JFrame{
	
	
	private final int EASY_GAME_MODE = 15;
	private final int INTERMEDIATE_GAME_MODE = 10;
	private final int HARD_GAME_MODE = 5;
	private final int BUTTON_1 = 1;
	private final int BUTTON_2 = 2;
	private final int BUTTON_3 = 3;
	private final int WIDTH = 300;
	private final int HEIGHT = 30;
	private final int Y = 10;
	private final int X = 120;
	
	
	private int level;
	private int turns;
	private int randomNumber;
	private int buttonId;
	private String selectedGameMode;
	
	private final JFrame frame = new JFrame();
	
	private final JLabel headerTitleTextLabel;
	private final JLabel easyLevelLabel = new JLabel("");
	private final JLabel intermediateLevelLabel = new JLabel("");
	private final JLabel hardLevelLabel = new JLabel("");
	private final JLabel randomNumberLabel = new JLabel("");
	private final JLabel outputLabel = new JLabel("");
	private final JLabel howManyGuessLabel = new JLabel("");
	
	private JButton submitButton;
	private JButton easyLevelButton;
	private JButton intermediateLevelButton;
	private JButton hardLevelButton;
	private JButton resetFieldsButton;
	private JButton peekButton;
	
	private JTextField userGuessField;
	
	private Timer peekTimer;
	private Timer clearGuessLabel;
	
	private Timer howManyGuessesTimer;
	
	
	private GameStatus gameStatus;
	
	public GuessTheNumber() {
		gameStatus = GameStatus.GAME_ON;
		displayRandomNumber();
		setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        userGuessField = new JTextField(60);
        headerTitleTextLabel = new JLabel("Guess The Number");
        headerTitleTextLabel.setFont(new Font("Times new Roman",Font.BOLD,30));
        headerTitleTextLabel.setForeground(getForeGround());
        
        getContentPane().setBackground(Color.yellow);
        
        
        headerTitleTextLabel.setBounds(X,Y,WIDTH,HEIGHT);
        easyLevelLabel.setBounds(140,100,120,30);
        easyLevelLabel.setForeground(getForeGround());
        intermediateLevelLabel.setBounds(270,100,120,30);
        intermediateLevelLabel.setForeground(getForeGround());
        hardLevelLabel.setBounds(390,100,120,30);
        hardLevelLabel.setForeground(getForeGround());
        randomNumberLabel.setBounds(100,130,120,30);
        randomNumberLabel.setForeground(getForeGround());
        outputLabel.setBounds(220,130,140,30);
        outputLabel.setBackground(Color.BLUE);
        outputLabel.setOpaque(true);
        howManyGuessLabel.setBounds(200,130,300,130);
        
       
        userGuessField.setBounds(10,50,90,30);
        
        add(headerTitleTextLabel);
        add(easyLevelLabel);
        add(intermediateLevelLabel);
        add(hardLevelLabel);
        add(randomNumberLabel);
        add(outputLabel);
        add(howManyGuessLabel);
        add(userGuessField);
        
        submitButton = new JButton("Submit");
        easyLevelButton = new JButton("Easy");
        intermediateLevelButton = new JButton("Intermediate");
        hardLevelButton = new JButton("Hard");
        resetFieldsButton = new JButton("Reset");
        peekButton = new JButton("Peek");
        
        submitButton.setBounds(10,90,90,30);
        easyLevelButton.setBounds(110,50,90,30);
        intermediateLevelButton.setBounds(220,50,110,30);
        hardLevelButton.setBounds(350,50,90,30);
        resetFieldsButton.setBounds(10,170,90,30);
        peekButton.setBounds(10,130,90,30);
        
        add(submitButton);
        add(easyLevelButton);
        add(intermediateLevelButton);
        add(hardLevelButton);
        add(resetFieldsButton);
        add(peekButton);
        
        howManyGuessLabel.setEnabled(false);
        outputLabel.setEnabled(false);
        peekButton.setEnabled(false);
        
        howManyGuessesTimer = new Timer(5000,new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		howManyGuessLabel.setText("");
        		howManyGuessesTimer.stop();
        	}
        });
        
        clearGuessLabel = new Timer(2000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				outputLabel.setText("");
				clearGuessLabel.stop();
			}
		});
        clearGuessLabel.setRepeats(false);
        
        
        peekTimer = new Timer(3000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				randomNumberLabel.setText("");
				peekTimer.stop();
			}
		});
        peekTimer.setRepeats(false);
        
        submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gameStatus == GameStatus.GAME_ON) {
					startingPoint(e);
				}
				
			}
		});
        
        easyLevelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				turns = setGameDifficulty(e);
				turns = level;
			}
		});
        
        intermediateLevelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				turns = setGameDifficulty(e);
				turns = level;
				
			}
		});
        
        hardLevelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				turns = setGameDifficulty(e);
				turns = level;
				
			}
		});
        
        resetFieldsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resetButton();
				
			}
		});
        
        peekButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				peeks(e);
				peekButton.setEnabled(false);
				
			}
		});
	}
	
	private void startingPoint(ActionEvent e) {
		if(isInputEmpty()) {
			JOptionPane.showMessageDialog(frame,"FIELD CANNOT BE EMPTY");
		}else if(isGuessValidNumber()) {
			JOptionPane.showMessageDialog(frame,"INVALID INPUT! " + userGuessField.getText() + " IS NOT VALID INPUT! ");
		}else if(isNumberOutOfBounds()) {
			JOptionPane.showMessageDialog(frame, " INVALID NUMBER " + userGuessField.getText() + " IS NOT IN RANGE! ");
		}else {
			checkChoice(e);
		}
	}
	
	private int setGameDifficulty(ActionEvent e) {
		Object sourceObject = e.getSource();
		if(sourceObject == easyLevelButton) {
			buttonId = BUTTON_1;
			selectedGameMode = "easy";
		}else if(sourceObject == intermediateLevelButton) {
			buttonId = BUTTON_2;
			selectedGameMode = "intermediate";
		}else if(sourceObject == hardLevelButton) {
			buttonId = BUTTON_3;
			selectedGameMode = "hard";
		}else {
			buttonId = 0;
		}
		turns = processButton();
		lockInDifficulty();
		return buttonId;
	}
	
	private int processButton() {
		switch(buttonId) {
			case BUTTON_1:
				level = EASY_GAME_MODE;
				printOutNumberOfTurns(easyLevelLabel,EASY_GAME_MODE);
				break;
				
			case BUTTON_2:
				level = INTERMEDIATE_GAME_MODE;
				printOutNumberOfTurns(intermediateLevelLabel, INTERMEDIATE_GAME_MODE);
				break;
				
			case BUTTON_3:
				level = HARD_GAME_MODE;
				printOutNumberOfTurns(hardLevelLabel, HARD_GAME_MODE);
				break;
				
			default:
				System.out.println("SOMETHING WENT WRONG!");
		}
		return buttonId;
	}
	
	private void printOutNumberOfTurns(JLabel label,int numberOfTurns) {
		label.setText("" + numberOfTurns);
	}
	
	private void lockInDifficulty() {
		easyLevelButton.setEnabled(false);
		intermediateLevelButton.setEnabled(false);
		hardLevelButton.setEnabled(false);
	}
	
	private void displayRandomNumber() {
		randomNumber = getRandomNumber();
	}
	
	private int getRandomNumber() {
		return (int)(Math.random() * 20) + 1;
	}
	
	private void peeks(ActionEvent e) {
		Object source = e.getSource();
		if(source == peekButton && turns == EASY_GAME_MODE) {
			peek(easyLevelLabel);
		}else if(source == peekButton && turns == INTERMEDIATE_GAME_MODE) {
			peek(intermediateLevelLabel);
		}else if( source == peekButton && turns == HARD_GAME_MODE) {
			peek(hardLevelLabel);
		}
	}
	
	private int peek(JLabel label) {
		randomNumberLabel.setText(String.valueOf(randomNumber));
		level--;
		label.setText("" + level);
		System.out.println("Turns: " + level);
		System.out.println("Random Number: " + randomNumber);
		checkTurns();
		peekTimer.start();
		return level;
	}
	
	private void checkChoice(ActionEvent e) {
		Object source = e.getSource();
		switch(turns) {
			case EASY_GAME_MODE:
				if(source == submitButton) {
					checkGuess(easyLevelLabel,e);
				}
				break;
			case INTERMEDIATE_GAME_MODE:
				if(source == submitButton) {
					checkGuess(intermediateLevelLabel,e);
				}
				break;
				
			case HARD_GAME_MODE:
				if(source == submitButton) {
					checkGuess(hardLevelLabel,e);
				}
				break;
		}
	}
	
	/*private void checkGuess(JLabel label) {
		String inputString = userGuessField.getText().trim();
		try {
			int number = Integer.parseInt(inputString);
			if(number < randomNumber) {
				level--;
				level = enablePeekBtn();
				label.setText(String.valueOf(level));
				checkTurns();
				 upDateLabelStatus("GUESS IS TOO LOW!");
				 clearGuessLabel.start();
			}else if(number > randomNumber) {
				level--;
				enablePeekBtn();
				label.setText(String.valueOf(level));
				checkTurns();
				upDateLabelStatus("GUESS IS TOO HIGH");
				clearGuessLabel.start();
			}else {
				JOptionPane.showMessageDialog(frame,"You got it! ");
				howManyGuesses();
				howManyGuessesTimer.start();
				playAgain();
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(frame,"INVALID INPUT,TRY AGAIN");
		}
	}*/
	
	private void checkGuess(JLabel label,ActionEvent e) {
		String inputString = userGuessField.getText().trim();
		try {
			int number = Integer.parseInt(inputString);
			if(number < randomNumber) {
				upDateLabelStatus("GUESS IS TOO LOW!");
				upDateGame(label,e);
			}else if( number > randomNumber) {
				upDateLabelStatus("GUESS IS TOO HIGH!");
				upDateGame(label,e);
			}else {
				winGame();
			}
		}catch(NumberFormatException E) {
			JOptionPane.showMessageDialog(frame,"INVALID INPUT!");
		}
	}
	
	private void upDateGame(JLabel label,ActionEvent e) {
		level--;
		enablePeekBtn();
		label.setText(String.valueOf(level));
		checkTurns();
		clearGuessLabel.start();
	}
	
	/*private int enablePeekBtn(){
        if(level == 2){
             peekButton.setEnabled(true);   
        }
        return level;    
    } */   
	
	private void winGame() {
		JOptionPane.showMessageDialog(frame,"YOU GOT IT!");
		howManyGuesses();
		howManyGuessesTimer.start();
		playAgain();
	}
	
	
	
	private void enablePeekBtn() {
		if(selectedGameMode.equals("easy") && level == 12 ||
		  (selectedGameMode.equals("intermediate") && level == 5)||
		  (selectedGameMode.equals("hard") && level == 2)){
		     peekButton.setEnabled(true);
		  }     
	}
	
	private void howManyGuesses() {
		int numberOfGuesses = turns - level;
		howManyGuessLabel.setText("It took you " + numberOfGuesses + " guess to guess the number!");
		System.out.println(numberOfGuesses);
	}
	
	private void upDateLabelStatus(String message) {
		outputLabel.setText(message);
	}
	
	private void playAgain() {
	  int askPlayAgain = JOptionPane.showConfirmDialog(frame,"Do you want to play again? ");
		if(askPlayAgain == JOptionPane.YES_OPTION || askPlayAgain == JOptionPane.CANCEL_OPTION) {
			resetButton();
		}else if(askPlayAgain == JOptionPane.NO_OPTION){
			dispose();
		}
	}
	
	private void checkTurns() {
		if(level == 0) {
			gameStatus = GameStatus.GAME_OVER;
			JOptionPane.showMessageDialog(frame,"OUT OF TURNS! ");
			resetButton();
			randomNumber = getRandomNumber();
		}
	}
	
	private boolean isNumberOutOfBounds() {
		String inputString = userGuessField.getText().trim();
		int guess = Integer.parseInt(inputString);
		return guess <= 0 || guess > 20;
	}
	
	private boolean isGuessValidNumber() {
		String inputString = userGuessField.getText().trim();
		try {
			int guess = Integer.parseInt(inputString);
		}catch(NumberFormatException e) {
			return true;
		}
		return false;
	}
	
	private boolean isInputEmpty() {
		String inputString = userGuessField.getText().trim();
		return inputString.isEmpty();
	}
	
	private void resetButton() {
		userGuessField.setText("");
		easyLevelLabel.setText("");
		intermediateLevelLabel.setText("");
		hardLevelLabel.setText("");
		easyLevelButton.setEnabled(true);
		intermediateLevelButton.setEnabled(true);
		hardLevelButton.setEnabled(true);
		peekButton.setEnabled(false);
		displayRandomNumber();
		gameStatus = GameStatus.GAME_ON;
	}
	
	private Color getForeGround() {
		return Color.RED;
	}
	
	
	
	
	
	

}
