package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.user.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.user.views.UserListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UserPlugin implements IPlugin {
    
    @Override
    public boolean init() {
        try {
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError();
                return false;
            }
            
            IUIController uiController = ICore.getInstance().getUIController();
            if (uiController == null) {
                System.err.println("UserPlugin: UIController not found");
                return false;
            }

            UserListView userListView = new UserListView();
            
            boolean menuCreated = uiController.createMenuItem("Registrations", "Users") != null;
            
            boolean tabCreated = uiController.createTab("Users", userListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("UserPlugin: Plugin initialized successfully");
                return true;
            } else {
                System.err.println("UserPlugin: Error creating user interface");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("UserPlugin: Error during initialization " + e.getMessage());
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Could not connect to database");
            alert.setContentText("Check if the MariaDB server is running on port 3307\n" +
                               "and whether the 'bookstore' bank exists.");
            alert.showAndWait();
        });
    }
}
