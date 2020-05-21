package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utilities.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private Label lblStatus;

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
            int assignment_1 = Integer.parseInt(assignment);
            String attendance = txtAttendance.getText();
            int attendance_1 = Integer.parseInt(attendance);
            String project = txtProject.getText();
            int project_1 = Integer.parseInt(project);
            String midsem = txtMidSem.getText();
            int midsem_1 = Integer.parseInt(midsem);
            String exam = txtExam.getText();
            int exam_1 = Integer.parseInt(exam);
            int total = assignment_1+attendance_1+project_1+midsem_1+exam_1;
            System.out.println(total);
            if(total == 100) {
                String st = "INSERT INTO scores ( assignment, attendance, project, midsem, exam) VALUES (?,?,?,?,?)";
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

    }
}
