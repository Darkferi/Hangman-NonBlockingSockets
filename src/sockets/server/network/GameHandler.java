
package sockets.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;
import sockets.server.controller.ServerController;


public class GameHandler implements Runnable{
    private static final int MAX_DATA_LENGTH = 8192; 
    private String recvdString;
    private final SocketChannel playerChannel;
    private String s;
    private ByteBuffer buffer;
    private boolean  connected = false;
    private ServerController serverController;
    private ByteBuffer fromClientBuffer = ByteBuffer.allocate(MAX_DATA_LENGTH);
    private boolean completedProcess = false;
    private boolean firstTime = true;
    private String dataToPlayer;
    
    public GameHandler(SocketChannel playerChannel){
        this.playerChannel = playerChannel;
        serverController = new ServerController();
        this.connected = true;
    }
    
    @Override
    public void run(){
        String userInput = recvdString;
        try {
            if(firstTime){
                dataToPlayer = serverController.intro();
                firstTime = false;
                buffer = ByteBuffer.wrap(dataToPlayer.getBytes());
                playerChannel.write(buffer);
            }
            while(connected && userInput != null && !completedProcess){
                dataToPlayer = serverController.checkUserInput(userInput);
                buffer = ByteBuffer.wrap(dataToPlayer.getBytes());
                playerChannel.write(buffer);
                completedProcess = true;
            }
        } catch(IOException e){
                System.out.println("GameHandler: IOException occured");
        } 
    }
     
    /*
    sendMessage will be used just for Introduction and Welcome Message
    */
    void sendMsg() throws IOException {
        completedProcess = false;
        ForkJoinPool.commonPool().execute(this);
    }
    
    
    void recvMsg() throws IOException {
        fromClientBuffer.clear();
        if (playerChannel.read(fromClientBuffer) == -1) {
            System.out.println("Srver > GameHandler > Client has closed connection.");
        }
        fromClientBuffer.flip();
        byte[] bytes = new byte[fromClientBuffer.remaining()];
        fromClientBuffer.get(bytes);
        recvdString = new String(bytes);
        ForkJoinPool.commonPool().execute(this);
        completedProcess = false;
    }

    
    void disconnectPlayer() throws IOException {
        playerChannel.close();
    }
}
