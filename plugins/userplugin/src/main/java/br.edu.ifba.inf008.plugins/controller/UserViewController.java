package br.edu.ifba.inf008.plugins.controller;

import java.util.List;
import java.util.Optional;

import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.plugins.business.UserService;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private Integer editingUserId = null;

    private TextField txtName;
    private TextField txtEmail;
    private Button btnSave;
    private Button btnCancel;
    private Label lblMessage;
    private TextField txtSearch;
    private TableView<User> tableUsers;
    private ComboBox<String> cmbSearchType;



    public UserController(UserService userService) {
        this.userService = userService;
    }


    public void handleSave() {
        try {
            String name = txtName.getText();
            String email = txtEmail.getText();

            if (!UserValidationService.isNotEmpty(name)) {
                displayErrorMessage("O campo \"Nome\" é obrigatório");
                return;
            }
            if (!UserValidationService.isValidEmail(email)) {
                displayErrorMessage("O campo \"Email\" é inválido");
                return;
            }

            boolean isEditing = editingUserId != null;

            if (isEditing) {
                handleUpdateUser(name, email);
            } else {
                handleCreateUser(name, email);
            }
        } catch (Exception e) {
            displayErrorMessage("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleSearch() {
        String query = txtSearch.getText();
        String searchType = cmbSearchType.getValue();
        List<User> users;

        if (query.isEmpty()) {
            users = userService.getAllUsers();
        } else if ("Email".equals(searchType)) {
            users = userService.findUsersByEmail(query);
        } else {
            users = userService.findUsersByName(query);
        }

        tableUsers.getItems().clear();
        tableUsers.getItems().addAll(users);
    }

    public void handleClear() {
        txtSearch.clear();
        loadInitialData();
    }

    public void handleRefresh() {
        loadInitialData();
    }

    public void handleCancel() {
        resetForm();
        displayConfirmationMessage("Edição cancelada");
    }

    public void handleEdit() {
        User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            txtName.setText(selectedUser.getName());
            txtEmail.setText(selectedUser.getEmail());

            btnSave.setText("Atualizar");
            btnCancel.setVisible(true);
            displayConfirmationMessage("Editando " + selectedUser.getName() + " (" + selectedUser.getEmail() + ")");
            editingUserId = selectedUser.getUserId();
        } else {
            displayErrorMessage("Selecione um usuário para editar");
        }
    }

    public void handleDelete() {
        User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String warningMessage = userService.getActiveLoansWarning(selectedUser.getUserId());

            if (warningMessage != null) {
                boolean confirmed = UserMessageUtils.showConfirmation(
                        warningMessage + "\n\nDeseja continuar com a exclusão do usuário?"
                );

                if (!confirmed) {
                    displayConfirmationMessage("Exclusão cancelada pelo usuário");
                    return;
                }
            }
            else{
                String confirmationMessage = "Deseja continuar com a exclusão do usuário \""
                        + selectedUser.getName() + "\" proprietário do email \"" + selectedUser.getEmail() + "\"?";

                boolean confirmed = UserMessageUtils.showConfirmation(confirmationMessage);

                if (!confirmed) {
                    displayConfirmationMessage("Exclusão cancelada pelo usuário");
                    return;
                }
            }

            boolean deleted = userService.deleteUser(selectedUser.getUserId());
            if (deleted) {
                tableUsers.getItems().remove(selectedUser);
                resetForm();

                if (warningMessage != null) {
                    displaySuccessMessage("Usuário excluído com sucesso! Os empréstimos ativos foram finalizados e as cópias dos livros foram atualizadas.");
                } else {
                    displaySuccessMessage("Usuário excluído com sucesso!");
                }
            } else {
                displayErrorMessage("Erro ao remover usuário");
            }
        } else {
            displayErrorMessage("Selecione um usuário para remover");
        }
    }

    private void handleCreateUser(String name, String email) {
        if (userService.isEmailExists(email)) {
            displayErrorMessage("Email já cadastrado. Por favor, utilize outro endereço de email.");
            return;
        }

        User savedUser = userService.createUser(name, email);

        if (savedUser != null) {
            resetForm();
            loadInitialData();
            displaySuccessMessage("Usuário cadastrado com sucesso!");
        } else {
            displayErrorMessage("Erro ao cadastrar usuário");
        }
    }

    private void handleUpdateUser(String name, String email) {
        if (userService.isEmailExistsExcludingUser(email, editingUserId)) {
            displayErrorMessage("Email já cadastrado para outro usuário. Por favor, utilize outro endereço de email.");
            return;
        }

        Optional<User> existingUser = userService.findUserById(editingUserId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setEmail(email);

            User updatedUser = userService.updateUser(user);

            if (updatedUser != null) {
                resetForm();
                loadInitialData();
                displaySuccessMessage("Usuário atualizado com sucesso!");
            } else {
                displayErrorMessage("Erro ao atualizar usuário");
            }
        } else {
            resetForm();
            displayErrorMessage("Usuário não encontrado. ID: " + editingUserId);

        }
    }

    private void displayErrorMessage(String message) {
        UserMessageUtils.displayErrorMessage(lblMessage, message);
    }

    private void displaySuccessMessage(String message) {
        UserMessageUtils.displaySuccessMessage(lblMessage, message);
    }

    private void displayConfirmationMessage(String message) {
        UserMessageUtils.displayConfirmationMessage(lblMessage, message);
    }

    private void resetForm() {
        txtName.clear();
        txtEmail.clear();
        editingUserId = null;
        btnSave.setText("Cadastrar");
        btnCancel.setVisible(false);
        UserMessageUtils.clearMessage(lblMessage);
    }

    public void loadInitialData() {
        List<User> users = userService.getAllUsers();
        tableUsers.getItems().clear();
        tableUsers.getItems().addAll(users);
    }
}