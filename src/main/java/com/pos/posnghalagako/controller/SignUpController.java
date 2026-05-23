package com.pos.posnghalagako.controller;

import com.pos.posnghalagako.factory.SceneFactory;
import com.pos.posnghalagako.repository.AuthRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final AuthRepository authRepository = new AuthRepository();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Input validation
        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (username.length() < 3 || username.length() > 50) {
            errorLabel.setText("Username must be 3-50 characters.");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {
            boolean success = authRepository.register(username, password, fullName);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Account created successfully! You can now log in.", ButtonType.OK);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.showAndWait();

                // Navigate back to login
                handleBackToLogin(event);
            } else {
                errorLabel.setText("Username already taken. Choose another.");
            }
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(SceneFactory.createLoginScene());
            stage.setTitle("POSngHALAGAko — Login");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}
