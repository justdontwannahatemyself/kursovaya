module com.kursovaya {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfreechart.fx;
    requires commons.math3;


    opens com.kursovaya to javafx.fxml;
    exports com.kursovaya;
}