package com.kursovaya;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UsersDialog {
    public void display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("UserDialog.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(fxmlLoader.load(), 325, 168);
        stage.setResizable(false);
        stage.setTitle("Количество локальных минимумов.");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
