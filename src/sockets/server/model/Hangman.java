

package sockets.server.model;

import java.io.IOException;

/**
 *
 * @author darkferi
 */
public class Hangman {
    
    private int score = 0;
    private int attempNo;
    private String chosenWord;
    private static final char SHOW_SCREEN = '_';
    private static final char SPACE = ' ';
    private String outputInScreen;
    public boolean gameWinLoseState = false;
    private String s;
    private static final String STRING_SEPERATOR = "#";
    
    
    public Hangman(){
        this.chosenWord = " ";
        this.attempNo = 0;
    }

    public Hangman(String chosenWord, int attempNo){
        this.chosenWord = chosenWord;
        this.attempNo = attempNo;
    }
    
    private void IncreaseScore(){
        score++;
    }
    
    private void DecreaseScore(){
        score--;
    }
    
    
    public String hangmanCheckChar(char c, StringBuilder screenWord) throws IOException{
       
        boolean changed = false;
        boolean win = false;
        for(int i=0; i < chosenWord.length(); i++){
           if(c==chosenWord.charAt(i)){
               if(screenWord.charAt(i)==SHOW_SCREEN){
                   screenWord.setCharAt(i, c);
                   changed = true;
               }
               else{
                   return screenWord.toString(); 
               }
           }
        }
        
        if(changed == true){
            if(hangmanWin(screenWord)){
                IncreaseScore();
                outputInScreen = chosenWord.substring(0, 1).toUpperCase() + chosenWord.substring(1);
                s = "\nCongrats! The correct word was: " + outputInScreen + "\nYour Score: " + score + "\n";
                screenWord = selectNextWord(screenWord);
                s += STRING_SEPERATOR + screenWord.toString();
            }
            else{
                s = "Bravo!\n";
                s += STRING_SEPERATOR + screenWord.toString();
            }
        }
        else{
            s = "Wrong!\n";
            attempNo--;
            if (attempNo == 0){
                DecreaseScore();
                gameWinLoseState = true;
                outputInScreen = chosenWord.substring(0, 1).toUpperCase() + chosenWord.substring(1);
                s += "\nYou Lost! The correct word was: " + outputInScreen + "\nYour Score: " + score + "\n";
                screenWord = selectNextWord(screenWord);
                s += STRING_SEPERATOR + screenWord.toString();
            }
            else{
                s += STRING_SEPERATOR + screenWord.toString();
            }
        }
        return s;
    }
    
    
    
    public String hangmanCheckWord(String userInput, StringBuilder screenWord) throws IOException{
        
        /*for(;;){
           //for testing responsive user interface 
        }*/
        
        if (userInput.equals(chosenWord.trim())){
            IncreaseScore();
            gameWinLoseState = true;
            outputInScreen = chosenWord.substring(0, 1).toUpperCase() + chosenWord.substring(1);
            
            screenWord = selectNextWord(screenWord);
            s = "\nCongrats! The correct word was: " + outputInScreen + "\nYour Score: " + score + "\n";
            s += STRING_SEPERATOR + screenWord.toString();
        }
            
        else{
            s = "Wrong!\n";
            attempNo--;
            if (attempNo == 0){
                DecreaseScore();
                gameWinLoseState = true;
                outputInScreen = chosenWord.substring(0, 1).toUpperCase() + chosenWord.substring(1);
                s += "\nYou Lost! The correct word was: " + outputInScreen + "\nYour Score: " + score + "\n";
                screenWord = selectNextWord(screenWord);
                s += STRING_SEPERATOR + screenWord.toString();
            }
            else{
                s += STRING_SEPERATOR + screenWord.toString();
            }
        }
 
        return s;
    }
    
    
    private boolean hangmanWin(StringBuilder screenWord){
        int found = screenWord.toString().indexOf(SHOW_SCREEN);
        if(found == -1){
            gameWinLoseState = true;
            return true;
        }
        return false;
    }
    
    private StringBuilder selectNextWord(StringBuilder screenWord){
        RandomWord newWord = new RandomWord();
        chosenWord = newWord.getTheWord();
        attempNo = chosenWord.length();
        screenWord = new StringBuilder(chosenWord);
        for (int i = 0; i < screenWord.length(); i++){
            screenWord.setCharAt(i, SHOW_SCREEN);
        }
        return screenWord;
    }
    
    
    public String showState(StringBuilder wordScreen) throws IOException{                                          
        StringBuilder outputBuilder = new StringBuilder(wordScreen.toString() + wordScreen.toString());
        
        //outputInScreen;
        for (int i = 0; i < wordScreen.length(); i++){
                outputBuilder.setCharAt(2*i, wordScreen.charAt(i));
                outputBuilder.setCharAt(2*i+1, SPACE);
            }
        outputInScreen = outputBuilder.toString();
        outputInScreen = outputInScreen.substring(0,1).toUpperCase() + outputInScreen.substring(1);
        s = "Word:\t" + outputInScreen + "\t\tAttemp: " + attempNo+ "\t\tScore: " + score + " \n";
        return s;
    }
    
    
   
    
    public String showIntro() throws IOException{                                          
        s = "-----------------------------------------------"
                    + "---------------------------------------------------------"
                    + "\nHey!!!\nWelcome to the Hangman Game (Country Version). "
                    + "This game only accepts characters and four commands.\n"
                    + "Remember, the words are only chosen from countries in the world\n\n"
                    + "Commands: \n" +
                    "1) START --> if you want to start the game.\n" +
                    "2) WORD --> if you want to guess the whole word (ex. WORD Iran).\n" + 
                    "3) RULE --> if you want to know about the rules of the game.\n" + 
                    "4) EXIT --> if you want to quit the game.\n\n" + 
                    "If you want to start the game write START command...\n";
        
        return s;
    }
    
    
    public String showRules() throws IOException{                                          
       s = "\n----------------------------------------------------------------------------------------------\n" +
            "Rules:\n" +
            "First you will be given new country to guess and you will try to guess the chosen word\n" +
            "by suggesting letters occurring in the word (one letter at a time), or by suggesting \n" +
            "the whole word. you are only allowed as many failed attempts as there are letters in \n" +
            "the word. A failed attempt is a suggestion of a letter not occurring in the word, or of \n" +
            "the wrong word. \n" +
            "If the you suggest a letter that occurs in the word, the letter is placed in all its \n" +
            "positions in the word; otherwise the number of allowed failed attempts is decreased by \n" +
            "one. At any time you are allowed to guess the whole word. \n" +
            "You win when the word is completed using single letters, or the whole word is guessed \n" +
            "correctly. You lose when the counter of allowed failed attempts reaches zero.\n" +
            "----------------------------------------------------------------------------------------------\n\n";
        return s;
    }    
}
