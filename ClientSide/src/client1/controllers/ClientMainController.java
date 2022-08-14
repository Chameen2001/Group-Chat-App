package client1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ClientMainController {
    public TextField txtUserName;
    public AnchorPane mainAnchorPane;
    public AnchorPane childrenAnchorPane;

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        mainAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/ClientView.fxml"));
        Parent load = fxmlLoader.load();
        ClientViewController controller = fxmlLoader.getController();
        controller.setUserName(txtUserName.getText());
        mainAnchorPane.getChildren().add(load);
    }
}
