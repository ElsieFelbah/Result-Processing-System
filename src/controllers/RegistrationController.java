/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.Course;
import models.Student;
import utilities.ConnectionUtil;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author oXCToo
 */
public class RegistrationController implements Initializable {

    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtidNumber;
    //    @FXML
//    private DatePicker txtDOB;
    @FXML
    private TextField txtPIN;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<String> txtGender;
    @FXML
    private ComboBox<String> txtRole;
    @FXML
    private TableColumn<Student, String> firstName;
    @FXML
    private TableColumn<Student, String> lastName;
    @FXML
    private TableColumn<Student, String> idNumber;
    @FXML
    private TableColumn<Student, String> PIN;
    @FXML
    private TableColumn<Student, String> gender;
    @FXML
    Label lblStatus;

    @FXML
    TableView<Student> tblData;


    ObservableList<Student> students = FXCollections.observableArrayList();
    private Student selectedStudent;

    PreparedStatement preparedStatement;
    Connection connection;

    public RegistrationController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtGender.getItems().addAll("Male", "Female", "Other");
        //txtGender.getSelectionModel().select("Male");
        txtRole.getItems().addAll("student", "lecturer");
        txtRole.getSelectionModel().select("student");
        try{
            String SQL = "SELECT id, firstName, lastName, idNUmber, PIN, gender from users WHERE role = 'student' ";
            ResultSet rs =connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("idNumber"), rs.getString("PIN"), rs.getString("gender")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        PIN.setCellValueFactory(new PropertyValueFactory<>("PIN"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tblData.setItems(students);
        //fetColumnList();
        //fetRowList();


    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        //check if not empty
        if (txtidNumber.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() || txtPIN.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            saveData();
        }

    }

    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtidNumber.clear();
        txtPIN.clear();

    }

    private String saveData() {

        try {
            String st = "INSERT INTO users ( firstName, lastName, idNumber, PIN, gender, role) VALUES (?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtidNumber.getText());
            preparedStatement.setString(4, txtPIN.getText());
            preparedStatement.setString(5, txtGender.getValue().toString());
            preparedStatement.setString(6, txtRole.getValue());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");
            refreshTable();
            //fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }
    public void HandleUpdate(MouseEvent mouseEvent) {
        updateStudent();
    }

    private String updateStudent() {

        try {
            String st = "UPDATE users SET firstName = ?, lastName = ?, idNumber = ?, PIN = ?, gender = ? WHERE id = '" + selectedStudent.getId() + "'";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtidNumber.getText());
            preparedStatement.setString(4, txtPIN.getText());
            preparedStatement.setString(5, txtGender.getValue());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Student Updated Successfully");
            refreshTable();
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    public void onEdit(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 1)
            Edit();

    }

    private void Edit() {
        if (tblData.getSelectionModel().getSelectedItems() != null) {
            selectedStudent = tblData.getSelectionModel().getSelectedItem();
            txtFirstname.setText(selectedStudent.getFirstName());
            txtLastname.setText(selectedStudent.getLastName());
            txtidNumber.setText(selectedStudent.getIdNumber());
            txtPIN.setText(selectedStudent.getPIN());
            txtGender.getItems().add(selectedStudent.getGender());

        }
    }
    private String deleteStudent(){
        if (tblData.getSelectionModel().getSelectedItems() != null) {
            selectedStudent = tblData.getSelectionModel().getSelectedItem();

            try {
                String st = "delete from users where id='"+selectedStudent.getId()+"'";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Student Deleted  Successfully");
                refreshTable();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText(ex.getMessage());
                return "Exception";
            }
        }
        return "Success";
    }
    private void refreshTable() {
        tblData.getItems().clear();
        try {
            String SQL = "SELECT id,firstName, lastName, idNumber, PIN, gender from users WHERE role = 'student'";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("idNumber"), rs.getString("PIN"), rs.getString("gender")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        PIN.setCellValueFactory(new PropertyValueFactory<>("PIN"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tblData.setItems(students);
    }


    public void HandleDelete(MouseEvent mouseEvent) {
        deleteStudent();
    }
}
    //private ObservableList<ObservableList> data;
//    String SQL = "SELECT firstName, lastName, idNUmber, PIN, gender from users WHERE role = 'student' ";
//
//    //only fetch columns
//    private void fetColumnList() {
//
//        try {
//            ResultSet rs = connection.createStatement().executeQuery(SQL);
//
//            //SQL FOR SELECTING ALL OF CUSTOMER
//            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                //We are using non property style for making dynamic table
//                final int j = i;
//                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
//                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
//                        return new SimpleStringProperty(param.getValue().get(j).toString());
//                    }
//                });
//
//                tblData.getColumns().removeAll(col);
//                tblData.getColumns().addAll(col);
//
//                System.out.println("Column [" + i + "] ");
//
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error " + e.getMessage());
//
//        }
//    }
//
//    //fetches rows and data from the list
//    private void fetRowList() {
//        data = FXCollections.observableArrayList();
//        ResultSet rs;
//        try {
//            rs = connection.createStatement().executeQuery(SQL);
//
//            while (rs.next()) {
//                //Iterate Row
//                ObservableList row = FXCollections.observableArrayList();
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    //Iterate Column
//                    row.add(rs.getString(i));
//                }
//                System.out.println("Row [1] added " + row);
//                data.add(row);
//
//            }
//
//            tblData.setItems(data);
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }

