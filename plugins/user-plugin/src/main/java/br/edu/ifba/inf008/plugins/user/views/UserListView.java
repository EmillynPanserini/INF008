package br.edu.ifba.inf008.plugins.user.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import br.edu.ifba.inf008.plugins.user.components.UserTableComponent;
import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.interfaces.models.User;

import java.util.List;

public class UserListView extends BorderPane implements UserTableComponent.UserTableCallback {
    private UserTableComponent userTable;
    private UserDAO userDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    
    public UserListView() {
        this.userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        loadStylesheet();
        loadUsers();
    }
    
    private void initializeComponents() {
        userTable = new UserTableComponent(this);
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);
        
        statusLabel = new Label("Loading users");
        statusLabel.setVisible(false);
    }
    
    private void setupLayout() {
        setPadding(new Insets(10));
        

        Label titleLabel = new Label("User Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        VBox statusBox = new VBox(5);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center; " +
                           "-fx-background-color: #ad7af4; " +
                           "-fx-border-radius: 5; " +
                           "-fx-padding: 10;");

        VBox mainContent = new VBox(10);
        mainContent.getChildren().addAll(titleLabel, userTable, statusBox);
        VBox.setVgrow(userTable, Priority.ALWAYS);
        
        setCenter(mainContent);
    }
    
    private void loadUsers() {
        showLoading(true, "Loading users");
        
        Task<List<User>> task = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return userDAO.findAll();
            }
            
            @Override
            protected void succeeded() {
                userTable.setUsers(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + "User(s) found");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Error loading users", getException());
                updateStatusLabel("Error loading data");
            }
        };
        new Thread(task).start();
    }
    
    private void searchUsers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadUsers();
            return;
        }
        
        showLoading(true, "Searching users");
        
        Task<List<User>> task = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return userDAO.findByNameContaining(searchText.trim());
            }
            
            @Override
            protected void succeeded() {
                userTable.setUsers(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + " User(s) found for'" + searchText + "'");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Error when searching for users", getException());
                updateStatusLabel("Search error");
            }
        };
        new Thread(task).start();
    }
    
    @Override
    public void onAdd() {
        UserFormDialog dialog = new UserFormDialog(null);
        dialog.showAndWait().ifPresent(user -> {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return userDAO.insert(user);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        userTable.addUser(user);
                        showSuccess("User added successfully");
                        updateStatusLabel("User added");
                    } else {
                        showError("Error adding user", null);
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Error adding user", getException());
                }
            };
            new Thread(task).start();
        });
    }
    
    @Override
    public void onEdit(User user) {
        UserFormDialog dialog = new UserFormDialog(user);
        dialog.showAndWait().ifPresent(updatedUser -> {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return userDAO.update(updatedUser);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        userTable.updateUser(updatedUser);
                        showSuccess("User updated successfully");
                        updateStatusLabel("User updated");
                    } else {
                        showError("Error updating user", null);
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Error updating user", getException());
                }
            };
            new Thread(task).start();
        });
    }
    
    @Override
    public void onDelete(User user) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return userDAO.delete(user.getId());
            }
            
            @Override
            protected void succeeded() {
                if (getValue()) {
                    userTable.removeUser(user);
                    showSuccess("User deleted successfully");
                    updateStatusLabel("User deleted");
                } else {
                    showError("Error deleting user", null);
                }
            }
            
            @Override
            protected void failed() {
                showError("Error deleting user", getException());
            }
        };
        
        new Thread(task).start();
    }
    
    @Override
    public void onRefresh() {
        loadUsers();
    }
    
    @Override
    public void onSearch(String searchText) {
        searchUsers(searchText);
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        } else if (!show) {
            statusLabel.setVisible(false);
        }
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(3),
            e -> statusLabel.setVisible(false)
        ));
        timeline.play();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message, Throwable exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        
        if (exception != null) {
            alert.setContentText(exception.getMessage());
        }
        
        alert.showAndWait();
    }
    
    private void loadStylesheet() {
        try {
            String css = getClass().getResource("/styles/user-plugin.css").toExternalForm();
            getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("UserListView: Could not load stylesheet - " + e.getMessage());
        }
    }
}
