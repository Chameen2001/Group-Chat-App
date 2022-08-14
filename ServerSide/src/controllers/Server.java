package controllers;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    private ServerSocket serverSocket;

    public Server (ServerSocket serverSocket){
        this.serverSocket=serverSocket;
    }

    public void closeServerSocket(ServerSocket serverSocket){
        try {
            if (serverSocket!=null) {
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void startServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
//            this.serverSocket=serverSocket;
//            this.socket=serverSocket.accept(); //jvm will stop here and result will be no server interface execution;
//            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            ClientHandler clientHandler = new ClientHandler(socket,bufferedReader,bufferedWriter);
//            clientHandlers.add(clientHandler);
        }catch (IOException e){
            System.out.println("Error creating server");
            e.printStackTrace();
            closeServerSocket(serverSocket);

        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2001);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
