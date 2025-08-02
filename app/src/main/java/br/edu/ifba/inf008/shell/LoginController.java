package br.edu.ifba.inf008.shell;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component; // Se você ainda não o tem, adicione este import e a anotação @Component
import org.springframework.beans.factory.annotation.Autowired; // Se você não o tem, adicione este import

@Component // Adicione @Component se ainda não o fez, para que o Spring o gerencie
public class LoginController {

    @FXML
    private TextFlow loginMsg;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldPassword;

    // ADICIONADO: Campo para a referência do UIController
    private UIController uiController;

    // ADICIONADO: Setter para injetar a referência do UIController
    // @Autowired // Se LoginController fosse um bean criado pelo Spring diretamente, usaria @Autowired
    public void setUiController(UIController uiController) {
        this.uiController = uiController;
    }

    @FXML
    public void initialize() {
        if (loginMsg != null) {
            loginMsg.setVisible(false);
        }
    }

    @FXML
    void handleRegister(MouseEvent event) {
        System.out.println("Register clicked!");
        // Exemplo: uiController.showRegistrationScreen();
    }

    @FXML
    void handlerLoginButton(ActionEvent event) {
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();

        System.out.println("Attempting login with: " + email + " / " + password);

        if ("test@example.com".equals(email) && "password".equals(password)) {
            if (loginMsg != null) {
                loginMsg.setVisible(true);
                loginMsg.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px;");
                ((javafx.scene.text.Text) loginMsg.getChildren().get(0)).setText("Login successful!");
            }
            System.out.println("Login successful!");
            // ADICIONADO: Chamar o método para mostrar a aplicação principal
            if (uiController != null) {
                uiController.showMainApplication();
            } else {
                System.err.println("UIController não injetado no LoginController!");
            }
        } else {
            if (loginMsg != null) {
                loginMsg.setVisible(true);
                loginMsg.setStyle("-fx-background-color: #FF5722; -fx-padding: 10px;");
                ((javafx.scene.text.Text) loginMsg.getChildren().get(0)).setText("Invalid credentials. Try again.");
            }
            System.err.println("Login failed!");
        }
    }
}