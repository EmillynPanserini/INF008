package br.edu.ifba.inf008.infrastructure.shell;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Importar FXMLLoader
import javafx.scene.Node; // Importar Node
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane; // Importar AnchorPane
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HomeController {

    @FXML private AnchorPane contentArea;

    @Autowired private ConfigurableApplicationContext springContext;
    @Setter
    private UIController uiController;


    @FXML
    void handleMenu(MouseEvent event) {
        System.out.println("Menu clicked!");
    }

    @FXML
    void handleMenuBooks(MouseEvent event) {
        System.out.println("Books Menu clicked!");
    }

    @FXML
    void handleMenuExit(MouseEvent event) {
        System.out.println("Exit Menu clicked!");
        System.exit(0); // Sair da aplicação
    }

    @FXML
    void handleMenuHome(MouseEvent event) {
        System.out.println("Home Menu clicked!");
    }

    @FXML
    void handleMenuLoan(MouseEvent event) { System.out.println("Loan Menu clicked!"); }

    @FXML
    void handleMenuLoanReports(MouseEvent event) { System.out.println("Loan Reports Menu clicked!"); }

    @FXML
    void handleMenuProfile(ActionEvent event) { System.out.println("Profile Menu clicked!"); }

    @FXML
    void handleMenuUsers(MouseEvent event) {
        System.out.println("Users Menu clicked! Loading User Registration Form...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/plugins/ui/view/UserRegstration.fxml"));
            // Definir o ControllerFactory para que o Spring injete dependências no UserRegistrationController
            loader.setControllerFactory(springContext::getBean);
            Node userRegistrationForm = loader.load();

            if (contentArea != null) {
                contentArea.getChildren().setAll(userRegistrationForm);
                AnchorPane.setTopAnchor(userRegistrationForm, 0.0);
                AnchorPane.setBottomAnchor(userRegistrationForm, 0.0);
                AnchorPane.setLeftAnchor(userRegistrationForm, 0.0);
                AnchorPane.setRightAnchor(userRegistrationForm, 0.0);
            } else {
                System.err.println("contentArea not injected!");
            }
        } catch (IOException e) {
            System.err.println("Failed to load UserRegistrationView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


}