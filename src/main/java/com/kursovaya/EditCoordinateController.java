package com.kursovaya;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCoordinateController {
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
    public void edit_click() {
        try {
            double x = Double.parseDouble(txt_x.getText());
            double y = Double.parseDouble(txt_y.getText());
            if (y < 0) {
                label_error.setText("Координата не может быть меньше нуля");
                return;
            }
            Cord cord = null;
            double new_x = 0, new_y = 0;
            boolean flag = false;
            for (int i = 0; i < Logic.coordinates.size(); i++) {
                if (Logic.coordinates.get(i).getX() == x) {
                    cord = Logic.coordinates.get(i);
                    flag = true;
                    new_x = x;
                    new_y = y;
                    break;
                }
            }
            if (!flag) {
                label_error.setText("Координата с этим x не задана");
                return;
            }
            AppController.logic.changeCoordinate(cord, new Cord(new_x, new_y));
            var func = AppController.logic.findEquation();
            appController.getSecondGraph(func, func.length);
            ((Stage)(txt_x.getScene().getWindow())).close();
        } catch (NumberFormatException e) {
            label_error.setText("Число не может быть распознано");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
