package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerViewController implements Initializable {


    public TextField txtMessage;
    public ScrollPane sp_main;
    public VBox vBox_messages;
    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            server = new Server(new ServerSocket(2001));
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        vBox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });


        server.receiveMessageFromClient(vBox_messages);

    }



    public void btnSendOnAction(ActionEvent actionEvent) {
        String massageToSend = txtMessage.getText();
        if (!massageToSend.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text = new Text(massageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color:rgb(239,242,255);"+"-fx-background-color:rgb(15,125,242);"+"-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            vBox_messages.getChildren().add(hBox);

            server.sendMessageToClient(massageToSend);
            txtMessage.clear();
        };
    };

    public static void addLabel(String messageFromClient,VBox vBox){
        System.out.println("ssss"+messageFromClient);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color:rgb(233,233,235);"+"-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

//        vBox.getChildren().add(hBox); this method called by another thread instead jfx thread so We Can not insert anything without jfx thread

        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
        });
        System.out.println("awada methana");
    }
















   /* public TextArea txtServerMsgArea;
    public TextField txtServerMsgTypeField;
    ServerSocket serverSocket=null;
    Socket socket=null;
    public void initialize(){
        new Thread(() -> {
            try {
                ServerSocket serverSocket= new ServerSocket(5000);
                System.out.println("controllers.Server started!");
                socket= serverSocket.accept();
                System.out.println("Client Connected!");
                InputStreamReader inputStreamReader =
                        new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                String record= bufferedReader.readLine();
                System.out.println(record);
                *//* while(true){if(!message.equals("exit"))}*//*

                while (!record.equals("exit")){
                    txtServerMsgArea.appendText(record);
                    System.out.println(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        PrintWriter printWriter= new PrintWriter(socket.getOutputStream());
        printWriter.println(txtServerMsgTypeField.getText());
        txtServerMsgTypeField.clear();
        printWriter.flush();
    }*/
}
