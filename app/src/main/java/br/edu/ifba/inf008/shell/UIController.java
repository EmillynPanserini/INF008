package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component // UIController é um bean Spring
public class UIController implements IUIController { // CORRIGIDO: Não estende mais Application

    @Autowired
    private ConfigurableApplicationContext springContext;

    @Autowired
    @Lazy
    private ICore core;

    private Stage primaryStage; // ADICIONADO: Campo para armazenar o Stage principal
    private TabPane tabPane;
    private Map<String, Tab> openTabs = new HashMap<>();

    public UIController() {
        // Construtor padrão. Spring gerencia a criação e injeção.
    }

    // ADICIONADO: Setter para o Stage principal (chamado de App.start())
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // CORRIGIDO: Este é o método que será chamado para iniciar a UI (antigo start())
    public void showLoginScreen() {
        this.primaryStage.setTitle("Library System"); // Define o título da janela

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/LoginView.fxml"));
            // springContext agora está injetado e NÃO é null
            loader.setControllerFactory(springContext::getBean); // Setar o ControllerFactory
            Parent loginRoot = loader.load();

            LoginController loginController = loader.getController();
            if (loginController != null) {
                // Passar a referência do UIController para o LoginController
                loginController.setUiController(this);
            }

            Scene scene = new Scene(loginRoot, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar LoginView.fxml: " + e.getMessage());
            e.printStackTrace();
            primaryStage.setScene(new Scene(new Label("Erro ao carregar tela de login.")));
            primaryStage.show();
        }
    }

    /**
     * Método para ser chamado após o login bem-sucedido para exibir a aplicação principal.
     */
    public void showMainApplication() {
        BorderPane mainLayout = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> System.exit(0));
        fileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(fileMenu);
        mainLayout.setTop(menuBar);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        mainLayout.setCenter(tabPane);

        primaryStage.setScene(new Scene(mainLayout, 800, 600));
        primaryStage.setTitle("Library System - Main App");

        // Agora usa o 'core' injetado
        core.getPluginController().init();
    }

    @Override
    public MenuItem createMenuItem(String menuText, String menuItemText) {
        if (primaryStage.getScene().getRoot() instanceof BorderPane) {
            BorderPane currentRoot = (BorderPane) primaryStage.getScene().getRoot();
            if (currentRoot.getTop() instanceof MenuBar) {
                MenuBar menuBar = (MenuBar) currentRoot.getTop();

                Menu newMenu = menuBar.getMenus().stream()
                        .filter(m -> m.getText().equals(menuText))
                        .findFirst()
                        .orElseGet(() -> {
                            Menu menu = new Menu(menuText);
                            menuBar.getMenus().add(menu);
                            return menu;
                        });

                MenuItem menuItem = new MenuItem(menuItemText);
                newMenu.getItems().add(menuItem);
                return menuItem;
            }
        }
        System.err.println("MenuBar não encontrada na cena principal para adicionar menuItem.");
        return null;
    }

    @Override
    public boolean createTab(String tabText, Node contents) {
        if (tabPane != null) {
            if (!openTabs.containsKey(tabText)) {
                Tab newTab = new Tab(tabText);
                newTab.setContent(contents);
                tabPane.getTabs().add(newTab);
                openTabs.put(tabText, newTab);
                newTab.setOnClosed(event -> openTabs.remove(tabText));
            }
            tabPane.getSelectionModel().select(openTabs.get(tabText));
            return true;
        }
        System.err.println("TabPane não inicializado para criar aba.");
        return false;
    }
}