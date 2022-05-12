package com.kursovaya;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UsersDialogController {
    //  Ссылка чтобы начертить график.
    protected static AppController appController;
    @FXML
    //  Найти количество локальных минимумов.
    private TextField getAmount;
    //  При ошибке вывести.
    @FXML
    private Label cantParse;
    @FXML
    protected void setOnMouseClicked() {
        try {
            Stage stage = (Stage)(cantParse.getScene().getWindow());
            int amount = Integer.parseInt(getAmount.getText());
            if (amount < 0 || amount > 10) {
                cantParse.setText("Выберите число от 0 до 10.");
            } else {
                cantParse.setText("");
                Logic logic = new Logic(amount);
                logic.approximation();
                logic.getListOfMinimums();
                var func = logic.findEquation();
                stage.close();
                appController.getSecondGraph(func, amount);
            }
        } catch (NumberFormatException e) {
            cantParse.setText("Число не может быть распознано");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
