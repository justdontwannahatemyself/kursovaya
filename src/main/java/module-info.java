module com.kursovaya {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kursovaya to javafx.fxml;
    exports com.kursovaya;
}