package controllers;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private ServerSocket serverSocket;
    private Socket  socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server (ServerSocket serverSocket){

        try {
            this.serverSocket=serverSocket;
            this.socket=serverSocket.accept(); //jvm will stop here and result will be no server interface execution;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            System.out.println("Error creating server");
            e.printStackTrace();
            closeEverything(socket,bufferedWriter,bufferedReader);

        }
        System.out.println("here");
    }
    public void sendMessageToClient(String messageToClient){
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to client");
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void receiveMessageFromClient(VBox vBox){
        /*System.out.println("karakenoooooooo");
        while (socket.isConnected()){
            System.out.println("Connectod");
            try {
                System.out.println("first ss");
                String massageFromClient = bufferedReader.readLine();
                ServerViewController.addLabel(massageFromClient,vBox);
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Error receiving message from client");
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }*/

        new Thread(new Runnable() {
           @Override
             public void run() {
                while (socket.isConnected()){
                    try {
                        String massageFromClient = bufferedReader.readLine();
                        ServerViewController.addLabel(massageFromClient,vBox);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from client");
                        closeEverything(socket,bufferedWriter,bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedWriter bufferedWriter,BufferedReader bufferedReader){
        try {
            if (bufferedReader!=null) {
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
            if (socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
