package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIController extends Application implements IUIController {
    private static UIController instance;
    private Stage primaryStage;
    private TabPane tabPane;
    private Map<String, Tab> openTabs = new HashMap<>();

    public UIController() {
        instance = this;
    }

    public static UIController getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Library System");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/LoginView.fxml"));
            Parent loginRoot = loader.load();

            LoginController loginController = loader.getController();
            // if (loginController != null) { loginController.setUiController(this); }

            Scene scene = new Scene(loginRoot, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar LoginView.fxml: " + e.getMessage());
            e.printStackTrace();
            // Fallback: Exibir uma tela de erro simples se o login FXML falhar
            primaryStage.setScene(new Scene(new Label("Erro ao carregar tela de login.")));
            primaryStage.show();
        }

        // deve ocorrer APENAS após o login bem-sucedido.
        // Para isso, LoginController precisaria chamar um metodo em UIController.
        // Exemplo de metodo que UIController chamaria para exibir a app principal:
        // showMainApplication();
    }

    /**
     * Metodo para ser chamado após o login bem-sucedido para exibir a aplicação principal.
     */
    public void showMainApplication() {




    }



}