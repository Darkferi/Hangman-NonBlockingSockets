
package sockets.server.controller;

import java.io.IOException;
import static java.lang.Character.isLetter;
import sockets.server.model.Hangman;
import sockets.server.model.RandomWord;

/**
 *
 * @author darkferi
 */
public class ServerController {
    
    private boolean isStarted = false;
    private boolean isMiddle = false;
    private boolean flag = false;
    private Hangman hangmanGame;
    private StringBuilder wordScreen;
    private static final char SHOW_SCREEN = '_';
    private char guessChar;
    private String dataToPlayer;
    private static final String STRING_SEPERATOR = "#";
    
    
    
    public String intro () throws IOException{
        return (new Hangman().showIntro());
    }
    
    
    public String checkUserInput(String userInput) throws IOException{
                if(userInput.startsWith("start") && userInput.length()==5){
                   
                    if (!isStarted){
                        if(!isMiddle){
                            RandomWord findWord = new RandomWord();
                            String chosenWord = findWord.getTheWord();
                            wordScreen = new StringBuilder(chosenWord);

                            for (int i = 0; i < wordScreen.length(); i++){
                                wordScreen.setCharAt(i, SHOW_SCREEN);                                   
                            }
                            
                            hangmanGame = new Hangman(chosenWord, chosenWord.length());
                            dataToPlayer = "\n---------------------------------------------------------------------------\n";
                            
                            dataToPlayer += hangmanGame.showState(wordScreen);          
                            isStarted = true;
                        }
                        else{
                            dataToPlayer = hangmanGame.showState(wordScreen);
                            isMiddle = false;
                            isStarted = true;
                        }
                    }
                    
                                       
                    else{
                        if (!isMiddle){
                            dataToPlayer = "\nYou started the game already!!!\n";
                            dataToPlayer += hangmanGame.showState(wordScreen); 
                        }
                    }
                
                                   
                }
 
                else if((userInput.startsWith("rule")) && (userInput.length() == 4) && (!isMiddle)){
                    if(isStarted == true){
                        dataToPlayer = hangmanGame.showRules();
                        dataToPlayer += hangmanGame.showState(wordScreen);
                    }
                    else{
                        dataToPlayer =  new Hangman().showRules();
                    }
                } 
                
                else if(userInput.startsWith("word ") && (!isMiddle)){
                    if(isStarted == true){
                        userInput = userInput.substring(5).trim();
                        
                        dataToPlayer = hangmanGame.hangmanCheckWord(userInput, wordScreen);
                        String[] temp = dataToPlayer.split(STRING_SEPERATOR);
                        dataToPlayer = temp[0];
                        wordScreen = new StringBuilder(temp[1]);
                        
                        if(!hangmanGame.gameWinLoseState){
                            dataToPlayer += "\n";
                            dataToPlayer += hangmanGame.showState(wordScreen);
                        }
                        else{
                            dataToPlayer += "\nIf you want to continue write START command. Unless write EXIT...\n ";
                            isMiddle = true;
                            isStarted = false;
                            hangmanGame.gameWinLoseState = false;
                        }
                    }
                    else{
                        dataToPlayer = "\nYou have not started the game yet. First write START command.\n ";
                    }
                }
                else{
                    if(userInput.length()==0){//ok
                        dataToPlayer = "\nYou didn't type anything or you are typing too fast!!!!\n";
                    }
                    else{
                        guessChar = userInput.charAt(0);
                        if( (userInput.length()==1) && (isLetter(guessChar)) && (isStarted == true) && (!isMiddle) ){/////////////////problem
                            
                            dataToPlayer = hangmanGame.hangmanCheckChar(guessChar, wordScreen);
                            if(dataToPlayer.indexOf(STRING_SEPERATOR)!= -1){
                                String[] temp = dataToPlayer.split(STRING_SEPERATOR);
                                dataToPlayer = temp[0];
                                wordScreen = new StringBuilder(temp[1]);
                            }
                            else{
                                wordScreen = new StringBuilder(dataToPlayer);
                                flag = true;
                            }
                            
                            if(!hangmanGame.gameWinLoseState && !flag){
                                dataToPlayer += "\n";
                                dataToPlayer += hangmanGame.showState(wordScreen);
                            }
                            else if(!hangmanGame.gameWinLoseState && flag){
                                dataToPlayer = "\n";
                                dataToPlayer += hangmanGame.showState(wordScreen);
                                flag = false;
                            }
                            else{
                                dataToPlayer += "\nIf you want to continue write START command. Unless write EXIT...\n";
                                isMiddle = true;
                                isStarted = false;
                                hangmanGame.gameWinLoseState = false;
                            }
                        }
                       
                        else if ( (userInput.length()==1) && (!isLetter(guessChar))&& (isStarted == true) && (!isMiddle)){//ok
                            dataToPlayer = "\nInvalid character! You should try a letter between a-z! Try agein...\n";
                            dataToPlayer += hangmanGame.showState(wordScreen);
                        }
                        else{//ok
                            dataToPlayer = "\nInvalid command! Try agein...\n";
                            if ((isStarted) && (!isMiddle)){
                                dataToPlayer += hangmanGame.showState(wordScreen);
                            }
                        }
                    }
                }  
                
        return dataToPlayer;           

    }
    
}
