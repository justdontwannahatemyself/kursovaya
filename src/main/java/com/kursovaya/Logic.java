package com.kursovaya;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Logic {
    //  Количество минимумов
    private int amount;
    //  Список координат.
    protected static List<Cord> coordinates;
    //  Список минимумов.
    private final List<Cord> minimums;
    //  Искомые коэффициенты.
    private double[][] result;

    Logic(int amount) {
        minimums = new ArrayList<>();
        this.amount = amount;
        AppController.logic = this;
    }
    //  Добавление координаты (также является опорной).
    void addCoordinate(Cord add) {
        coordinates.add(add);
        minimums.add(add);
        amount += 1;
    }
    //  Изменение одной из координат (становится опорной.)
    void changeCoordinate(Cord old, Cord new_c) {
        for (int i = 0; i < minimums.size(); i++) {
            if (old == minimums.get(i)) {
                minimums.remove(i);
                amount--;
                break;
            }
        }
        for (int i = 0; i < coordinates.size(); i++) {
            if (old == coordinates.get(i)) {
                coordinates.remove(i);
                coordinates.add(new_c);
                amount++;
                minimums.add(new_c);
                break;
            }
        }
    }
    //  Сглаживание графика.
     void approximation() {
        for (int i = 1; i < coordinates.size(); i++) {
            coordinates.get(i).setY(
                    getYOfCoordinate(coordinates.get(i - 1).getY(),
                            coordinates.get(i).getY())
            );
        }
    }
    //  Метод для сглаживания.
    static double getYOfCoordinate(double prev, double cur) {
        // Если разница < 2% - нас всё устраивает.
        double difference = prev / 50;
        if (Math.abs(prev - cur) < difference) {
            return prev;
        } else {
            return cur;
        }
    }
    //  Список минимумов.
    void getListOfMinimums() {
        int startCoordinate = 0;
        int getCapacity = coordinates.size() / amount;
        for (int i = 0; i < amount; i++) {
            double min = Double.MAX_VALUE;
            Cord cord = new Cord(0, -1);
            for (int j = startCoordinate; j < startCoordinate + getCapacity; j++) {
                if (coordinates.get(j).getY() < min) {
                    min = coordinates.get(j).getY();
                    cord = coordinates.get(j);
                }
            }
            minimums.add(cord);
            startCoordinate += getCapacity;
        }
    }
    //  Алгоритм решения.
    double[][] findEquation() {
        //  Список констант (y)
        double[][] constants = new double[amount][1];
        double[][] mat = new double[amount][amount];
        for (int i = 0; i < minimums.size(); i++) {
            Cord cord = minimums.get(i);
            for (int j = 0; j < minimums.size(); j++) {
                mat[i][j] = Math.pow(cord.getX(), minimums.size() - 1 - j);
            }
            constants[i][0] = cord.getY();
        }
        double[][] inverted_mat = invert(mat);
        result = new double[amount][1];
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < amount; k++) {
                    result[i][j] += inverted_mat[i][k] * constants[k][j];
                }
            }
        }
        double sum = 0;
        for (int i = 0; i < amount; i++) {
            sum += result[i][0] * Math.pow(minimums.get(0).getX(), amount - i - 1);
        }
        assert (Math.abs(sum - minimums.get(0).getY()) < 0.00001);
        try {
            FileWriter myWriter = new FileWriter("minimums.txt");
            for (Cord minimum : minimums) {
                myWriter.write(("x: " + minimum.getX()
                        + ", y: " + minimum.getY() +
                        " \n"));
            }
            myWriter.write("function: " + this);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //  Поиск обратной матрицы.
    public static double[][] invert(double[][] matrix) {
        int len = matrix.length;
        double[][] x = new double[len][len];
        double[][] b = new double[len][len];
        int[] index = new int[len];
        for (int i = 0; i < len; ++i) {
            b[i][i] = 1;
        }
        gaussian(matrix, index);
        for (int i = 0; i < len - 1; ++i)
            for (int j = i + 1; j < len; ++j)
                for (int k = 0; k < len; ++k)
                    b[index[j]][k]
                            -= matrix[index[j]][i] * b[index[i]][k];
        for (int i = 0; i < len; ++i) {
            x[len - 1][i] = b[index[len - 1]][i] / matrix[index[len - 1]][len - 1];
            for (int j = len - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < len; ++k) {
                    x[j][i] -= matrix[index[j]][k] * x[k][i];
                }
                x[j][i] /= matrix[index[j]][j];
            }
        }
        return x;
    }
    //  Из матрицы сделать верхнетреугольную.
    public static void gaussian(double[][] a, int[] index) {
        int n = index.length;
        double[] c = new double[n];
        for (int i = 0; i < n; ++i) {
            index[i] = i;
        }
        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) {
                    c1 = c0;
                }
            }
            c[i] = c1;
        }
        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }
            int ind = index[j];
            index[j] = index[k];
            index[k] = ind;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];
                a[index[i]][j] = pj;
                for (int l = j + 1; l < n; ++l) {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
    }
    //  Как мы записываем логику в строку.
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("y=");
        for (int i = 0; i < amount; i++) {
            if (i == 0 && result[0][0] > 0) {
                output.append(" ").append(String.format("%.4f",result[0][0])).append("x^").append(amount - 1);
            } else if (i != amount - 1) {
                if (result[i][0] > 0) {
                    output.append(" + ").append(String.format("%.4f",result[i][0])).append("x^").append(amount - i - 1);
                } else {
                    output.append(String.format("%.4f",result[i][0])).append("x^").append(amount - i - 1);
                }
            } else {
                if (result[i][0] > 0) {
                    output.append(" + ").append(String.format("%.4f",result[i][0]));
                } else {
                    output.append(String.format("%.4f",result[i][0]));
                }
            }

        }
        return output.toString();
    }
}
