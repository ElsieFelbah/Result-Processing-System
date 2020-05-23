package controllers;

import com.jfoenix.controls.JFXComboBox;
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

    ObservableList<Student> students = FXCollections.observableArrayList();

    public StudentsProfileController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @FXML
    private Button btnRegister;

    @FXML
    private ComboBox<String> listOfCourses;

    PreparedStatement preparedStatement;
    Connection connection;
    private List<String> options = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfCourses.setItems(FXCollections.observableArrayList(getData()));
        txtidNumber.getText();
        String sql = "SELECT * FROM users Where idNumber = '" + txtidNumber.getText() + "'";
        try {
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                students.add(new Student(resultSet.getString("firstName"), resultSet.getString("lastName")));
                txtfirstName.setText(String.valueOf(new PropertyValueFactory<Student,String>("firstName")));
                txtlastName.setText(String.valueOf(new PropertyValueFactory<Student,String>("lastName")));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }

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
        //Display the message
        txtidNumber.setText(idNumber);

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
            txtidNumber.clear();
            txtfirstName.clear();
            txtlastName.clear();
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
            System.out.println(txtidNumber.getText());
            String SQL = "SELECT courseName FROM course_registration WHERE idNumber='" + txtidNumber.getText() + "'";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                options.add(rs.getString("courseName"));
            }
            rs.close();
            if (options.contains(listOfCourses.getValue())) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Already registered for this course");
                txtidNumber.clear();
                txtfirstName.clear();
                txtlastName.clear();
                listOfCourses.setValue("choose a course");
            } else {
                saveData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
