/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package sockets.server.network;
package sockets.server.model;

import java.io.*;
import java.util.Random;

/**
 *
 * @author darkferi
 */
public class RandomWord {
    
    private static final String FILE_PATH = "input.txt"; 
    private static final String ENTER = "\n";
    private boolean readOK = true;
    
    public String getTheWord(){
        
        String[] words;
        words = this.readFile();
        
        Random random = new Random();
        int randomWordIndex = random.nextInt(words.length);
               
        return words[randomWordIndex].toLowerCase();
    }
    
    
    public String[] readFile(){
        try{
            //Read file and extract all the words
            BufferedReader fromFile = new BufferedReader(new FileReader(FILE_PATH));
            String content = fromFile.readLine();
            String temp;
            while(readOK){
                if((temp = fromFile.readLine()) == null)
                    readOK = false;
                else{
                    content = content + ENTER + temp; 
                }
            }
            String[] words = content.split(ENTER);
            
            return words;
           
        }catch (IOException e){
            System.out.println("Server(FileRead): IOException happened!!!");
            return null;
        }
    }
}
