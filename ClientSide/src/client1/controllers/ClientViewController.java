package client1.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    public TextField txtMessage;
    public ScrollPane sp_main;
    public VBox vBox_messages;
    public AnchorPane mainAnchorPane;
    public AnchorPane childrenAnchorPane;
    public String clientName;

    private Client client;
    private FileChooser fileChooser;
    private File filePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnSendOnClick(MouseEvent mouseEvent) {
        String messageToSend = txtMessage.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color:rgb(15,125,242);" + "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);
            vBox_messages.getChildren().add(hBox);

            client.sendMessageToClientHandler(messageToSend);
            txtMessage.clear();

        }
    }

    ;

    public void addLabel(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color:rgb(233,233,235);" + "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
        });
    }

    public void setUserName(String text) {
        this.clientName = text;
        try {
            client = new Client(new Socket("localhost", 2001), this, clientName);
            System.out.println("Connected to server");
        } catch (IOException e) {
            System.out.println("Can notttttttttt");
            e.printStackTrace();
        }

        vBox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        client.receiveMessageFromClientHandler(vBox_messages);
        client.receiveImagesFromClientHandler(vBox_messages);
    }

    public void btnImageOnMouseClicked(MouseEvent mouseEvent) throws MalformedURLException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);

        System.out.println("Absolute : " + filePath.getAbsolutePath());
        File file = new File(filePath.getAbsolutePath());
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(150);
        imageView.setFitWidth(200);


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);
        vBox_messages.getChildren().add(hBox);


        client.sendImageToClientHandler(filePath.getAbsolutePath());

    }

    public void addImage(String url, VBox vBox) {
        File file = new File(url);
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(150);
        imageView.setFitWidth(200);


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);
        Platform.runLater(() -> {
            vBox_messages.getChildren().add(hBox);
        });




        /*HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        WritableImage wr = null;
        if (bufferedImage != null) {
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }
        Image image = new ImageView(wr).getImage();
        ImageView imageView = new ImageView(image);
        hBox.getChildren().add(imageView);
        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
        });*/
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
