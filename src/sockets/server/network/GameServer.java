
package sockets.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class GameServer {
    
    private static final int LINGER_TIME = 5000;
    private ServerSocket serverSocket;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private SocketChannel playerChannel;
    private Set keySet;
    private SelectionKey key;
    private Iterator keyIterator;
        
    public void serve(int port){
        try{
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverSocket = serverChannel.socket();
            serverChannel.configureBlocking(false); 
            InetSocketAddress serverPort = new InetSocketAddress(port);
            serverSocket.bind(serverPort);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("Waiting for Players!!!");
            
            while(true){
                selector.select();
                keySet = selector.selectedKeys();
                keyIterator = keySet.iterator();
                while (keyIterator.hasNext()){
                    key = (SelectionKey) keyIterator.next();
                    keyIterator.remove();
                    if (key.isAcceptable()){
                        serverChannel = (ServerSocketChannel) key.channel();
                        playerChannel = serverChannel.accept();
                        playerChannel.configureBlocking(false);
                        playerChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
                        GameHandler playerHandler = new GameHandler(playerChannel);
                        playerChannel.register(selector,SelectionKey.OP_WRITE , playerHandler);
                    }
                    else if(key.isReadable()){
                        playerChannel = (SocketChannel) key.channel();
                        GameHandler playerHandler = (GameHandler) key.attachment();
                        try{
                            playerHandler.recvMsg();
                        } catch (IOException e) {
                            System.out.println("\nPlayer requested for disconnection...\n");
                            playerHandler.disconnectPlayer();
                            key.cancel();
                        }
                    }
                    else if(key.isWritable()){
                        playerChannel = (SocketChannel) key.channel();
                        GameHandler playerHandler = (GameHandler) key.attachment();
                        try{
                            playerHandler.sendMsg();
                            key.interestOps(SelectionKey.OP_READ);
                        } catch (IOException e) {
                            System.out.println("\nPlayer requested for disconnection...\n");
                            playerHandler.disconnectPlayer();
                            key.cancel();
                        }
                    }
                    else{
                        System.out.println("The key is not valid!!!");
                    }
                }
           }
        } catch (IOException e){
            System.out.println("GameServer: IOException occured"
                    + "(possible reason: server run already)");
        } 
    }
    
}
    