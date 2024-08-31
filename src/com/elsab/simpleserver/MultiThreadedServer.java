package com.elsab.simpleserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {
    private int port;

    MultiThreadedServer(int port){
        this.port = port;
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server listens on port:" + port);

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client added. Welcoming " + clientSocket.getRemoteSocketAddress());
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        MultiThreadedServer server = new MultiThreadedServer(port);
        server.start();
    }
}

