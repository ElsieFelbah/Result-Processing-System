package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class HomeController implements Initializable {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btn_Courses;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnScores;

    @FXML
    private Button btnClasses;


    public void handleButtonClicks(ActionEvent mouseEvent) {
        if(mouseEvent.getSource() == btnScores){
            loadStage("/fxml/scores.fxml");
        }
        if (mouseEvent.getSource() == btnStudents) {
            loadStage("/fxml/Registration.fxml");
        }
        if (mouseEvent.getSource() == btn_Courses) {
            loadStage("/fxml/course.fxml");
        }

        if (mouseEvent.getSource() == btnDashboard) {
            loadStage("/fxml/Dashboard.fxml");
        }

    }
    private void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
//            stage.getIcons().add(new Image("/home/icons/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
