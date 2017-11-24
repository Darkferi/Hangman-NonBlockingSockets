/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.server.start;

import sockets.server.network.GameServer;

/**
 *
 * @author darkferi
 */
public class StartServer {
   
    private static final int SERVER_PORT = 8080;
   
    public static void main(String[] args){
       
        GameServer gameServer = new GameServer();
        gameServer.serve(SERVER_PORT);
              
    }
    
}
