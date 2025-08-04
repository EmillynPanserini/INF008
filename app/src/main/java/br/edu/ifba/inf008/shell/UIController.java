package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIController extends Application implements IUIController {
    private MenuBar menuBar;
    private TabPane tabPane;
    private static UIController uiController;

    public UIController() {}

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library management system");

        menuBar = new MenuBar();
        tabPane = new TabPane();
        tabPane.setSide(Side.TOP);

        VBox vBox = new VBox(menuBar, tabPane);
        Scene scene = new Scene(vBox, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Core.getInstance().getPluginController().init();
    }

    @Override
    public MenuItem createMenuItem(String menuText, String menuItemText) {
        Menu targetMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                targetMenu = menu;
                break;
            }
        }
        if (targetMenu == null) {
            targetMenu = new Menu(menuText);
            menuBar.getMenus().add(targetMenu);
        }

        MenuItem menuItem = new MenuItem(menuItemText);
        targetMenu.getItems().add(menuItem);
        
        menuItem.setOnAction(e -> openTab(menuItemText));
        
        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return false;
            }
        }
        
        Tab tab = new Tab(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        return true;
    }

    private void openTab(String tabText) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }

        Tab tab = new Tab(tabText);
        tab.setContent(new VBox(new Label("Content will be provided by plugins")));
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
}
