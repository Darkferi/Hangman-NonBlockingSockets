
//no change

package sockets.client.view;

import sockets.client.network.OutputHandler;


public class ConsoleOutput implements OutputHandler {                     
    
    private ClientConsoleThreadSafety consoleManager = new ClientConsoleThreadSafety();
    
    @Override
    public void messageOnScreen(String message) {
        consoleManager.println(message);
    }
    
}
