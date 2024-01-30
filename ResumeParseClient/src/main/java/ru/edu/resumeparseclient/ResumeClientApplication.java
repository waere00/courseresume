package ru.edu.resumeparseclient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ResumeClientApplication {
    private String fxml;
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Register.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 350);
        stage.setTitle("Система для работы с резюме соискателей");
        stage.setMaxHeight(400);
        stage.setMaximized(false);
        stage.setMaxWidth(1200);
        stage.setScene(scene);
        stage.show();
    }
    public ResumeClientApplication(String fxml){
        this.fxml = fxml;
    }
}