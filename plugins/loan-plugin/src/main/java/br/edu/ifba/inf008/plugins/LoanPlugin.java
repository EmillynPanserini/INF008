package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.loan.views.LoanListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class LoanPlugin implements IPlugin {
    
    @Override
    public boolean init() {
        try {
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError();
                return false;
            }
            
            IUIController uiController = ICore.getInstance().getUIController();
            if (uiController == null) {
                System.err.println("LoanPlugin: UIController not found");
                return false;
            }
            
            LoanListView loanListView = new LoanListView();
            
            boolean menuCreated = uiController.createMenuItem("Operations",
                                                            "Loans") != null;

            boolean tabCreated = uiController.createTab("Loans", loanListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("LoanPlugin: Plugin initialized");
                return true;
            } else {
                System.err.println("LoanPlugin: Error creating user interface");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("LoanPlugin: Error during initialization - " + e.getMessage());
            java.util.logging.Logger.getLogger(LoanPlugin.class.getName()).log(java.util.logging.Level.SEVERE, "Erro durante inicialização", e);
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Error connecting to database");
            alert.setContentText("Unable to establish database connection.\n");
            alert.showAndWait();
        });
    }
}
