package com.elsab.simpleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(String[] args){
        String serverAddress = "localhost";
        int serverPort = 12345;

        try(Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to server.");

            Thread readThread = new Thread(() -> {
                try{
                    String response;
                    while((response = in.readLine()) != null){
                        System.out.println("Server: " + response);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            });
            readThread.start();

            String message;
            while((message = scanner.nextLine()) != null){
                out.println(message);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
