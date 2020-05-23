package controllers;

import com.mysql.cj.protocol.Resultset;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Course;
import utilities.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CourseController implements Initializable {
    @FXML
    private TextField txtCourseName;

    @FXML
    private TextField txtCourseCode;


    @FXML
    private Button btnSave;

    @FXML
    TableView<Course> tblData;

    @FXML
    Label lblStatus;

    @FXML
    private TableColumn<Course, String> course_name;

    @FXML
    private TableColumn<Course, String> course_code;

    ObservableList<Course> courses = FXCollections.observableArrayList();

    private Course selectedCourse;

    PreparedStatement preparedStatement;

    Connection connection;


    public CourseController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @FXML
    public void HandleEvents(MouseEvent event) {
        if (txtCourseName.getText().isEmpty() || txtCourseCode.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            saveData();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //fetColumnList();
        //fetRowList();

        try {
            String SQL = "SELECT id, name, code from courses";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getString("code")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        course_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_code.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        tblData.setItems(courses);


    }

    private String saveData() {

        try {
            String st = "INSERT INTO courses ( name, code) VALUES (?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtCourseName.getText());
            preparedStatement.setString(2, txtCourseCode.getText());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");
            clearFields();
            refreshTable();
            return "Success";




        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }




    }

    private void clearFields() {
        txtCourseName.clear();
        txtCourseCode.clear();

    }

    public void HandleUpdate(MouseEvent mouseEvent) {
        updateCourse();
    }

    private String updateCourse() {

        try {
            String st = "UPDATE courses SET name = ?, code = ? WHERE id = '" + selectedCourse.getId() + "'";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtCourseName.getText());
            preparedStatement.setString(2, txtCourseCode.getText());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Course Updated  Successfully");
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
            selectedCourse = tblData.getSelectionModel().getSelectedItem();
            txtCourseName.setText(selectedCourse.getName());
            txtCourseCode.setText(selectedCourse.getCourseCode());

        }
    }
    private String deleteCourse(){
        if (tblData.getSelectionModel().getSelectedItems() != null) {
            selectedCourse = tblData.getSelectionModel().getSelectedItem();

            try {
                String st = "delete from courses where id='"+selectedCourse.getId()+"'";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Course Deleted  Successfully");
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
            String SQL = "SELECT id, name, code from courses";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getString("code")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        course_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_code.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        tblData.setItems(courses);


    }

    public void HandleDelete(MouseEvent mouseEvent) {
        deleteCourse();
    }

}








//    private ObservableList<ObservableList> data;
//    String SQL = "SELECT name, code from courses";
//DYNAMIC COLUMNS AND ROWS, I WOULD VISIT THIS lATER.....ANNOYING SHIT....ONYAAYE GBEMI WAA FOR ALL THE SUFFERING.

    //only fetch columns
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
//                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                        return new SimpleStringProperty(param.getValue().get(j).toString());
//                    }
//                });
//
//                tblData.getColumns().removeAll(col);
//                tblData.getColumns().addAll(col);
//                col.setCellFactory(TextFieldTableCell.forTableColumn());
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

    //fetches rows and data from the list

//    private void fetRowList() {
//        data = FXCollections.observableArrayList();
//        ResultSet rs;
//        try {
//            rs = connection.createStatement().executeQuery(SQL);
//
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



