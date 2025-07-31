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
import javafx.fxml.FXMLLoader; // Importação adicionada para FXMLLoader
import java.io.IOException;   // Importação adicionada para IOException

public class UserViewController {
    private UserService userService;
    private String editingUserId = null; // Para armazenar o ID do usuário em edição
    private Node rootView; // Este campo será inicializado ao carregar o FXML

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
            // Cria um FXMLLoader e especifica o caminho para o seu arquivo FXML.
            // O caminho é relativo à pasta 'resources' do seu módulo e segue a estrutura de pacotes.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/inf008/plugins/controller/UserView.fxml"));

            // Define esta instância (UserViewController) como o controlador do FXML.
            // Isso permite que os elementos fx:id e onAction no FXML sejam conectados a este objeto.
            loader.setController(this);

            // Carrega o FXML e atribui o Node raiz (geralmente um VBox, AnchorPane, etc.)
            // que está definido no FXML para a variável rootView.
            this.rootView = loader.load();
        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML para UserViewController: " + e.getMessage());
            e.printStackTrace();
            // Aqui você pode adicionar lógica para lidar com o erro de carregamento do FXML,
            // como exibir uma mensagem de erro na console ou na própria UI, ou carregar
            // uma interface de fallback.
        }
    }

    @FXML
    public void initialize() {
        // Configurar colunas da tabela
        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, LocalDate> regDateColumn = new TableColumn<>("Registration Date");
        regDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Adiciona as colunas à TableView apenas se ela ainda não as tiver (evita duplicação)
        if (userTable != null && userTable.getColumns().isEmpty()) { // Adicionada verificação de nulidade para userTable
            userTable.getColumns().addAll(idColumn, nameColumn, emailColumn, regDateColumn);
        }

        // loadUserData() e resetForm() agora serão chamados após o FXML ser carregado
        // e os elementos @FXML injetados.
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
            // Create new user
            User newUser = userService.createUser(name, email);
            if (newUser != null) {
                labelMessage.setText("User saved successfully!");
                labelMessage.setStyle("-fx-text-fill: green;");
            } else {
                labelMessage.setText("Error saving user!");
                labelMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            // Update existing user
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
            editingUserId = selectedUser.getId(); // Store ID for update
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
        if (userTable != null) { // Adicionada verificação de nulidade
            userTable.setItems(data);
        }
    }

    private void resetForm() {
        if (textName != null) textName.clear(); // Adicionada verificação de nulidade
        if (textEmail != null) textEmail.clear(); // Adicionada verificação de nulidade
        editingUserId = null;
        if (buttonSave != null) buttonSave.setText("Save"); // Adicionada verificação de nulidade
        if (buttonCancel != null) buttonCancel.setVisible(false); // Adicionada verificação de nulidade
        if (labelMessage != null) labelMessage.setText(""); // Clear message (Adicionada verificação de nulidade)
    }

    public Node getView() {
        return rootView;
    }
}
