package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Student;
import utilities.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TableColumn<Student, String> idNumber;

    @FXML
    private TableColumn<Student, String> firstName;

    @FXML
    private TableColumn<Student, String> lastName;

    @FXML
    private ComboBox<String> courseName;

    @FXML
    private Button btnviewStudents;

    ObservableList<Student> students = FXCollections.observableArrayList();

    private List<String> options = new ArrayList<>();

    PreparedStatement preparedStatement;

    Connection connection;

    public DashboardController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    public void handleButtonAction(MouseEvent event) {
        generateStudents();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseName.setItems(FXCollections.observableArrayList(getData()));

    }
    private void generateStudents(){
        System.out.println(courseName.getValue());
        tblStudents.getItems().clear();
        try{
            String SQL = "SELECT firstName, lastName, idNumber from course_registration WHERE courseName = '"+courseName.getValue()+"' ";
            ResultSet rs =connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student( rs.getString("firstName"), rs.getString("lastName"), rs.getString("idNumber")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tblStudents.setItems(students);


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
}
