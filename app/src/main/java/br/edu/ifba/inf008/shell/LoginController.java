package br.edu.ifba.inf008.shell;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextFlow loginMsg;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldPassword;


    @FXML
    public void initialize() {
        if (loginMsg != null) {
            loginMsg.setVisible(false);
        }
    }

    @FXML
    void handleRegister(MouseEvent event) {
        System.out.println("Register clicked!");
        //  Chamar um metodo no UIController para mudar para uma tela de registro
    }

    @FXML
    void handlerLoginButton(ActionEvent event) {
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();

        System.out.println("Attempting login with: " + email + " / " + password);

        // Exemplo de lógica de login (substitua pela sua autenticação real)
        if ("test@example.com".equals(email) && "password".equals(password)) {
            if (loginMsg != null) {
                loginMsg.setVisible(true);
                loginMsg.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px;"); // Verde para sucesso
                ((javafx.scene.text.Text) loginMsg.getChildren().get(0)).setText("Login successful!");
            }
            System.out.println("Login successful!");
            // chamar um metodo no UIController para mudar para a tela principal
            // Ex: UIController.getInstance().showMainApplication();
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