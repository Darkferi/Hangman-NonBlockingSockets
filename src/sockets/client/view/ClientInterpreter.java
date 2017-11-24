

//I removed the controller. So View passes data directly to Network without thread handling

package sockets.client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.client.network.ClientSocketHandling;

/**
 *
 * @author darkferi
 */
public class ClientInterpreter implements Runnable{
    
    private final int serverPort;
    private ConsoleOutput screenHandler;                                       
    private BufferedReader console;
    private static boolean ThreadStarted = false;
    private ClientSocketHandling clientSocket;

    
    public ClientInterpreter(int serverPort){
        this.serverPort = serverPort;
    }
    
    //I removed the controller. So View passes data directly to Network without thread handling
    @Override
    public void run(){
        clientSocket = new ClientSocketHandling();
        ThreadStarted = true;
        console = new BufferedReader(new InputStreamReader(System.in));
        String command;
        screenHandler = new ConsoleOutput();
        try {             
            clientSocket.connect(serverPort, screenHandler);
        } catch (IOException ex) {
            Logger.getLogger(ClientInterpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(ThreadStarted){
            try {
                
                command = console.readLine().toLowerCase();
                command = command.trim();
                
                if(command.startsWith("exit") && command.length()==4){
                    clientSocket.disconnect();
                    ThreadStarted = false;
                }
                
                else{
                    clientSocket.sendMessage(command);
                }
                           
            } catch (IOException e) {
                System.out.println("ClientVeiw > run() > IOException");
            }
        }      
    }
}
