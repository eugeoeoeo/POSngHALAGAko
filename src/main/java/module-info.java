module com.pos.posnghalagako {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.pos.posnghalagako.controller to javafx.fxml;
    opens com.pos.posnghalagako.model to javafx.base;
    exports com.pos.posnghalagako.app;
}
