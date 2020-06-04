package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Student;
import utilities.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentResultsController implements Initializable {
    @FXML
            private TableView<Student> tblResults;
    @FXML
            private TableColumn<Student, String> colCourseName;
    @FXML
            private TableColumn<Student, String> colGrade;
    @FXML
            private Button btnLogout;
    @FXML
    private Button btnHome;

    @FXML
    private Button btnCourses;


    ObservableList<Student> students = FXCollections.observableArrayList();

    private List<String> firstnames = new ArrayList<String>();
    private List<String> lastnames = new ArrayList<String>();


    Connection connection;
    public void handleButtonAction(MouseEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (event.getSource() == btnLogout) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to leave?");
                alert.setTitle("WARNING");
                alert.getButtonTypes().remove(0, 2);
                alert.getButtonTypes().add(0, ButtonType.YES);
                alert.getButtonTypes().add(1, ButtonType.NO);
                Optional<ButtonType> confirmationResponse = alert.showAndWait();
                if (confirmationResponse.get() == ButtonType.YES) {
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Login.fxml")));
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert.close();
                }
            }
            if(event.getSource() == btnHome){
                StudentsProfileController studentsProfileController = new StudentsProfileController();
                studentsProfileController.loadSceneAndSendMessageToHome();
            }
            if(event.getSource() == btnCourses){

            }


        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public StudentResultsController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    public void handleEvents(MouseEvent event) {
    }
    public void transferMessage(String idNumber) {

        String IDNUMBER = idNumber;
        try {
            String sql = "SELECT * FROM course_registration WHERE idNumber = '" + IDNUMBER + "'";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                students.addAll(new Student(resultSet.getString("courseName"), resultSet.getString("grade")));
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        tblResults.setItems(students);

    }
//    private void loadSceneAndSendMessageToHome() {
//        try {
//            //Load second scene
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentHome.fxml"));
//            Parent root = loader.load();
//
//            //Get controller of scene2
//            studentHomeController studentHomeController = loader.getController();
//            studentHomeController.transferMessage(txtidNumber.getText(), txtfirstName.getText(), txtlastName.getText());
//
//
//            //Show scene 2 in new window
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//    }
//    private void loadSceneAndSendMessageToCourse() {
//        try {
//            //Load second scene
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentsProfile.fxml"));
//            Parent root = loader.load();
//
//            //Get controller of scene2
//            StudentsProfileController studentsProfileController = loader.getController();
//            studentsProfileController.transferMessage(lblID.getText());
//
//
//            //Show scene 2 in new window
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
