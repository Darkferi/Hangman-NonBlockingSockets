/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.client.main;

import sockets.client.view.ClientInterpreter;

public class StartClient {
    
    public static final int SERVER_PORT = 8080;
    
    public static void main(String[] args){
            
            Thread userInterfaceThread = new Thread(new ClientInterpreter(SERVER_PORT));
            userInterfaceThread.start();

    }
}
