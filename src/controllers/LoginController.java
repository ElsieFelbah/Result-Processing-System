package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.Student;
import utilities.ConnectionUtil;


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

/**
 * @author oXCToo
 */
public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtidNumber;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private CheckBox checkStudent;

    @FXML
    private CheckBox checkLecturer;

    @FXML
    private Button btnSignin;

    private String role;

    private static String prof;



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    ///
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleButtonAction(MouseEvent event) {
        Window owner = btnSignin.getScene().getWindow();
        if (event.getSource() == btnSignin) {
            //login here
            if (logIn().equals("Success")) {
                try {

                    //add you loading or delays
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    if (checkStudent.isSelected() && checkLecturer.isSelected()) {
                        infoBox("Please select one, You cannot select both", null, "Failed");
                        stage.close();

                    } else if (checkStudent.isSelected()) {
                        stage.close();
                        infoBox("Login Successful!", null, "Success");
//                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/StudentsProfile.fxml")));
//                        stage.setScene(scene);
//                        stage.show();
                        loadSceneAndSendMessage();
                    } else if (checkLecturer.isSelected()) {
                        stage.close();
                        infoBox("Login Successful!", null, "Success");
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Home.fxml")));
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        //setLblError(Color.TOMATO, "Hey select either a lecturer or student");
                        infoBox("Hey select either a lecturer or student", null, "Failed");
                        stage.close();
                    }


                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }


    }


    private void loadSceneAndSendMessage() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentsProfile.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            StudentsProfileController studentsProfileController = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            studentsProfileController.transferMessage(txtidNumber.getText());


            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public LoginController() {
        con = ConnectionUtil.conDB();
    }



    //we gonna use string to check for status
    private String logIn() {
        Window owner = btnSignin.getScene().getWindow();
        String status = "Success";
        String idNumber = txtidNumber.getText();
        String PIN = txtPassword.getText();
        if (idNumber.isEmpty() || PIN.isEmpty()) {
            //setLblError(Color.TOMATO, "Empty credentials");
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "There is an empty field(s)");
            status = "Error";
        } else {
            //query
            if (checkLecturer.isSelected()) {
                setRole("lecturer");
                prof = getRole();
            }
            if (checkStudent.isSelected()) {
                setRole("student");
                prof = getRole();

            }
            System.out.println(prof);
            String sql = "SELECT * FROM users Where idNumber = ? and PIN = ? and role = '" + prof + "'";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, idNumber);
                preparedStatement.setString(2, PIN);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {

                    infoBox("Could not login. Check credentials", null, "Failed");
                    status = "Error";

                }


            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }

        return status;
    }


    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }


    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }


}
