package br.edu.ifba.inf008.plugins.report.views;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.plugins.report.dao.LoanReportDAO;
import br.edu.ifba.inf008.plugins.report.models.Loan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoanReportView extends VBox {
    private LoanReportDAO dao;
    private TableView<Loan> tableView;
    private ObservableList<Loan> loans;
    
    private TextField userFilterField;
    private TextField bookFilterField;
    private ComboBox<String> statusFilter;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button searchButton;
    private Button clearButton;
    private Button exportButton;
    
    // Stats labels
    private Label totalLoansLabel;
    private Label activeLoansLabel;
    private Label returnedLoansLabel;

    public LoanReportView() {
        this.dao = new LoanReportDAO();
        this.loans = FXCollections.observableArrayList();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }

    private void initializeComponents() {
        Label titleLabel = new Label("Loan Report");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Filter controls
        userFilterField = new TextField();
        userFilterField.setPromptText("Filter by user");
        
        bookFilterField = new TextField();
        bookFilterField.setPromptText("Filter by book");
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "Actives", "Returned");
        statusFilter.setValue("All");
        
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start date");
        
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Data final");
        
        searchButton = new Button("Filter");
        searchButton.setStyle("-fx-background-color: #8243D9; -fx-text-fill: white;");
        
        clearButton = new Button("Clean");
        clearButton.setStyle("-fx-background-color: #3C0F59; -fx-text-fill: white;");
        
        exportButton = new Button("Export");
        exportButton.setStyle("-fx-background-color: #7845BF; -fx-text-fill: white;");
        
        totalLoansLabel = new Label("Total Loans: 0");
        activeLoansLabel = new Label("Active Loans: 0");
        returnedLoansLabel = new Label("Returned Loans: 0");

        setupTable();
    }

    private void setupTable() {
        tableView = new TableView<>();
        tableView.setItems(loans);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Loan, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        
        // User Column
        TableColumn<Loan, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userColumn.setPrefWidth(150);
        
        TableColumn<Loan, String> bookColumn = new TableColumn<>("Book");
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookColumn.setPrefWidth(200);
        
        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan Date");
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        loanDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            boolean returned = cellData.getValue().isReturned();
            return new javafx.beans.property.SimpleStringProperty(returned ? "Returned" : "Active");
        });
        statusColumn.setPrefWidth(80);
        
        statusColumn.setCellFactory(column -> new TableCell<Loan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Active".equals(item)) {
                        setStyle("-fx-background-color: #8243D9; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #3C0F59; -fx-text-fill: white;");
                    }
                }
            }
        });
        
        tableView.getColumns().addAll(idColumn, userColumn, bookColumn, 
                                     loanDateColumn, returnDateColumn, statusColumn);
    }

    private void setupLayout() {
        Label titleLabel = new Label("Relatório de Empréstimos");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane filterPanel = new GridPane();
        filterPanel.setHgap(10);
        filterPanel.setVgap(10);
        filterPanel.setPadding(new Insets(10));
        filterPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        
        filterPanel.add(new Label("User:"), 0, 0);
        filterPanel.add(userFilterField, 1, 0);
        filterPanel.add(new Label("Book:"), 2, 0);
        filterPanel.add(bookFilterField, 3, 0);
        
        filterPanel.add(new Label("Status:"), 0, 1);
        filterPanel.add(statusFilter, 1, 1);
        filterPanel.add(new Label("Start Date:"), 2, 1);
        filterPanel.add(startDatePicker, 3, 1);
        
        filterPanel.add(new Label("Final Date:"), 0, 2);
        filterPanel.add(endDatePicker, 1, 2);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(searchButton, clearButton, exportButton);
        filterPanel.add(buttonBox, 2, 2, 2, 1);
        
        HBox statsPanel = new HBox(30);
        statsPanel.setPadding(new Insets(10));
        statsPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 5;");
        statsPanel.getChildren().addAll(totalLoansLabel, activeLoansLabel, returnedLoansLabel);
        
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(titleLabel, filterPanel, statsPanel, tableView);
        
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    private void setupEventHandlers() {
        searchButton.setOnAction(e -> applyFilters());
        clearButton.setOnAction(e -> clearFilters());
        exportButton.setOnAction(e -> exportData());
        
        userFilterField.textProperty().addListener((obs,
                                                    oldVal,
                                                    newVal) -> {
            if (newVal.isEmpty()) {
                applyFilters();
            }
        });
        
        bookFilterField.textProperty().addListener((obs,
                                                    oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                applyFilters();
            }
        });
    }

    private void loadData() {
        new Thread(() -> {
            try {
                List<Loan> allLoans = dao.getAllLoans();
                
                Platform.runLater(() -> {
                    loans.clear();
                    loans.addAll(allLoans);
                    updateStats();
                });
                
            } catch (SQLException e) {
                Platform.runLater(() -> {
                    showError("Error loading data", "Error querying the database: " + e.getMessage());
                });
            }
        }).start();
    }

    private void applyFilters() {
        new Thread(() -> {
            try {
                String userFilter = userFilterField.getText().trim();
                String bookFilter = bookFilterField.getText().trim();
                String statusFilterValue = statusFilter.getValue();
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();

                List<Loan> filteredLoansTemp;

                if (startDate != null && endDate != null) {
                    filteredLoansTemp = dao.getLoansByDateRange(startDate.toString(), endDate.toString());
                } else {
                    filteredLoansTemp = dao.getAllLoans();
                }

                if (!userFilter.isEmpty()) {
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.getUserName().toLowerCase().contains(userFilter.toLowerCase()))
                        .toList();
                }

                if (!bookFilter.isEmpty()) {
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.getBookTitle().toLowerCase().contains(bookFilter.toLowerCase()))
                        .toList();
                }

                if (!"All".equals(statusFilterValue)) {
                    boolean showReturned = "Returned".equals(statusFilterValue);
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.isReturned() == showReturned)
                        .toList();
                }

                final List<Loan> filteredLoans = filteredLoansTemp;

                Platform.runLater(() -> {
                    loans.clear();
                    loans.addAll(filteredLoans);
                    updateStats();
                });

            } catch (SQLException e) {
                Platform.runLater(() -> {
                    showError("Error when applying filters",
                            "Error when consulting the bank: " + e.getMessage());
                });
            }
        }).start();
    }

    private void clearFilters() {
        userFilterField.clear();
        bookFilterField.clear();
        statusFilter.setValue("All");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        loadData();
    }

    private void exportData() {
        StringBuilder sb = new StringBuilder();
        sb.append("LOAN REPORT\n");
        
        for (Loan loan : loans) {
            sb.append(String.format("ID: %d | User: %s | Book: %s | Date: %s | Status: %s\n",
                    loan.getId(),
                    loan.getUserName(),
                    loan.getBookTitle(),
                    loan.getLoanDate(),
                    loan.isReturned() ? "Returned" : "Active"));
        }
        
        sb.append(String.format("\nStatistics:\n"));
        sb.append(String.format("All: %d Loans\n", loans.size()));
        sb.append(String.format("Actives: %d\n", (int) loans.stream().filter(l -> !l.isReturned()).count()));
        sb.append(String.format("Returned\n: %d\n", (int) loans.stream().filter(Loan::isReturned).count()));
        
        System.out.println(sb.toString());
        
        showInfo("Export carried out", "Report exported to console.");
    }

    private void updateStats() {
        int total = loans.size();
        long active = loans.stream().filter(loan -> !loan.isReturned()).count();
        long returned = loans.stream().filter(Loan::isReturned).count();
        
        totalLoansLabel.setText("All Loans: " + total);
        activeLoansLabel.setText("Active Loans: " + active);
        returnedLoansLabel.setText("Returned Loans: " + returned);
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
