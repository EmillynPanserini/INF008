package br.edu.ifba.inf008;

import br.edu.ifba.inf008.shell.UIController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hello !
 */
@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        springContext = SpringApplication.run(App.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController uiController = springContext.getBean(UIController.class);

        uiController.setPrimaryStage(primaryStage);
        uiController.showLoginScreen();
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}