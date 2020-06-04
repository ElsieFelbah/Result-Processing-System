package controllers;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Result;
import models.Student;
import utilities.ConnectionUtil;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentsProfileController implements Initializable {
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnResults;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnLogout;

    @FXML
    private TextField txtidNumber;

    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtfirstName;

    @FXML
    private TextField txtlastName;

    @FXML
    private Button btnHome;


    @FXML
    private Button btnRegister;

    @FXML
    private ComboBox<String> listOfCourses;

    PreparedStatement preparedStatement;
    Connection connection;
    private List<String> options = new ArrayList<>();
    private List<String> options2 = new ArrayList<>();
    private List<String> firstnames = new ArrayList<>();
    private List<String> lastnames = new ArrayList<>();

    public StudentsProfileController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfCourses.setItems(FXCollections.observableArrayList(getData()));

    }


    public void handleEvents(MouseEvent event) {
        if (txtlastName.getText().isEmpty() || txtfirstName.getText().isEmpty() || txtidNumber.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            isPresent();
        }

    }

    public void transferMessage(String idNumber) {

        txtidNumber.setText(idNumber);
        try {
            String sql = "SELECT firstName, lastName FROM users WHERE idNumber = '" + txtidNumber.getText() + "'";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                firstnames.add(resultSet.getString("firstName"));
                lastnames.add(resultSet.getString("lastName"));
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String first : firstnames
        ) {
            txtfirstName.setText(first);
        }
        for (String last : lastnames
        ) {
            txtlastName.setText(last);
        }

    }

    private void loadSceneAndSendMessageToResults() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentResults.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            StudentResultsController studentResultsController = loader.getController();
            studentResultsController.transferMessage(txtidNumber.getText());


            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void loadSceneAndSendMessageToHome() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentHome.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            studentHomeController studentHomeController = loader.getController();
            studentHomeController.transferMessage(txtidNumber.getText(), txtfirstName.getText(), txtlastName.getText());


            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

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

            if (event.getSource() == btnResults) {
                stage.close();
                loadSceneAndSendMessageToResults();
            }
            if (event.getSource() == btnHome) {
                stage.close();
                loadSceneAndSendMessageToHome();
            }


        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }


    }

    private String saveData() {
        try {
            String st = "INSERT INTO course_registration (firstName, lastName, idNumber, courseName) VALUES (?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtfirstName.getText());
            preparedStatement.setString(2, txtlastName.getText());
            preparedStatement.setString(3, txtidNumber.getText());
            preparedStatement.setString(4, listOfCourses.getValue());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Registered Successfully");
            listOfCourses.setValue("choose a course");

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "Success";
    }

    public List<String> getData() {
        try {
            String SQL = "SELECT name FROM courses";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                options.add(rs.getString("name"));
            }
            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return options;
    }

    public void isPresent() {
        try {

            String SQL = "SELECT courseName, idNumber FROM course_registration WHERE idNumber='" + txtidNumber.getText() + "'";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                options2.add(rs.getString("courseName"));
            }
            rs.close();
            if (options2.contains(listOfCourses.getValue())) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Already registered for this course");
                listOfCourses.setValue("choose a course");
            } else {
                saveData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
