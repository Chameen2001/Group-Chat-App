package client1.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    public TextField txtMessage;
    public ScrollPane sp_main;
    public VBox vBox_messages;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client=new Client(new Socket("localhost",2001));
            System.out.println("Connected to server");
        }catch (IOException e){
            e.printStackTrace();
        }

        vBox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double)newValue);
            }
        });

        client.receiveMessageFromServer(vBox_messages);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        String messageToSend = txtMessage.getText();
        if (!messageToSend.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.setPadding(new Insets(5,5,5,10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color:rgb(239,242,255);"+"-fx-background-color:rgb(15,125,242);"+"-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            vBox_messages.getChildren().add(hBox);

            client.sendMessageToServer(messageToSend);
            txtMessage.clear();

        }
    };

    public static void addLabel(String messageFromServer,VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color:rgb(233,233,235);"+"-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
        });
    }










    /*public TextArea txtMsgArea;
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
    }*/
}
