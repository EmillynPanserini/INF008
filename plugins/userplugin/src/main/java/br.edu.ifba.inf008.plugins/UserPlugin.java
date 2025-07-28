package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.*;


import br.edu.ifba.inf008.plugins.controller.UserViewController;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;


public class UserPlugin implements IPlugin, IRequiresDatabaseService {

    private IDatabaseService databaseService;

    @Override
    public String getId() {
        return "user-management";
    }

    @Override
    public String getCapabilities() {
        return "User Management (CRUD, Listing)";
    }

    @Override
    public void setDatabaseService(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public boolean init() {
        if (databaseService == null) {
            System.err.println("UserPluginImpl: DatabaseService not injected. Initialization failed.");
            return false;
        }

        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Manage", "Users"); // Menu "Manage" -> "Users"
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Instanciar o controlador da UI do usuário, passando o databaseService
                UserViewController userView = new UserViewController(databaseService);
                uiController.createTab("User Management", userView.getView());
            }
        });

        System.out.println("User Management Plugin initialized successfully.");
        return true;
    }
}