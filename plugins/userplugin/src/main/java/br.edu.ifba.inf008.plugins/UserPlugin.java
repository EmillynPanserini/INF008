package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.*;


//import br.edu.ifba.inf008.plugins.controller.UserViewController;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;


public class UserPlugin implements IPlugin, IRequiresDatabaseService {

    private IDatabaseService databaseService;



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

        //IUIController uiController = ICore.getInstance().getUIController();


        return false;
    }
}