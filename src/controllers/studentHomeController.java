package controllers;

//import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class studentHomeController implements Initializable {
    @FXML
    private Button btnHome;

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnResults;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblFName;

    @FXML
    private Label lblLName;

    @FXML
    private Label lblID;


    ObservableList<Student> students = FXCollections.observableArrayList();

    public studentHomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }


    PreparedStatement preparedStatement;
    Connection connection;
    private List<String> options = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblID.getText();
        String sql = "SELECT * FROM users Where idNumber = '" + lblID.getText() + "'";
        try {
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                students.add(new Student(resultSet.getString("firstName"), resultSet.getString("lastName")));
                lblFName.setText(String.valueOf(new PropertyValueFactory<Student,String>("firstName")));
                lblLName.setText(String.valueOf(new PropertyValueFactory<Student,String>("lastName")));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }


    }
    public void transferMessage(String idNumber) {
        //Display the message
        lblID.setText(idNumber);

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
            if (event.getSource() == btnCourses) {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/StudentsProfile.fxml")));
            }


        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }


    }

}
