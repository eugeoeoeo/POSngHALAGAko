package com.pos.posnghalagako.app;

import com.pos.posnghalagako.factory.SceneFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class POSApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(SceneFactory.createLoginScene());
        stage.setTitle("POSngHALAGAko — Login");
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
