package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Blah implements Initializable {
    @FXML
    private AnchorPane loadAnchor;

    @FXML
    private Button btnHome;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleButtonAction(MouseEvent event) throws IOException {
        if(event.getSource() == btnHome){
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/studentHome.fxml"));
            loadAnchor.getChildren().setAll(pane);
        }
    }
}
