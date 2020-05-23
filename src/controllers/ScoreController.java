package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import models.Course;
import models.Result;
import utilities.ConnectionUtil;

import java.net.URL;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ScoreController implements Initializable {

    @FXML
    private TextField txtAssignment;

    @FXML
    private TextField txtAttendance;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtMidSem;

    @FXML
    private TextField txtProject;

    @FXML
    private TextField txtExam;

    @FXML
    private TableView<Result> tblScores;

    @FXML
    private TableColumn<Result,String> attendance;

    @FXML
    private TableColumn<Result, String> assignment;

    @FXML
    private TableColumn<Result, String> midsem;

    @FXML
    private TableColumn<Result, String> project;

    @FXML
    private TableColumn<String, String> exam;

    @FXML
    private Label lblStatus;

    private Result score;

    ObservableList<Result> scores = FXCollections.observableArrayList();

    PreparedStatement preparedStatement;
    Connection connection;
    public void handleButtonAction(MouseEvent event) {
        saveData();

    }

    public ScoreController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    public String saveData(){
        try {
            String assignment = txtAssignment.getText();
            float assignment_1 = Float.parseFloat(assignment);
            String attendance = txtAttendance.getText();
            float attendance_1 = Float.parseFloat(attendance);
            String project = txtProject.getText();
            float project_1 = Float.parseFloat(project);
            String midsem = txtMidSem.getText();
            float midsem_1 = Float.parseFloat(midsem);
            String exam = txtExam.getText();
            float exam_1 = Float.parseFloat(exam);
            float total = assignment_1+attendance_1+project_1+midsem_1+exam_1;
            System.out.println(total);
            if(total == 100.0) {
                String st = "UPDATE scores SET assignment = ?, attendance = ?, project = ?, midsem = ?, exam = ? WHERE id = '"+score.getId()+"'";
//                String st = "INSERT INTO scores ( assignment, attendance, project, midsem, exam) VALUES (?,?,?,?,?)";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.setString(1, txtAssignment.getText());
                preparedStatement.setString(2, txtAttendance.getText());
                preparedStatement.setString(3, txtProject.getText());
                preparedStatement.setString(4, txtMidSem.getText());
                preparedStatement.setString(5, txtExam.getText());
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Saved Successfully");
                txtAssignment.clear();
                txtAttendance.clear();
                txtProject.clear();
                txtMidSem.clear();
                txtExam.clear();
                refreshTable();
            }
            else{
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Invalid Score");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Success";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String SQL = "SELECT * from scores";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                scores.add(new Result(rs.getInt("id"), rs.getFloat("assignment"), rs.getFloat("attendance"), rs.getFloat("project"), rs.getFloat("midsem"), rs.getFloat("exam")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        attendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        assignment.setCellValueFactory(new PropertyValueFactory<>("assignmentScore"));
        project.setCellValueFactory(new PropertyValueFactory<>("projectScore"));
        midsem.setCellValueFactory(new PropertyValueFactory<>("midsemScore"));
        exam.setCellValueFactory(new PropertyValueFactory<>("examScore"));
        tblScores.setItems(scores);


    }

    public void onEdt(MouseEvent event) {
        Edit();
    }
    private void Edit(){
        if(tblScores.getSelectionModel().getSelectedItem() != null){
            score = tblScores.getSelectionModel().getSelectedItem();
            txtAssignment.setText(Float.toString(score.getAssignmentScore()));
            txtAttendance.setText(Float.toString(score.getAttendance()));
            txtProject.setText(Float.toString(score.getProjectScore()));
            txtMidSem.setText(Float.toString(score.getMidsemScore()));
            txtExam.setText(Float.toString(score.getExamScore()));

        }
    }
    private void refreshTable() {
        tblScores.getItems().clear();
        try {
            String SQL = "SELECT * from scores";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                scores.add(new Result(rs.getInt("id"), rs.getFloat("assignment"), rs.getFloat("attendance"), rs.getFloat("project"), rs.getFloat("midsem"), rs.getFloat("exam")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        attendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        assignment.setCellValueFactory(new PropertyValueFactory<>("assignmentScore"));
        project.setCellValueFactory(new PropertyValueFactory<>("projectScore"));
        midsem.setCellValueFactory(new PropertyValueFactory<>("midsemScore"));
        exam.setCellValueFactory(new PropertyValueFactory<>("examScore"));
        tblScores.setItems(scores);


    }
}
