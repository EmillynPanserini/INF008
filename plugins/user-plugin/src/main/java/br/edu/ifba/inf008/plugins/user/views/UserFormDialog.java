package br.edu.ifba.inf008.plugins.user.views;

import br.edu.ifba.inf008.plugins.user.components.UserFormComponent;
import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.interfaces.models.User;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

public class UserFormDialog extends Dialog<User> {
    private UserFormComponent formComponent;
    private User originalUser;
    private UserDAO userDAO;
    
    public UserFormDialog(User user) {
        this.originalUser = user;
        this.userDAO = new UserDAO();
        
        initializeDialog();
        setupDialog();
    }
    
    private void initializeDialog() {
        formComponent = new UserFormComponent(null); // Pass null for callback
        
        if (originalUser != null) {
            setTitle("Edit User");
            setHeaderText("Edit user information");
            formComponent.setUser(originalUser);
        } else {
            setTitle("New User");
            setHeaderText("Add a new user");
            formComponent.clearForm();
        }
    }
    
    private void setupDialog() {
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        
        getDialogPane().setContent(formComponent);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                User user = formComponent.getCurrentUser();
                if (user != null && formComponent.validateInput()) {
                    if (isEmailTaken(user)) {
                        showEmailError();
                        return null;
                    }
                    return user;
                }
                return null;
            }
            return null;
        });
        
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        Button cancelButton = (Button) getDialogPane().lookupButton(cancelButtonType);
        
        saveButton.setDisable(true);
        formComponent.setValidationCallback((isValid) -> saveButton.setDisable(!isValid));
        
        setOnShown(e -> formComponent.focusNameField());
    }
    
    private boolean isEmailTaken(User user) {
        try {
            int excludeId = originalUser != null ? originalUser.getId() : -1;
            return userDAO.emailExists(user.getEmail(), excludeId);
        } catch (Exception e) {
            showDatabaseError("Error checking email", e);
            return true;
        }
    }
    
    private void showEmailError() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Email already exists");
        alert.setHeaderText("Email already registered");
        alert.showAndWait();
    }
    
    private void showDatabaseError(String title, Exception e) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("System Error");
        alert.setHeaderText(title);
        alert.setContentText("Error accessing the database: " + e.getMessage());
        alert.showAndWait();
    }
}
