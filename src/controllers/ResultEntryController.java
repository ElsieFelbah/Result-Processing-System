package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import models.Result;
import models.Student;
import utilities.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ResultEntryController implements Initializable {
    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TableColumn<Student, String> idNumber;

    @FXML
    private TableColumn<Student, String> lastName;

    @FXML
    private TableColumn<Student, Float> assignment;

    @FXML
    private TableColumn<Student, Float> attendance;

    @FXML
    private TableColumn<Student, Float> project;

    @FXML
    private TableColumn<Student, Float> midsem;

    @FXML
    private TableColumn<Student, Float> exam;

    @FXML
    private TableColumn<Student, Character> grade;

    @FXML
    private ComboBox<String> courseName;

    @FXML
    private TextField txtAssignment;

    @FXML
    private TextField txtAttendance;

    @FXML
    private TextField txtProject;

    @FXML
    private TextField txtMidsem;

    @FXML
    private TextField txtExam;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnsaveScores;

    @FXML
    private Button btnviewStudents;

    private List<String> options = new ArrayList<>();
    ObservableList<Student> students = FXCollections.observableArrayList();
    ObservableList<Result> scores = FXCollections.observableArrayList();
    private Result score;
    private Student selectedStudent;

    PreparedStatement preparedStatement;
    Connection connection;

    @FXML
    void handleButtonAction(MouseEvent event) {

        generateStudentsAndResults();

    }

    public ResultEntryController() {
        connection = ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseName.setItems(FXCollections.observableArrayList(getData()));


    }

    @FXML
    void onEdit(MouseEvent event) {
        if (event.getClickCount() > 1)
            Edit();
    }

    private void Edit() {
        if (tblStudents.getSelectionModel().getSelectedItem() != null) {
            selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
            txtAssignment.setText(Float.toString(selectedStudent.getAssignmentScore()));
            txtAttendance.setText(Float.toString(selectedStudent.getAttendance()));
            txtProject.setText(Float.toString(selectedStudent.getProjectScore()));
            txtMidsem.setText(Float.toString(selectedStudent.getMidsemScore()));
            txtExam.setText(Float.toString(selectedStudent.getExamScore()));
        }
    }

    private void generateStudentsAndResults() {
        tblStudents.getItems().clear();
        try {
            String SQL = "SELECT * from course_registration WHERE courseName = '" + courseName.getValue() + "' ";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("idNumber"), rs.getFloat("assignment"), rs.getFloat("attendance"), rs.getFloat("project"), rs.getFloat("midsem"), rs.getFloat("exam"), rs.getString("grade")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        assignment.setCellValueFactory(new PropertyValueFactory<>("assignmentScore"));
        attendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        project.setCellValueFactory(new PropertyValueFactory<>("projectScore"));
        midsem.setCellValueFactory(new PropertyValueFactory<>("midsemScore"));
        exam.setCellValueFactory(new PropertyValueFactory<>("examScore"));
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
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


    public void handleUpdateAction(MouseEvent event) {
        updateResults();
    }



    private Character calculateGrade(float ass, float att, float proj, float mid, float ex) {
        float assignment = Float.parseFloat(txtAssignment.getText());
        float attendance = Float.parseFloat(txtAttendance.getText());
        float project = Float.parseFloat(txtProject.getText());
        float midsem = Float.parseFloat(txtMidsem.getText());
        float exam = Float.parseFloat(txtExam.getText());

        float percass = (assignment / 100) * ass;
        float percatt = (attendance / 100) * att;
        float percproj = (project / 100) * proj;
        float percmid = (midsem / 100) * mid;
        float percex = (exam / 100) * ex;
        float total = percass + percatt + percproj + percmid + percex;

        if (total > 80)
            grade.setText("A");
        else if (total > 70 && total < 79)
            grade.setText("B");
        else if (total > 60 && total < 69)
            grade.setText("C");
        else if (total > 50 && total < 59)
            grade.setText("D");
        else if (total > 40 && total < 49)
            grade.setText("E");
        else if (total < 39)
            grade.setText("F");


        return 'V';

    }

    private String updateResults() {

        try {
            String SQL = "SELECT * from scores";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                scores.add(new Result(rs.getInt("id"), rs.getFloat("assignment"), rs.getFloat("attendance"), rs.getFloat("project"), rs.getFloat("midsem"), rs.getFloat("exam")));
                calculateGrade(rs.getFloat("assignment"), rs.getFloat("attendance"), rs.getFloat("project"), rs.getFloat("midsem"), rs.getFloat("exam"));
            }
            String st = "UPDATE course_registration SET assignment = ?, attendance = ?, project = ?, exam = ?, midsem = ?, grade = ? WHERE id = '" + selectedStudent.getId() + "'";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtAssignment.getText());
            preparedStatement.setString(2, txtAttendance.getText());
            preparedStatement.setString(3, txtProject.getText());
            preparedStatement.setString(4, txtMidsem.getText());
            preparedStatement.setString(5, txtExam.getText());
            preparedStatement.setString(6, grade.getText());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Updated Successfully");
            txtAssignment.clear();
            txtAttendance.clear();
            txtProject.clear();
            txtMidsem.clear();
            txtExam.clear();
            generateStudentsAndResults();

            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

}
