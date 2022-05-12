package com.kursovaya;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EditCoordinate {
    public void display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("EditCoordinate.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(fxmlLoader.load(), 232, 212);
        stage.setResizable(false);
        stage.setTitle("Изменить координату");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
