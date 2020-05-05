package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        stage.setTitle("Hello World");
        //stage.setScene(new Scene(root, 300, 275));



        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //grab your root here
    }


    public static void main(String[] args) {
        launch(args);
    }
}
