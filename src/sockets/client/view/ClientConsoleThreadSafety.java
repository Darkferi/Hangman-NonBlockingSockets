
//no change

package sockets.client.view;


public class ClientConsoleThreadSafety {
    
    synchronized void println(String consoleOutput) {
        System.out.println(consoleOutput);
    }
    
}
