
package sockets.client.network;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author darkferi
 */
public class ClientSocketHandling implements Runnable{
    
    //Fields
    private OutputHandler screenHandler;
    private InetSocketAddress serverPort;
    private Selector selector;
    private SocketChannel clientChannel;
    private Set keySet;
    private SelectionKey key;
    private Iterator keyIterator;
    private static final int LINGER_TIME = 5000;
    private static final int MAX_BUFFER_LENGTH = 8192;
    private boolean connected = false;
    private ByteBuffer fromServerBuffer = ByteBuffer.allocate(MAX_BUFFER_LENGTH);
    private String dataToServer;
    private static final String EXIT = "exit";
    private volatile boolean timeToSend = false;
    
    
    
     public void connect(int port, OutputHandler screenHandler) throws IOException{
        this.screenHandler = screenHandler;
        serverPort = new InetSocketAddress(port);
        Thread clientCommunicatioin = new Thread(this);
        clientCommunicatioin.start();
    }
    
    @Override
    public void run(){
        try{
            selector = Selector.open();
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
            clientChannel.connect(serverPort);
            connected = true;
            clientChannel.register(selector, SelectionKey.OP_CONNECT);

            while (connected){

                    if (timeToSend) {
                        clientChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                        timeToSend = false;
                    }

                    selector.select();
                    keySet = selector.selectedKeys();
                    keyIterator = keySet.iterator();
                    while (keyIterator.hasNext()){
                        key = (SelectionKey) keyIterator.next();
                        keyIterator.remove();

                        if (key.isConnectable()) {
                            clientChannel.finishConnect();
                            key.interestOps(SelectionKey.OP_READ);
                            screenHandler.messageOnScreen("Now Player is Connected to the Game...");
                        } 

                        else if (key.isReadable()) {
                            fromServerBuffer.clear();
                            if (clientChannel.read(fromServerBuffer) != -1) {
                                fromServerBuffer.flip();
                                byte[] bytes = new byte[fromServerBuffer.remaining()];
                                fromServerBuffer.get(bytes);
                                String recvdString =  new String(bytes);
                                showMessageOnScreeen( recvdString, screenHandler);
                            }
                            else{
                                System.out.println("Connection is lost");
                            }
                        } 

                        else if (key.isWritable()) {
                            ByteBuffer toServerBuffer;
                            toServerBuffer = ByteBuffer.wrap(dataToServer.getBytes());
                            clientChannel.write(toServerBuffer);
                            key.interestOps(SelectionKey.OP_READ);
                        }

                        else{
                            System.out.println("The key is not valid!!!!");
                        }

                    }
                    
            }
            clientChannel.close();
            clientChannel.keyFor(selector).cancel();
            
        } catch (Exception e){
            System.out.println("Client > Net > ClientSocketHandling: Exception occured!!!");
        }
        
    }
    
    
    public void disconnect(){
        if (connected){
            sendMessage(EXIT);
            connected = false;
        }
        else{
            System.out.println("EXIT from command line!");
        }
    }
    
    public void sendMessage(String charGuess){
        if(connected){
            dataToServer = charGuess;
            timeToSend = true;
            selector.wakeup();
        }
        else{
            System.out.println("You have not started the game!!! First start!!!!");
        }
    }
    
    
    private void showMessageOnScreeen(String msg, OutputHandler screenHandler) {
        Executor pool = ForkJoinPool.commonPool();
        
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    screenHandler.messageOnScreen(msg);
                }
            });
        
    }
}
