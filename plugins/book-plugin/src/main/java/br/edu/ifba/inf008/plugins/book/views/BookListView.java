package br.edu.ifba.inf008.plugins.book.views;

import br.edu.ifba.inf008.plugins.book.dao.BookDAO;
import br.edu.ifba.inf008.interfaces.models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BookListView extends BorderPane {
    private TableView<Book> bookTable;
    private final ObservableList<Book> bookList;
    private final BookDAO bookDAO;
    private TextField searchField;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;
    
    public BookListView() {
        this.bookDAO = new BookDAO();
        this.bookList = FXCollections.observableArrayList();
        
        initializeComponents();
        setupLayout();
        setupEvents();
        loadBooks();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search books");
        searchField.setPrefWidth(300);
        
        // Buttons with consistent styling
        addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #170126; " +
                            "-fx-text-fill: white; " +
                            "-fx-padding: 8 16 8 16;" +
                            "-fx-border-radius: 4px; " +
                            "-fx-background-radius: 4px;");
        
        editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #3C0F59; " +
                            "-fx-text-fill: white; " +
                            "-fx-padding: 8 16 8 16; " +
                            "-fx-border-radius: 4px; " +
                            "-fx-background-radius: 4px;");
        editButton.setDisable(true);
        
        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #8243D9; " +
                              "-fx-text-fill: white; " +
                              "-fx-padding: 8 16 8 16; " +
                              "-fx-border-radius: 4px; " +
                              "-fx-background-radius: 4px;");
        deleteButton.setDisable(true);
        
        refreshButton = new Button("Update");
        refreshButton.setStyle("-fx-background-color: #C6A7F2; " +
                               "-fx-text-fill: white; " +
                               "-fx-padding: 8 16 8 16; " +
                               "-fx-border-radius: 4px; " +
                               "-fx-background-radius: 4px;");
        
        bookTable = new TableView<>();
        bookTable.setItems(bookList);
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
    }
    
    private void setupTableColumns() {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        idColumn.setResizable(false);
        
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);
        
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(150);
        
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(120);
        
        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("PublishedYear"));
        yearColumn.setPrefWidth(80);
        
        TableColumn<Book, String> copiesColumn = new TableColumn<>("Copies");
        copiesColumn.setCellValueFactory(cellData -> {
            int copies = cellData.getValue().getCopiesAvailable();
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(copies));
        });
        copiesColumn.setPrefWidth(80);
        
        TableColumn<Book, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            int copies = cellData.getValue().getCopiesAvailable();
            return new javafx.beans.property.SimpleStringProperty(copies > 0 ? "Available" : "Unavailable");
        });
        statusColumn.setPrefWidth(100);
        
        statusColumn.setCellFactory(column -> new javafx.scene.control.TableCell<Book, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Available".equals(item)) {
                        setStyle("-fx-background-color: #C6A7F2;" +
                                " -fx-text-fill: white; " +
                                "-fx-padding: 4 8 4 8;" +
                                " -fx-border-radius: 3px;" +
                                " -fx-background-radius: 3px;");
                    } else {
                        setStyle("-fx-background-color: #170126;" +
                                " -fx-text-fill: white; " +
                                "-fx-padding: 4 8 4 8; " +
                                "-fx-border-radius: 3px; " +
                                "-fx-background-radius: 3px;"
                        );
                    }
                }
            }
        });
        
        bookTable.getColumns().addAll(idColumn,
                                        titleColumn,
                                        authorColumn,
                                        isbnColumn,
                                        yearColumn,
                                        copiesColumn,
                                        statusColumn
        );
    }
    
    private void setupLayout() {

        Label titleLabel = new Label("Book Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        searchBox.setStyle("-fx-border-color: #cccccc; " +
                           "-fx-border-radius: 5;" +
                           " -fx-background-color: #f9f9f9;");
        searchBox.getChildren().addAll(
            new Label("Search:"),
            searchField,
            refreshButton
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        HBox statsPanel = new HBox(30);
        statsPanel.setPadding(new Insets(10));
        statsPanel.setStyle("-fx-background-color: #f5f5f5; " +
                            "-fx-border-radius: 5;");
        
        Label totalBooksLabel = new Label();
        Label availableBooksLabel = new Label(); 
        Label unavailableBooksLabel = new Label();
        updateBookStats(totalBooksLabel, availableBooksLabel, unavailableBooksLabel);
        
        statsPanel.getChildren().addAll(totalBooksLabel, availableBooksLabel, unavailableBooksLabel);
        
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(10));
        mainContent.getChildren().addAll(titleLabel, searchBox, buttonBox, statsPanel, bookTable);
        VBox.setVgrow(bookTable, Priority.ALWAYS);
        
        setCenter(mainContent);
    }
    
    private void updateBookStats(Label totalLabel, Label availableLabel, Label unavailableLabel) {
        int total = bookList.size();
        long available = bookList.stream().filter(book -> book.getCopiesAvailable() > 0).count();
        long unavailable = bookList.stream().filter(book -> book.getCopiesAvailable() == 0).count();
        
        totalLabel.setText("Total Books: " + total);
        availableLabel.setText("Available Books: " + available);
        unavailableLabel.setText("Unavailable Books: " + unavailable);
    }

    private void setupEvents() {
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean selected = newSelection != null;
            editButton.setDisable(!selected);
            deleteButton.setDisable(!selected);
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            filterBooks(newText);
        });

        refreshButton.setOnAction(e -> loadBooks());

        addButton.setOnAction(e -> addBook());

        editButton.setOnAction(e -> editBook());

        deleteButton.setOnAction(e -> deleteBook());

        bookTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editBook();
                }
            });
            return row;
        });
    }
    
    private void loadBooks() {
        try {
            bookList.clear();
            bookList.addAll(bookDAO.findAll());
            if (getCenter() instanceof VBox) {
                VBox mainContent = (VBox) getCenter();
                if (mainContent.getChildren().size() > 3 && mainContent.getChildren().get(3) instanceof HBox) {
                    HBox statsPanel = (HBox) mainContent.getChildren().get(3);
                    if (statsPanel.getChildren().size() >= 3) {
                        updateBookStats(
                            (Label) statsPanel.getChildren().get(0),
                            (Label) statsPanel.getChildren().get(1), 
                            (Label) statsPanel.getChildren().get(2)
                        );
                    }
                }
            }
        } catch (Exception e) {
            showError("Error loading books", e.getMessage());
        }
    }
    
    private void filterBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadBooks();
        } else {
            try {
                bookList.clear();
                bookList.addAll(bookDAO.findByTitleContaining(searchText.trim()));
                if (getCenter() instanceof VBox) {
                    VBox mainContent = (VBox) getCenter();
                    if (mainContent.getChildren().size() > 3 && mainContent.getChildren().get(3) instanceof HBox) {
                        HBox statsPanel = (HBox) mainContent.getChildren().get(3);
                        if (statsPanel.getChildren().size() >= 3) {
                            updateBookStats(
                                (Label) statsPanel.getChildren().get(0),
                                (Label) statsPanel.getChildren().get(1), 
                                (Label) statsPanel.getChildren().get(2)
                            );
                        }
                    }
                }
            } catch (Exception e) {
                showError("Error when filtering books", e.getMessage());
            }
        }
    }
    
    private void addBook() {
        BookFormDialog dialog = new BookFormDialog(null);
        dialog.showAndWait().ifPresent(book -> {
            if (bookDAO.insert(book)) {
                loadBooks();
                showSuccess("Book added successfully!");
            } else {
                showError("Error.", "Unable to add book.");
            }
        });
    }
    
    private void editBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            BookFormDialog dialog = new BookFormDialog(selectedBook);
            dialog.showAndWait().ifPresent(book -> {
                if (bookDAO.update(book)) {
                    loadBooks();
                    showSuccess("Book updated successfully!");
                } else {
                    showError("Error", "Unable to update book.");
                }
            });
        }
    }
    
    private void deleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete book\n");
            confirmDialog.setContentText("Are you sure you wanna delete the book? \"" +
                                          selectedBook.getTitle() +
                                          "\"?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (bookDAO.delete(selectedBook.getId())) {
                        loadBooks();
                        showSuccess("Book deleted successfully!");
                    } else {
                        showError("Error", "Unable to delete book.");
                    }
                }
            });
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
