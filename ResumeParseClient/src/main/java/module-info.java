module ru.edu.resumeparseclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens ru.edu.resumeparseclient to javafx.fxml;
    exports ru.edu.resumeparseclient;
}