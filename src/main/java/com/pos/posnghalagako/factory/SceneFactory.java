package com.pos.posnghalagako.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Factory for creating all application scenes from FXML files.
 */
public final class SceneFactory {

    private static final String BASE = "/com/pos/posnghalagako/";

    private SceneFactory() {
    }

    public static Scene createLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(BASE + "login-view.fxml"));
        return new Scene(loader.load(), 400, 350);
    }

    public static Scene createSignUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(BASE + "signup-view.fxml"));
        return new Scene(loader.load(), 400, 450);
    }

    public static Scene createDashboardScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(BASE + "dashboard-view.fxml"));
        return new Scene(loader.load(), 600, 400);
    }

    public static Scene createPOSScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(BASE + "pos-view.fxml"));
        return new Scene(loader.load(), 900, 550);
    }

    public static Scene createProductScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(BASE + "products-view.fxml"));
        return new Scene(loader.load(), 850, 550);
    }
}
