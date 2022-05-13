package com.kursovaya;

//  Класс координат.
public class Cord implements Comparable<Cord> {
    private final double x;
    private double y;
    Cord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected double getX() {
        return x;
    }

    protected double getY() {
        return y;
    }

    protected void setY(double y) {
        this.y = y;
    }
    //  При отрисовки графика, оказывается, важно, чтобы массив/список координат
    //  Был отсортирован.
    @Override
    public int compareTo(Cord o) {
        if (x > o.getX()) {
            return 1;
        } else {
            return -1;
        }
    }
}
