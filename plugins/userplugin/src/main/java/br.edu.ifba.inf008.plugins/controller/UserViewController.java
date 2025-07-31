package br.edu.ifba.inf008.plugins.controller;

import br.edu.ifba.inf008.interfaces.IDatabaseService;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.plugins.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

import java.util.List;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class UserViewController {
    private UserService userService;
    private String editingUserId = null;
    private Node rootView;

    @FXML private TableView<User> userTable;
    @FXML private TextField textName;
    @FXML private TextField textEmail;
    @FXML private Label labelMessage;
    @FXML private Button buttonSave;
    @FXML private Button buttonCancel;
    @FXML private Button buttonDelete;
    @FXML private Button buttonEdit;
    @FXML private Button buttonRefresh;

    public UserViewController(IDatabaseService databaseService) {
        this.userService = new UserService(databaseService);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/plugins/resource/UserView.fxml"));

            loader.setController(this);
            this.rootView = loader.load();
        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML para UserViewController: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @FXML
    public void initialize() {

        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, LocalDate> regDateColumn = new TableColumn<>("Registration Date");
        regDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        if (userTable != null && userTable.getColumns().isEmpty()) {
            userTable.getColumns().addAll(idColumn, nameColumn, emailColumn, regDateColumn);
        }


        loadUserData();
        resetForm();
    }

    @FXML
    public void handleSave() {
        String name = textName.getText();
        String email = textEmail.getText();

        if (name.isEmpty() || email.isEmpty()) {
            labelMessage.setText("Name and email are required!");
            labelMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        if (editingUserId == null) {

            User newUser = userService.createUser(name, email);
            if (newUser != null) {
                labelMessage.setText("User saved successfully!");
                labelMessage.setStyle("-fx-text-fill: green;");
            } else {
                labelMessage.setText("Error saving user!");
                labelMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            User updatedUser = new User(editingUserId, name, email, LocalDate.now()); // Keep original reg date, or fetch it
            User result = userService.updateUser(updatedUser);
            if (result != null) {
                labelMessage.setText("User updated successfully!");
                labelMessage.setStyle("-fx-text-fill: green;");
            } else {
                labelMessage.setText("Error updating user!");
                labelMessage.setStyle("-fx-text-fill: red;");
            }
        }
        resetForm();
        loadUserData();
    }

    @FXML
    public void handleEdit() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            textName.setText(selectedUser.getName());
            textEmail.setText(selectedUser.getEmail());
            editingUserId = selectedUser.getId();
            buttonSave.setText("Update");
            buttonCancel.setVisible(true);
            labelMessage.setText("Editing user: " + selectedUser.getName());
            labelMessage.setStyle("-fx-text-fill: blue;");
        } else {
            labelMessage.setText("Select a user to edit.");
            labelMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void handleDelete() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Add confirmation dialog if desired
            if (userService.deleteUser(selectedUser.getId())) {
                labelMessage.setText("User deleted successfully!");
                labelMessage.setStyle("-fx-text-fill: green;");
                loadUserData();
                resetForm();
            } else {
                labelMessage.setText("Error deleting user!");
                labelMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            labelMessage.setText("Select a user to delete.");
            labelMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void handleCancel() {
        resetForm();
        labelMessage.setText("Operation cancelled.");
        labelMessage.setStyle("-fx-text-fill: black;");
    }

    @FXML
    public void handleRefresh() {
        loadUserData();
        labelMessage.setText("Data refreshed.");
        labelMessage.setStyle("-fx-text-fill: black;");
    }

    private void loadUserData() {
        List<User> users = userService.getAllUsers();
        ObservableList<User> data = FXCollections.observableArrayList(users);
        if (userTable != null) {
            userTable.setItems(data);
        }
    }

    private void resetForm() {
        if (textName != null) textName.clear();
        if (textEmail != null) textEmail.clear();
        editingUserId = null;
        if (buttonSave != null) buttonSave.setText("Save");
        if (buttonCancel != null) buttonCancel.setVisible(false);
        if (labelMessage != null) labelMessage.setText("");
    }

    public Node getView() {
        return rootView;
    }
}
