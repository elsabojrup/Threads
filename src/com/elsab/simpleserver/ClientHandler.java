package com.elsab.simpleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread {
    private Socket socket;
    private String username;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, String username) {
        this.socket = socket;
        this.username = username;

    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            this.in = in;
            this.out = out;

            String message;
            while ((message = in.readLine()) != null) {
                if(message.startsWith("@"))
                {
                    int spaceIndex = message.indexOf(" ");
                    String recipient = message.substring(1, spaceIndex);
                    String pm = message.substring(spaceIndex+1);
                    sendPrivateMessage(recipient, pm);
                }
                System.out.println("Recieved from " + username +": " + message);
                out.println("Echo: " + message);
            }

        }catch (SocketException e) {
            System.out.println("Connection reset by client " + username + ": " + e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            MultiThreadedServer.removeClientHandler(username);
            try{
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        }

    private void sendPrivateMessage(String recipient, String pm) {
        ClientHandler recieptHandler = MultiThreadedServer.getClientHandler(recipient);
        if(recieptHandler != null){
            recieptHandler.sendMessage("@" + username + " " + pm);
        }
        else{
            sendMessage(recipient +" not found. Sorry...");
        }
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public String senPM(String message){
        out.println(message);
        String input = "";
        while(!input.matches("^(y|yes|n|no)$"))
        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public String getUsername() {
        return username;
    }
}
