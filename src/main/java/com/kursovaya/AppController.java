package com.kursovaya;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class AppController {
    //  Логика программы.
    protected static Logic logic;
    //  График
    @FXML
    private LineChart<Double, Double> chart;
    //  Ось х.
    @FXML
    private NumberAxis xAxis;
    //  Ось y.
    @FXML
    private NumberAxis yAxis;
    //  лейбл.
    @FXML
    private Label label;
    private List<Cord> coordinates;

    //  Обработка нажатия кнопки добавления точки (опорной).
    @FXML
    private void newPointClick() throws IOException {
        if (chart.getData().size() < 2) {
            return;
        }
        AddNewCoordinate addNewCoordinate = new AddNewCoordinate();
        addNewCoordinate.display();
    }
    //  Обработка нажатия кнопки редактирования точки.
    @FXML
    private void pointEdit() throws IOException {
        if (chart.getData().size() < 2) {
            return;
        }
        EditCoordinate editCoordinate = new EditCoordinate();
        editCoordinate.display();
    }
    //  Добавление дифрактограммы.
    @FXML
    protected void addDiffractograms() {
        chart.getData().clear();
        coordinates = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".txt", "*.txt"),
                new FileChooser.ExtensionFilter(".sp", "*.sp"),
                new FileChooser.ExtensionFilter(".dat", "*.dat")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
            if (selectedFile != null) {
                Scanner scanner = new Scanner(selectedFile);
                while (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    String[] abc = input.split(" ");
                    Cord cord = new Cord(Double.parseDouble(abc[0]), Double.parseDouble(abc[abc.length - 1]));
                    coordinates.add(cord);
                }
            } else {
                label.setText("Файл не выбран.");
                return;
            }
            coordinates.sort(Cord::compareTo);
            Logic.coordinates = coordinates;
            getChartAndDisplayDialog();
        } catch (NumberFormatException e) {
            label.setText("Неправильный формат данных файла.");
        } catch (FileNotFoundException e) {
            label.setText("Файл не был найден.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //  Добавление графика.
    private void getChartAndDisplayDialog() throws IOException {
        xAxis.setLabel("2Q, град.");
        yAxis.setLabel("Интенсивность");
        yAxis.setUpperBound(getYMin() +2500);
        xAxis.setLowerBound(getXMin() - 1);
        xAxis.setUpperBound(getXMax());
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Изначальная дифрактограмма");
        for (Cord coordinate : coordinates) {
            series.getData().add(new XYChart.Data<>(coordinate.getX(), coordinate.getY()));
        }
        chart.getData().add(series);
        UsersDialog usersDialog = new UsersDialog();
        UsersDialogController.appController = this;
        AddNewCoordinateController.appController = this;
        EditCoordinateController.appController = this;
        usersDialog.display();
    }
    //  Поиск минимального у.
    double getYMin() {
        double min = Double.MAX_VALUE;
        for (Cord coordinate : Logic.coordinates) {
            if (coordinate.getY() < min) {
                min = coordinate.getY();
            }
        }
        return min;
    }
    //  Поиск минимального х.
    double getXMin() {
        double min = Double.MAX_VALUE;
        for (Cord coordinate : Logic.coordinates) {
            if (coordinate.getX() < min) {
                min = coordinate.getX();
            }
        }
        return min;
    }
    //  Поиск максимального х.
    double getXMax() {
        double max = Double.MIN_VALUE;
        for (Cord coordinate : Logic.coordinates) {
            if (coordinate.getX() > max) {
                max = coordinate.getX();
            }
        }
        return max;
    }
    // Рисовка кривой (в случае с добавлением/редактированием - тоже).
    protected void getSecondGraph(double[][] res, int amount) {
        if (chart.getData().size() == 2) {
            chart.getData().remove(1);
        }
        double startX = getXMin();
        double endX = getXMax();
        Logic.coordinates.sort(Cord::compareTo);
        yAxis.setUpperBound(getYMin() + 2500);
        yAxis.setLowerBound(getYMin()-100);
        yAxis.setForceZeroInRange(false);
        xAxis.setLowerBound(startX - 1);
        xAxis.setUpperBound(endX);
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(logic.toString());
        double y = 0;
        while (startX < endX) {
            for (int i = 0; i < amount; i++) {
                y += res[i][0] * Math.pow(startX, amount - i - 1);
            }
            XYChart.Data<Double, Double> data = new XYChart.Data<>(startX, y);
            series.getData().add(data);
            startX += 0.25;
            y = 0;
        }
        if (startX != endX) {
            for (int i = 0; i < amount; i++) {
                y += res[i][0] * Math.pow(endX, amount - i - 1);
            }
            XYChart.Data<Double, Double> data = new XYChart.Data<>(endX, y);
            series.getData().add(data);
        }
        chart.getData().add(series);
    }
}