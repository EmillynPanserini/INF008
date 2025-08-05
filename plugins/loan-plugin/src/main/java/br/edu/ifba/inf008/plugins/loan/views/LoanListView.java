package br.edu.ifba.inf008.plugins.loan.views;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import br.edu.ifba.inf008.plugins.loan.dao.LoanDAO;
import br.edu.ifba.inf008.interfaces.models.Loan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LoanListView extends BorderPane {
    private TableView<Loan> loanTable;
    private final LoanDAO loanDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private TextField searchField;
    private ComboBox<String> searchTypeCombo;
    private CheckBox activeOnlyCheckBox;
    private Button newLoanButton;
    
    public LoanListView() {
        this.loanDAO = new LoanDAO();
        initializeComponents();
        setupLayout();
        loadLoans();
        applyStyles();
    }
    
    private void initializeComponents() {
        loanTable = new TableView<>();
        loanTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        
        searchField = new TextField();
        searchField.setPromptText("Type to search");
        searchField.textProperty().addListener((obs,
                                                oldText,
                                                newText) -> performSearch());
        
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("All", "User", "Book");
        searchTypeCombo.setValue("All");
        searchTypeCombo.setOnAction(e -> performSearch());
        
        activeOnlyCheckBox = new CheckBox("Active loans only");
        activeOnlyCheckBox.setOnAction(e -> performSearch());
        
        newLoanButton = new Button("New Loan");
        newLoanButton.setStyle("-fx-background-color: #7845BF; " +
                               "-fx-text-fill: white; " +
                               "-fx-padding: 8 16 8 16; " +
                               "-fx-border-radius: 4px; -fx-background-radius: 4px;");
        newLoanButton.setOnAction(e -> showCreateLoanDialog());
        
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);
        
        statusLabel = new Label("Loading loans");
        statusLabel.setVisible(false);
    }
    
    private void setupTableColumns() {
        TableColumn<Loan, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setPrefWidth(50);
        
        TableColumn<Loan, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));
        userColumn.setPrefWidth(150);
        
        TableColumn<Loan, String> bookColumn = new TableColumn<>("Book");
        bookColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookTitle()));
        bookColumn.setPrefWidth(200);
        
        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan Date");
        loanDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLoanDate()));
        loanDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReturnDate()));
        returnDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isReturned() ? "Returned" : "Active"));
        statusColumn.setPrefWidth(80);
        
        // Action column with consistent button styling
        TableColumn<Loan, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button returnButton = new Button("Return");
            {
                returnButton.setStyle("-fx-background-color: #C6A7F2; " +
                                      "-fx-text-fill: white; " +
                                      "-fx-padding: 4 8 4 8; " +
                                      "-fx-border-radius: 3px; " +
                                      "-fx-background-radius: 3px;");
                returnButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (!loan.isReturned()) {
                        returnLoan(loan);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan.isReturned()) {
                        Label returnedLabel = new Label("Returned");
                        returnedLabel.setStyle("-fx-background-color: #170126; " +
                                               "-fx-text-fill: white; " +
                                               "-fx-padding: 4 8 4 8; " +
                                               "-fx-border-radius: 3px; " +
                                               "-fx-background-radius: 3px;");
                        setGraphic(returnedLabel);
                    } else {
                        setGraphic(returnButton);
                    }
                }
            }
        });
        actionColumn.setPrefWidth(100);
        
        loanTable.getColumns().addAll(idColumn, userColumn, bookColumn, 
                                     loanDateColumn, returnDateColumn, statusColumn, actionColumn);
    }
    
    private void setupLayout() {
        setPadding(new Insets(10));
        
        Label titleLabel = new Label("Loan Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        searchBox.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("By:"), searchTypeCombo,
            activeOnlyCheckBox
        );
        
        HBox topBar = new HBox(10);
        topBar.getChildren().addAll(searchBox, newLoanButton);
        topBar.setStyle("-fx-alignment: center-left;");
        
        HBox.setHgrow(searchBox, javafx.scene.layout.Priority.ALWAYS);
        
        VBox statusBox = new VBox(5);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center; " +
                           "-fx-background-color: #C6A7F2;" +
                           "-fx-border-radius: 5; " +
                           "-fx-padding: 10;");
        
        VBox mainContent = new VBox(10);
        mainContent.getChildren().addAll(titleLabel, topBar, loanTable, statusBox);
        VBox.setVgrow(loanTable, Priority.ALWAYS);
        
        setCenter(mainContent);
    }
    
    private void loadLoans() {
        showLoading(true, "Loading loans");
        
        Task<List<Loan>> task = new Task<List<Loan>>() {
            @Override
            protected List<Loan> call() throws Exception {
                return loanDAO.findAll();
            }
            
            @Override
            protected void succeeded() {
                loanTable.getItems().clear();
                loanTable.getItems().addAll(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + "Loan(s) found");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Error loading loans", getException());
                updateStatusLabel("Error loading data");
            }
        };
        
        new Thread(task).start();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = searchTypeCombo.getValue();
        boolean activeOnly = activeOnlyCheckBox.isSelected();
        
        showLoading(true, "Searching");
        
        Task<List<Loan>> task = new Task<List<Loan>>() {
            @Override
            protected List<Loan> call() throws Exception {
                List<Loan> results;
                
                if (activeOnly) {
                    results = loanDAO.findActiveLoans();
                } else if (searchText.isEmpty()) {
                    results = loanDAO.findAll();
                } else {
                    switch (searchType) {
                        case "User":
                            results = loanDAO.searchByUserName(searchText);
                            break;
                        case "Book":
                            results = loanDAO.searchByBookTitle(searchText);
                            break;
                        default:
                            results = loanDAO.searchByUserName(searchText);
                            results.addAll(loanDAO.searchByBookTitle(searchText));
                            results = results.stream().distinct().collect(java.util.stream.Collectors.toList());
                            break;
                    }
                }
                
                if (activeOnly && !searchText.isEmpty()) {
                    results = results.stream()
                        .filter(loan -> !loan.isReturned())
                        .collect(java.util.stream.Collectors.toList());
                }
                
                return results;
            }
            
            @Override
            protected void succeeded() {
                loanTable.getItems().clear();
                loanTable.getItems().addAll(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + "Loan(s) found");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Search error", getException());
                updateStatusLabel("Search error");
            }
        };
        
        new Thread(task).start();
    }
    
    private void returnLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Return");
        alert.setHeaderText("Return loan");
        alert.setContentText("Do you wanna confirm the return of the book? '" +
                              loan.getBookTitle() +
                              "' by user '" +
                              loan.getUserName() + "'?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String returnDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return loanDAO.returnLoan(loan.getId(), returnDate);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        showInfo("Successfully", "Loan returned successfully");
                        performSearch();
                    } else {
                        showError("Error", "Unable to return loan.");
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Error returning loan", getException());
                }
            };
            
            new Thread(task).start();
        }
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        } else {
            statusLabel.setVisible(false);
        }
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.1),
                                         e -> statusLabel.setVisible(false))
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
    
    private void showCreateLoanDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Create New Loan");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        
        LoanCreateView createView = new LoanCreateView();
        createView.setOnLoanCreated(() -> {
            dialog.close();
            performSearch();
        });
        
        Scene scene = new Scene(createView, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/loan-plugin-styles.css").toExternalForm();
            getStylesheets().add(cssPath);
            loanTable.getStyleClass().add("loan-table");
        } catch (Exception e) {
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
}
