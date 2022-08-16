package client1.controllers;

import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client {
    private BufferedInputStream bufferedInputStream;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ClientViewController clientViewController;
    private String userName;
    private BufferedOutputStream bufferedOutputStream;

    public Client(Socket socket, ClientViewController clientViewController,String userName){
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedInputStream=new BufferedInputStream(socket.getInputStream());
            this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            this.clientViewController=clientViewController;
            this.userName=userName;
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("Error creating client");
            e.printStackTrace();
            closeEverything(socket,bufferedWriter,bufferedReader);
        }

    }

    public void sendMessageToClientHandler(String messageToServer){
        try {
            System.out.println("group test : "+messageToServer);
            bufferedWriter.write(userName+": "+messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to sever");
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void receiveMessageFromClientHandler(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String massageFromServer = bufferedReader.readLine();
                        if (massageFromServer.split("-")[0].equals("img")){
                            clientViewController.addLabel((massageFromServer.split(":")[0]).split("-")[1],vBox);
                            clientViewController.addImage(massageFromServer.split(" ")[1],vBox);
                        }else {
                            clientViewController.addLabel(massageFromServer,vBox);
                        }
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

    public void sendImageToClientHandler(String absolutePath) {

        try {
            bufferedWriter.write("img-"+userName+": "+absolutePath);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending image to sever");
            closeEverything(socket,bufferedWriter,bufferedReader);
        }


    }

    public void receiveImagesFromClientHandler(VBox vBox_messages) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (socket.isConnected()){
//                    try {
//                        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
//                        clientViewController.addImage(bufferedImage,vBox_messages);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
}
