package br.edu.ifba.inf008.infrastructure.shell;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UIController implements IUIController {

    @Autowired
    private ConfigurableApplicationContext springContext;

    @Autowired
    @Lazy
    private ICore core;

    @Setter
    private Stage primaryStage;
    private TabPane tabPane; // Este TabPane é do layout principal. Será obtido do HomeController se ele tiver um fx:id.
    private Map<String, Tab> openTabs = new HashMap<>();

    public UIController() {
    }

    public void showLoginScreen() {
        this.primaryStage.setTitle("Library System");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/LoginView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent loginRoot = loader.load();

            LoginController loginController = loader.getController();
            if (loginController != null) {
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

    public void showMainApplication() {
        try {
            // Carregar o FXML da tela principal (home.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/home/home.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent mainAppRoot = loader.load();


            HomeController homeController = loader.getController();
            if (homeController != null) {
                homeController.setUiController(this);
            }

            primaryStage.setScene(new Scene(mainAppRoot, 900, 600));
            primaryStage.setTitle("Library System - Main App");
            primaryStage.show();

            // Inicializa os plugins APÓS a UI principal estar configurada
            // Passa 'this' (UIController) e 'springContext' para o PluginController.init()
            core.getPluginController().init();

        } catch (IOException e) {
            System.err.println("Erro ao carregar home.fxml ou inicializar Main Application: " + e.getMessage());
            e.printStackTrace();
            // Fallback em caso de erro ao carregar a tela principal
            primaryStage.setScene(new Scene(new Label("Erro ao carregar aplicação principal.")));
            primaryStage.show();
        }
    }

    @Override
    public MenuItem createMenuItem(String menuText, String menuItemText) {
        // Este metodo precisa da MenuBar do HomeController
        // Se a MenuBar tiver fx:id="mainMenuBar" em home.fxml, HomeController poderia expor um getter.
        // Ou o UIController poderia manter uma referência a ela.

        // Por enquanto, vamos manter a lógica anterior de buscar no root,
        // mas idealmente, HomeController forneceria um metodo para adicionar itens.
        if (primaryStage.getScene().getRoot() instanceof BorderPane currentRoot) {
            if (currentRoot.getTop() instanceof MenuBar menuBar) {

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
        // Este metodo precisa do TabPane do HomeController
        // Se o TabPane tiver fx:id="mainTabPane" em home.fxml, HomeController poderia expor um getter.
        // Ou o UIController poderia manter uma referência a ele.
        if (tabPane == null && primaryStage.getScene().getRoot() instanceof BorderPane) {
            BorderPane mainLayout = (BorderPane) primaryStage.getScene().getRoot();
            if (mainLayout.getCenter() instanceof TabPane) {
                this.tabPane = (TabPane) mainLayout.getCenter();
            }
        }

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