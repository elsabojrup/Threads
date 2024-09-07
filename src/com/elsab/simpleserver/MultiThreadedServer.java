package com.elsab.simpleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MultiThreadedServer {
    private int port;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();

    MultiThreadedServer(int port){
        this.port = port;
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server listens on port:" + port);

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client added. Welcoming " + clientSocket.getRemoteSocketAddress());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Enter username.");
                String username = in.readLine();
                if(username == null || username.trim().isEmpty()){
                    System.out.println("User did not enter a name.");
                    out.println("You did not enter a username. Bye!");
                    clientSocket.close();
                    continue;
                }
                ClientHandler client = new ClientHandler(clientSocket, username);
                addClientHandler(username, client);
                out.println("Welcome " +username+ "!");
                client.start();
                broadcastMessages(username + " has joined the chat.\n If you want to chat with " +username+ ", type @"+username, username);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClientHandler(String userName, ClientHandler clientHandler){
        clientHandlers.put(userName, clientHandler);
    }

    public static ClientHandler getClientHandler(String username){
        return clientHandlers.get(username);
    }

    public static void removeClientHandler(String username){
        clientHandlers.remove(username);
    }

    public void connectUsers(String username){
        for(ClientHandler clientHandler : clientHandlers.values() ){
            if(clientHandler.getUsername() != username ){
                String answer = clientHandler.senPM("Do you want to chat with " + username + "? (y/n)");
            }
        }
    }

    public void broadcastMessages(String message, String username){
        for(ClientHandler clientHandler : clientHandlers.values()){
            if(clientHandler.getUsername() != username){
            clientHandler.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        MultiThreadedServer server = new MultiThreadedServer(port);
        server.start();
    }
}

