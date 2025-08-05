package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.book.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.book.views.BookListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class BookPlugin implements IPlugin {
    
    @Override
    public boolean init() {
        try {
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError();
                return false;
            }
            
            IUIController uiController = ICore.getInstance().getUIController();
            if (uiController == null) {
                System.err.println("BookPlugin: UIController not found");
                return false;
            }
            
            BookListView bookListView = new BookListView();
            
            boolean menuCreated = uiController.createMenuItem("Registrations", "Books") != null;
            
            // Create tab
            boolean tabCreated = uiController.createTab("Books", bookListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("BookPlugin: Plugin initialized successfully");
                return true;
            } else {
                System.err.println("BookPlugin: Error creating user interface");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("BookPlugin: Error during initialization" + e.getMessage());
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Could not connect to database");
            alert.setContentText("Check if the MariaDB server is running on port 3307\n" +
                               "and if the 'bookstore' bank exists.");
            alert.showAndWait();
        });
    }
}
