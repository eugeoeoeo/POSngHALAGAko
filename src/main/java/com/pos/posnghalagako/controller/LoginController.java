package com.pos.posnghalagako.controller;

import com.pos.posnghalagako.factory.SceneFactory;
import com.pos.posnghalagako.model.UserAccount;
import com.pos.posnghalagako.repository.AuthRepository;
import com.pos.posnghalagako.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthRepository authRepository = new AuthRepository();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        try {
            UserAccount user = authRepository.authenticate(username, password);
            if (user != null) {
                // Store session
                SessionManager.login(user);

                // Navigate to Dashboard
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(SceneFactory.createDashboardScene());
                stage.setTitle("POSngHALAGAko — Dashboard");
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } catch (Exception e) {
            errorLabel.setText("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenSignUp(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(SceneFactory.createSignUpScene());
            stage.setTitle("POSngHALAGAko — Sign Up");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}
