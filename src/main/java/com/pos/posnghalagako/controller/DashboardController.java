package com.pos.posnghalagako.controller;

import com.pos.posnghalagako.factory.SceneFactory;
import com.pos.posnghalagako.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;

    @FXML
    public void initialize() {
        if (SessionManager.isLoggedIn()) {
            welcomeLabel.setText("Welcome, " + SessionManager.getCurrentUser().getFullName() + "!");
            roleLabel.setText("Role: " + SessionManager.getCurrentUser().getRole().toUpperCase());
        }
    }

    @FXML
    private void handleOpenPOS(MouseEvent event) {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(SceneFactory.createPOSScene());
            stage.setTitle("POSngHALAGAko — Point of Sale");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenProducts(MouseEvent event) {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(SceneFactory.createProductScene());
            stage.setTitle("POSngHALAGAko — Product Management");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        try {
            SessionManager.logout();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(SceneFactory.createLoginScene());
            stage.setTitle("POSngHALAGAko — Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
