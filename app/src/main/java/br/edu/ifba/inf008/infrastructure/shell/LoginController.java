package br.edu.ifba.inf008.infrastructure.shell;

import br.edu.ifba.inf008.application.usecase.LoginUseCase;
import br.edu.ifba.inf008.interfaces.ILoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
public class LoginController implements ILoginController {

    @FXML
    private TextFlow loginMsg;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldPassword;

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Setter
    private UIController uiController;

    @FXML
    public void initialize() {
        if (loginMsg != null) {
            loginMsg.setVisible(false);
        }
    }

    //Implementar
    @FXML
    public void handleRegister(MouseEvent event) {
        System.out.println("Register clicked!");
        // Exemplo: uiController.showRegistrationScreen();
    }

    @FXML
    public void handlerLoginButton(ActionEvent event) {

        String email = textFieldEmail.getText();
        boolean isAuthenticated = loginUseCase.authenticate(email);

        if (isAuthenticated) {
            if (loginMsg != null) {
                loginMsg.setVisible(true);
                loginMsg.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px;");
                ((javafx.scene.text.Text) loginMsg.getChildren().get(0)).setText("Login successful!");
            }
            System.out.println("Login successful!");
            if (uiController != null) {
                uiController.showMainApplication();
            } else {
                System.err.println("UIController not injected into LoginController!");
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