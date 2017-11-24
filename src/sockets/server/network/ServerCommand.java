/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author darkferi
 */
public class ServerCommand implements Runnable{
    private ServerSocket serverSocket;
    private Socket playerSocket;

    private BufferedReader serverConsole;
    
    public ServerCommand(ServerSocket serverSocket, Socket playerSocket){
        this.serverSocket = serverSocket;
        this.playerSocket = playerSocket;
    }
    
    @Override
    public void run(){
        serverConsole = new BufferedReader(new InputStreamReader(System.in));
        String serverCmd;
        for(;;){
            try {
                serverCmd = serverConsole.readLine();
            
                if(serverCmd.equalsIgnoreCase("shutdown")){
                    serverSocket.close();
                    playerSocket.close();
                }
            }catch (IOException ex) {
                    System.out.println("Server >> ServerCommand >> IOException when shutdown!!");
            }
        }
    }
    
}
