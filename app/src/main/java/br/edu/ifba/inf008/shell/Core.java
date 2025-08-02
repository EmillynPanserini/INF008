package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.*;
import br.edu.ifba.inf008.interfaces.IDatabaseService;
import br.edu.ifba.inf008.service.DatabaseService;
import org.springframework.stereotype.Component; // Importar @Component
import org.springframework.beans.factory.annotation.Autowired; // Importar @Autowired

@Component
public class Core extends ICore
{

    private IUIController uiController; // UIController será injetado pelo Spring
    private IAuthenticationController authenticationController; // Injetado
    private IIOController ioController; // Injetado
    private IPluginController pluginController; // Injetado
    private IDatabaseService databaseService; // Injetado

    @Autowired // Construtor para injeção de dependências
    public Core(IUIController uiController,
                IAuthenticationController authenticationController,
                IIOController ioController,
                IPluginController pluginController,
                IDatabaseService databaseService) {
        this.uiController = uiController;
        this.authenticationController = authenticationController;
        this.ioController = ioController;
        this.pluginController = pluginController;
        this.databaseService = databaseService;
    }


    @Override
    public IUIController getUIController() {
        return uiController;
    }
    @Override
    public IAuthenticationController getAuthenticationController() {
        return authenticationController;
    }
    @Override
    public IIOController getIOController() {
        return ioController;
    }
    @Override
    public IPluginController getPluginController() {
        return pluginController;
    }
    @Override
    public IDatabaseService getDatabaseService() {
        return databaseService;
    }

}