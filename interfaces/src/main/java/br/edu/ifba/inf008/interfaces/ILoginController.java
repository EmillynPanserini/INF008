package br.edu.ifba.inf008.interfaces;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface ILoginController {

    public void initialize();
    void handleRegister(MouseEvent event);
    void handlerLoginButton(ActionEvent event);
}
