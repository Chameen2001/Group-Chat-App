package controllers;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String clientUserName;

    public ClientHandler(Socket socket){
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUserName=bufferedReader.readLine();
            System.out.println("Const name : "+this.clientUserName);
            clientHandlers.add(this);
            broadcastMessage("SERVER: "+clientUserName+" has entered the chat!");
        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    @Override
    public void run() {

        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient   = bufferedReader.readLine();
                System.out.println(clientUserName+" :: "+messageFromClient);
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Error receiving message from client");
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }

    }

    private void broadcastMessage(String messageFromClient) {
        if (messageFromClient==null)return;
        for (ClientHandler clientHandler : clientHandlers){
            try {
                System.out.println(clientHandler.clientUserName+" != "+clientUserName);
                if (!clientHandler.clientUserName.equals(clientUserName)){
                    System.out.println(messageFromClient);
                    clientHandler.bufferedWriter.write(messageFromClient);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
            }
        }
    }

    public void removeClientHandler(){
        System.out.println("in remove meth : "+clientUserName);
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+clientUserName+" left the chat");
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler();
        try {
            if (socket!=null){
                socket.close();
            }

            if (bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
