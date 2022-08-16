package controllers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientImageHandler implements Runnable {
    private static ArrayList<ClientImageHandler> clientImageHandlers = new ArrayList<>();
    private  Socket socket;
    private  BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private String clientUserName;

    public ClientImageHandler(Socket socket, String clientUserName) {

        try {
            this.socket=socket;
            this.bufferedInputStream=new BufferedInputStream(socket.getInputStream());
            this.bufferedOutputStream=new BufferedOutputStream(socket.getOutputStream());
            this.clientUserName=clientUserName;
            clientImageHandlers.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()){
            try {
                BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                broadcastImage(bufferedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastImage(BufferedImage bufferedImage) {
        for (ClientImageHandler clientImageHandler : clientImageHandlers){
            try {
                if (clientImageHandler.clientUserName!=clientUserName){
                    ImageIO.write(bufferedImage,"png",bufferedOutputStream);
                    bufferedOutputStream.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
