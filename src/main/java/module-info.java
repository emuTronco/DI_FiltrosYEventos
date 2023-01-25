module com.example.di_filtrosyeventos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.di_filtrosyeventos to javafx.fxml;
    exports com.example.di_filtrosyeventos;
}