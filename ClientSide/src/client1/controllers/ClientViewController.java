package client1.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientViewController {

    public TextArea txtMsgArea;
    public TextField txtMsgTypeField;
    Socket socket=null;
    public void initialize() throws IOException {

        new Thread(() -> {
            System.out.println("initialized");
            try {
                socket=new Socket("localhost",3000);
                InputStreamReader inputStreamReader =
                        new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                String record= bufferedReader.readLine();
                while (!record.equals("exit")){
                    txtMsgArea.appendText(record);
                    System.out.println(record);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        System.out.println(socket);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(txtMsgArea.getText());
        txtMsgTypeField.clear();
        printWriter.flush();
    }
}
