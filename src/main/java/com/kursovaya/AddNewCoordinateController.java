package com.kursovaya;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewCoordinateController {
    //  Выписывать ошибки в лейбл.
    @FXML
    private Label label_error;
    //  Поле для текста задаёт х.
    @FXML
    private TextField txt_x;
    //  Поле для текста задаёт у.
    @FXML
    private TextField txt_y;
    //  Ссылка чтобы начертить график.
    protected static AppController appController;
    //  Действие на кнопку.
    @FXML
    public void add_click() {
        try {
            double x = Double.parseDouble(txt_x.getText());
            double y = Double.parseDouble(txt_y.getText());
            if (y < 0) {
                label_error.setText("y должен быть > 0");
                return;
            }
            for (int i = 0; i < Logic.coordinates.size(); i++) {
                if (Logic.coordinates.get(i).getX() == x) {
                    label_error.setText("Координата с этим x уже есть");
                    return;
                }
            }
            AppController.logic.addCoordinate(new Cord(x,y));
            double[][] func = AppController.logic.findEquation();
            appController.getSecondGraph(func, func.length);
            ((Stage)(txt_x.getScene().getWindow())).close();
        } catch (NumberFormatException e) {
            // Если поле не разпознаётся как число - выводим ошибку.
            label_error.setText("Число не может быть распознано");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
