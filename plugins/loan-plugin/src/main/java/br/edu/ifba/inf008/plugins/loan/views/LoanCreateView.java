package br.edu.ifba.inf008.plugins.loan.views;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import br.edu.ifba.inf008.plugins.loan.dao.LoanDAO;
import br.edu.ifba.inf008.interfaces.models.Loan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanCreateView extends BorderPane {
    private ComboBox<UserItem> userComboBox;
    private ComboBox<BookItem> bookComboBox;
    private DatePicker loanDatePicker;
    private Button createButton;
    private Button cancelButton;
    private final LoanDAO loanDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private Runnable onLoanCreated;
    
    public static class UserItem {
        private final int id;
        private final String name;
        
        public UserItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        
        @Override
        public String toString() { return name; }
    }
    
    public static class BookItem {
        private final int id;
        private final String title;
        private final String author;
        private final boolean available;
        
        public BookItem(int id, String title, String author, boolean available) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.available = available;
        }
        
        public int getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }
        public String getAuthor() {
            return author;
        }
        public boolean isAvailable() {
            return available;
        }
        
        @Override
        public String toString() { 
            String status = available ? "" : " (Unavailable)";
            return title + " - " + author + status;
        }
    }
    
    public LoanCreateView() {
        this.loanDAO = new LoanDAO();
        initializeComponents();
        setupLayout();
        loadData();
        applyStyles();
    }
    
    public void setOnLoanCreated(Runnable callback) {
        this.onLoanCreated = callback;
    }
    
    private void initializeComponents() {
        userComboBox = new ComboBox<>();
        userComboBox.setPromptText("Select a user");
        userComboBox.setMaxWidth(Double.MAX_VALUE);
        userComboBox.setStyle("-fx-padding: 8px 12px; " +
                              "-fx-border-radius: 4px;" +
                              "-fx-background-radius: 4px;");
        
        bookComboBox = new ComboBox<>();
        bookComboBox.setPromptText("Select a book");
        bookComboBox.setMaxWidth(Double.MAX_VALUE);
        bookComboBox.setStyle("-fx-padding: 8px 12px;" +
                              "-fx-border-radius: 4px;" +
                              "-fx-background-radius: 4px;");
        
        loanDatePicker = new DatePicker(LocalDate.now());
        loanDatePicker.setMaxWidth(Double.MAX_VALUE);
        loanDatePicker.setStyle("-fx-padding: 8px 12px;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;");
        
        createButton = new Button("Create Loan");
        createButton.setStyle("-fx-background-color: #7845BF; " +
                              "-fx-text-fill: white;" +
                              "-fx-padding: 8 16 8 16; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        createButton.setOnAction(e -> createLoan());
        createButton.setDefaultButton(true);
        createButton.setDisable(true);
        
        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #170126;" +
                              "-fx-text-fill: white; " +
                              "-fx-padding: 8 16 8 16; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        cancelButton.setOnAction(e -> clearForm());
        
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(30, 30);
        
        statusLabel = new Label();
        statusLabel.setVisible(false);
        
        userComboBox.valueProperty().addListener((obs,
                                                  oldVal,
                                                  newVal) -> updateCreateButtonState());
        bookComboBox.valueProperty().addListener((obs,
                                                  oldVal,
                                                  newVal) -> updateCreateButtonState());
    }
    
    private void setupLayout() {
        setPadding(new Insets(20));
        
        Label titleLabel = new Label("Create New Loan");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20, 0, 20, 0));
        
        formGrid.add(new Label("User:"), 0, 0);
        formGrid.add(userComboBox, 1, 0);
        
        formGrid.add(new Label("Book:"), 0, 1);
        formGrid.add(bookComboBox, 1, 1);
        
        formGrid.add(new Label("Loan Date:"), 0, 2);
        formGrid.add(loanDatePicker, 1, 2);
        
        formGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(120));
        formGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
        formGrid.getColumnConstraints().get(1).setHgrow(javafx.scene.layout.Priority.ALWAYS);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(createButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center-right;");
        
        HBox statusBox = new HBox(10);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center;");
        statusBox.setPadding(new Insets(10, 0, 0, 0));
        
        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(titleLabel, formGrid, buttonBox, statusBox);
        
        setCenter(mainContent);
    }
    
    private void loadData() {
        showLoading(true, "Loading Data");
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loadUsers();
                loadBooks();
                return null;
            }
            
            @Override
            protected void succeeded() {
                showLoading(false, null);
                updateStatusLabel("Data loaded ");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Error loading data", getException());
                updateStatusLabel("Error loading data");
            }
        };
        
        new Thread(task).start();
    }
    
    private void loadUsers() throws SQLException {
        String sql = "SELECT user_id, name FROM users ORDER BY name";
        
        try (Connection conn = br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            javafx.application.Platform.runLater(() -> userComboBox.getItems().clear());
            
            while (rs.next()) {
                UserItem user = new UserItem(rs.getInt("user_id"), rs.getString("name"));
                javafx.application.Platform.runLater(() -> userComboBox.getItems().add(user));
            }
        }
    }
    
    private void loadBooks() throws SQLException {
        String sql = "SELECT b.book_id, b.title, b.author, " +
                     "CASE WHEN l.loan_id IS NULL THEN 1 ELSE 0 END as available " +
                     "FROM books b " +
                     "LEFT JOIN loans l ON b.book_id = l.book_id AND l.return_date IS NULL " +
                     "ORDER BY b.title";
        
        try (Connection conn = br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            javafx.application.Platform.runLater(() -> bookComboBox.getItems().clear());
            
            while (rs.next()) {
                BookItem book = new BookItem(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getBoolean("available")
                );
                javafx.application.Platform.runLater(() -> bookComboBox.getItems().add(book));
            }
        }
    }
    
    private void updateCreateButtonState() {
        boolean canCreate = userComboBox.getValue() != null && 
                           bookComboBox.getValue() != null &&
                           (bookComboBox.getValue() == null || bookComboBox.getValue().isAvailable());
        createButton.setDisable(!canCreate);
        
        updateComboBoxStyle(userComboBox, userComboBox.getValue() != null);
        updateComboBoxStyle(bookComboBox, bookComboBox.getValue() != null && 
                           (bookComboBox.getValue() == null || bookComboBox.getValue().isAvailable()));
    }
    
    private void updateComboBoxStyle(ComboBox<?> comboBox, boolean isValid) {
        if (comboBox.getValue() != null) {
            if (isValid) {
                comboBox.setStyle("-fx-padding: 8px 12px; " +
                                  "-fx-border-radius: 4px;" +
                                  "-fx-background-radius: 4px; " +
                                  "-fx-border-color: #C6A7F2;" +
                                  "-fx-border-width: 1px;");
            } else {
                comboBox.setStyle("-fx-padding: 8px 12px; " +
                                  "-fx-border-radius: 4px; " +
                                  "-fx-background-radius: 4px; " +
                                  "-fx-border-color: #f44336; " +
                                  "-fx-border-width: 2px;");
            }
        } else {
            comboBox.setStyle("-fx-padding: 8px 12px; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        }
    }
    
    private void createLoan() {
        UserItem selectedUser = userComboBox.getValue();
        BookItem selectedBook = bookComboBox.getValue();
        LocalDate selectedDate = loanDatePicker.getValue();
        
        clearValidationStyles();
        
        StringBuilder errors = new StringBuilder();
        boolean hasErrors = false;
        
        if (selectedUser == null) {
            errors.append("Select a user\n");
            userComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (selectedBook == null) {
            errors.append("Select a book\n");
            bookComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        } else if (!selectedBook.isAvailable()) {
            errors.append("The selected book is not available\n");
            bookComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (selectedDate == null) {
            errors.append("Select a date\n");
            loanDatePicker.getStyleClass().add("field-error");
            hasErrors = true;
        } else if (selectedDate.isAfter(LocalDate.now())) {
            errors.append("The loan date cannot be in the future\n");
            loanDatePicker.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (hasErrors) {
            showValidationError("Correct the following errors:", errors.toString());
            return;
        }
        
        showLoading(true, "Creating loan");
        
        Loan newLoan = new Loan();
        if (selectedUser != null) {
            newLoan.setUserId(selectedUser.getId());
        }
        if (selectedBook != null) {
            newLoan.setBookId(selectedBook.getId());
        }
        if (selectedDate != null) {
            newLoan.setLoanDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return loanDAO.save(newLoan);
            }
            
            @Override
            protected void succeeded() {
                showLoading(false, null);
                if (getValue()) {
                    showInfo("Successfully", "Loan created");
                    clearForm();
                    if (onLoanCreated != null) {
                        onLoanCreated.run();
                    }
                } else {
                    showError("Error", "Unable to create loan");
                }
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Error creating loan", getException());
            }
        };
        
        new Thread(task).start();
    }
    
    private void clearValidationStyles() {
        userComboBox.setStyle("-fx-padding: 8px 12px; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        bookComboBox.setStyle("-fx-padding: 8px 12px; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        loanDatePicker.setStyle("-fx-padding: 8px 12px; " +
                                "-fx-border-radius: 4px; " +
                                "-fx-background-radius: 4px;");
    }
    
    private void showValidationError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void clearForm() {
        userComboBox.setValue(null);
        bookComboBox.setValue(null);
        loanDatePicker.setValue(LocalDate.now());
        statusLabel.setVisible(false);
        clearValidationStyles();
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        }
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), e -> statusLabel.setVisible(false))
        );
        timeline.play();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, Throwable exception) {
        String message = exception.getMessage();
        if (message == null) {
            message = exception.getClass().getSimpleName();
        }
        showError(title, message);
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/loan-plugin-styles.css").toExternalForm();
            getStylesheets().add(cssPath);
            getStyleClass().add("loan-form");
        } catch (Exception e) {
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
}
