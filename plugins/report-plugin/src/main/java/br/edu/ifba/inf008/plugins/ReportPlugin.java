package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.report.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.report.views.LoanReportView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ReportPlugin implements IPlugin {
    
    @Override
    public boolean init() {
        try {
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError();
                return false;
            }
            
            IUIController uiController = ICore.getInstance().getUIController();
            if (uiController == null) {
                System.err.println("ReportPlugin: UIController not found");
                return false;
            }
            
            LoanReportView reportView = new LoanReportView();
            
            boolean menuCreated = uiController.createMenuItem("Loans",
                                                            "Loans Report") != null;
            
            boolean tabCreated = uiController.createTab("Loans Report", reportView);
            
            if (menuCreated && tabCreated) {
                System.out.println("ReportPlugin: Plugin initialized");
                return true;
            } else {
                System.err.println("ReportPlugin: Error creating user interface");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("ReportPlugin: Error during initialization" + e.getMessage());
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Error connecting to database\n");
            alert.setContentText("Unable to establish database connection");
            alert.showAndWait();
        });
    }
}
