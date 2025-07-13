package com.library.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.library.service.LibraryService;
import com.library.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class LibraryManagementApp extends Application {
    private LibraryService libraryService;
    private TabPane tabPane;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public void start(Stage primaryStage) {
        libraryService = new LibraryService();
        
        primaryStage.setTitle("Library Management System");
        
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createStudentTab(),
            createBookTab(),
            createBookCopyTab(),
            createLoanTab(),
            createReportTab()
        );
        
        Scene scene = new Scene(tabPane, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createStudentTab() {
        Tab tab = new Tab("Student Management");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        // Student form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField broncoIdField = new TextField();
        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField degreeField = new TextField();
        
        form.add(new Label("Bronco ID:"), 0, 0);
        form.add(broncoIdField, 1, 0);
        form.add(new Label("Name:"), 0, 1);
        form.add(nameField, 1, 1);
        form.add(new Label("Address:"), 0, 2);
        form.add(addressField, 1, 2);
        form.add(new Label("Degree:"), 0, 3);
        form.add(degreeField, 1, 3);
        
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add Student");
        Button updateBtn = new Button("Update Student");
        Button deleteBtn = new Button("Delete Student");
        Button clearBtn = new Button("Clear");
        buttonBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);
        
        TableView<Student> studentTable = new TableView<>();
        studentTable.setPrefHeight(400);
        
        TableColumn<Student, String> broncoIdCol = new TableColumn<>("Bronco ID");
        broncoIdCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBroncoId()));
        broncoIdCol.setPrefWidth(120);
        
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setPrefWidth(200);
        
        TableColumn<Student, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAddress() != null ? 
                cellData.getValue().getAddress() : ""));
        addressCol.setPrefWidth(300);
        
        TableColumn<Student, String> degreeCol = new TableColumn<>("Degree");
        degreeCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDegree() != null ? 
                cellData.getValue().getDegree() : ""));
        degreeCol.setPrefWidth(200);
        
        TableColumn<Student, String> loanCountCol = new TableColumn<>("Active Loans");
        loanCountCol.setCellValueFactory(cellData -> {
            try {
                int count = libraryService.getCurrentBorrowedCount(cellData.getValue().getBroncoId());
                return new SimpleStringProperty(String.valueOf(count));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        loanCountCol.setPrefWidth(100);
        
        studentTable.getColumns().addAll(broncoIdCol, nameCol, addressCol, degreeCol, loanCountCol);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search students by name or Bronco ID...");
        Button searchBtn = new Button("Search");
        Button refreshBtn = new Button("Refresh");
        HBox searchBox = new HBox(10, searchField, searchBtn, refreshBtn);
        
        vbox.getChildren().addAll(
            new Label("Student Management"),
            form,
            buttonBox,
            new Separator(),
            searchBox,
            studentTable
        );
        
        // Event handlers
        addBtn.setOnAction(addEvent -> {
            try {
                if (broncoIdField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
                    showAlert("Error", "Bronco ID and Name are required!");
                    return;
                }
                
                Student student = new Student(
                    broncoIdField.getText().trim(),
                    nameField.getText().trim(),
                    addressField.getText().trim(),
                    degreeField.getText().trim()
                );
                libraryService.createStudent(student);
                refreshStudentTable(studentTable);
                clearStudentForm(broncoIdField, nameField, addressField, degreeField);
                showAlert("Success", "Student added successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add student: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(updateEvent -> {
            try {
                if (broncoIdField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a student to update!");
                    return;
                }
                
                Student student = new Student(
                    broncoIdField.getText().trim(),
                    nameField.getText().trim(),
                    addressField.getText().trim(),
                    degreeField.getText().trim()
                );
                libraryService.updateStudent(student);
                refreshStudentTable(studentTable);
                showAlert("Success", "Student updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update student: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(deleteEvent -> {
            try {
                if (broncoIdField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a student to delete!");
                    return;
                }
                
                libraryService.deleteStudent(broncoIdField.getText().trim());
                refreshStudentTable(studentTable);
                clearStudentForm(broncoIdField, nameField, addressField, degreeField);
                showAlert("Success", "Student deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete student: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(clearEvent -> clearStudentForm(broncoIdField, nameField, addressField, degreeField));
        
        searchBtn.setOnAction(searchEvent -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                refreshStudentTable(studentTable);
            } else {
                try {
                    studentTable.getItems().setAll(libraryService.searchStudents(searchTerm));
                } catch (Exception ex) {
                    showAlert("Error", "Search failed: " + ex.getMessage());
                }
            }
        });
        
        refreshBtn.setOnAction(refreshEvent -> refreshStudentTable(studentTable));
        
        studentTable.setOnMouseClicked(clickEvent -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                broncoIdField.setText(selected.getBroncoId());
                nameField.setText(selected.getName());
                addressField.setText(selected.getAddress() != null ? selected.getAddress() : "");
                degreeField.setText(selected.getDegree() != null ? selected.getDegree() : "");
            }
        });
        
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                refreshStudentTable(studentTable);
            }
        });
        
        tab.setContent(vbox);
        return tab;
    }

    private Tab createBookTab() {
        Tab tab = new Tab("Book Management");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField isbnField = new TextField();
        TextField titleField = new TextField();
        TextArea descriptionField = new TextArea();
        descriptionField.setPrefRowCount(3);
        TextField authorsField = new TextField();
        TextField pagesField = new TextField();
        TextField publisherField = new TextField();
        DatePicker publicationDatePicker = new DatePicker();
        
        form.add(new Label("ISBN:"), 0, 0);
        form.add(isbnField, 1, 0);
        form.add(new Label("Title:"), 0, 1);
        form.add(titleField, 1, 1);
        form.add(new Label("Description:"), 0, 2);
        form.add(descriptionField, 1, 2);
        form.add(new Label("Authors:"), 0, 3);
        form.add(authorsField, 1, 3);
        form.add(new Label("Pages:"), 0, 4);
        form.add(pagesField, 1, 4);
        form.add(new Label("Publisher:"), 0, 5);
        form.add(publisherField, 1, 5);
        form.add(new Label("Publication Date:"), 0, 6);
        form.add(publicationDatePicker, 1, 6);
        
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add Book");
        Button updateBtn = new Button("Update Book");
        Button deleteBtn = new Button("Delete Book");
        Button clearBtn = new Button("Clear");
        buttonBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);
        
        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefHeight(400);
        
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIsbn()));
        isbnCol.setPrefWidth(120);
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitle()));
        titleCol.setPrefWidth(250);
        
        TableColumn<Book, String> authorsCol = new TableColumn<>("Authors");
        authorsCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAuthors() != null ? 
                cellData.getValue().getAuthors() : ""));
        authorsCol.setPrefWidth(200);
        
        TableColumn<Book, String> publisherCol = new TableColumn<>("Publisher");
        publisherCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPublisher() != null ? 
                cellData.getValue().getPublisher() : ""));
        publisherCol.setPrefWidth(150);
        
        TableColumn<Book, String> pagesCol = new TableColumn<>("Pages");
        pagesCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNumberOfPages() != null ? 
                cellData.getValue().getNumberOfPages().toString() : ""));
        pagesCol.setPrefWidth(80);
        
        TableColumn<Book, String> pubDateCol = new TableColumn<>("Publication Date");
        pubDateCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPublicationDate() != null ? 
                cellData.getValue().getPublicationDate().format(dateFormatter) : ""));
        pubDateCol.setPrefWidth(120);
        
        TableColumn<Book, String> copiesCol = new TableColumn<>("Total Copies");
        copiesCol.setCellValueFactory(cellData -> {
            try {
                int count = libraryService.getBookCopiesByISBN(cellData.getValue().getIsbn()).size();
                return new SimpleStringProperty(String.valueOf(count));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        copiesCol.setPrefWidth(100);
        
        TableColumn<Book, String> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(cellData -> {
            try {
                int count = libraryService.getAvailableBookCopies(cellData.getValue().getIsbn()).size();
                return new SimpleStringProperty(String.valueOf(count));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        availableCol.setPrefWidth(80);
        
        bookTable.getColumns().addAll(isbnCol, titleCol, authorsCol, publisherCol, 
                                      pagesCol, pubDateCol, copiesCol, availableCol);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search books by title, author, or ISBN...");
        Button searchBtn = new Button("Search");
        Button refreshBtn = new Button("Refresh");
        Button showAvailabilityBtn = new Button("Show Availability");
        HBox searchBox = new HBox(10, searchField, searchBtn, refreshBtn, showAvailabilityBtn);
        
        vbox.getChildren().addAll(
            new Label("Book Management"),
            form,
            buttonBox,
            new Separator(),
            searchBox,
            bookTable
        );
        
        // Event handlers
        addBtn.setOnAction(addEvent -> {
            try {
                if (isbnField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty()) {
                    showAlert("Error", "ISBN and Title are required!");
                    return;
                }
                
                Book book = new Book(
                    isbnField.getText().trim(),
                    titleField.getText().trim(),
                    descriptionField.getText().trim(),
                    authorsField.getText().trim(),
                    pagesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(pagesField.getText().trim()),
                    publisherField.getText().trim(),
                    publicationDatePicker.getValue()
                );
                libraryService.createBook(book);
                refreshBookTable(bookTable);
                clearBookForm(isbnField, titleField, descriptionField, authorsField, 
                           pagesField, publisherField, publicationDatePicker);
                showAlert("Success", "Book added successfully!");
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number for pages!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add book: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(updateEvent -> {
            try {
                if (isbnField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book to update!");
                    return;
                }
                
                Book book = new Book(
                    isbnField.getText().trim(),
                    titleField.getText().trim(),
                    descriptionField.getText().trim(),
                    authorsField.getText().trim(),
                    pagesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(pagesField.getText().trim()),
                    publisherField.getText().trim(),
                    publicationDatePicker.getValue()
                );
                libraryService.updateBook(book);
                refreshBookTable(bookTable);
                showAlert("Success", "Book updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update book: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(deleteEvent -> {
            try {
                if (isbnField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book to delete!");
                    return;
                }
                
                libraryService.deleteBook(isbnField.getText().trim());
                refreshBookTable(bookTable);
                clearBookForm(isbnField, titleField, descriptionField, authorsField, 
                           pagesField, publisherField, publicationDatePicker);
                showAlert("Success", "Book deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete book: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(clearEvent -> clearBookForm(isbnField, titleField, descriptionField, 
                                              authorsField, pagesField, publisherField, publicationDatePicker));
        
        searchBtn.setOnAction(searchEvent -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                refreshBookTable(bookTable);
            } else {
                try {
                    bookTable.getItems().setAll(libraryService.searchBooks(searchTerm));
                } catch (Exception ex) {
                    showAlert("Error", "Search failed: " + ex.getMessage());
                }
            }
        });
        
        refreshBtn.setOnAction(refreshEvent -> refreshBookTable(bookTable));
        
        showAvailabilityBtn.setOnAction(availabilityEvent -> {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showBookAvailability(selected);
            } else {
                showAlert("Info", "Please select a book to view availability.");
            }
        });
        
        bookTable.setOnMouseClicked(clickEvent -> {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                isbnField.setText(selected.getIsbn());
                titleField.setText(selected.getTitle());
                descriptionField.setText(selected.getDescription() != null ? selected.getDescription() : "");
                authorsField.setText(selected.getAuthors() != null ? selected.getAuthors() : "");
                pagesField.setText(selected.getNumberOfPages() != null ? selected.getNumberOfPages().toString() : "");
                publisherField.setText(selected.getPublisher() != null ? selected.getPublisher() : "");
                publicationDatePicker.setValue(selected.getPublicationDate());
            }
        });
        
        refreshBookTable(bookTable);
        
        tab.setContent(vbox);
        return tab;
    }

    private Tab createBookCopyTab() {
        Tab tab = new Tab("Book Copy Management");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField barcodeField = new TextField();
        TextField isbnField = new TextField();
        TextField locationField = new TextField();
        ComboBox<BookStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(BookStatus.values());
        statusCombo.setValue(BookStatus.AVAILABLE);
        
        form.add(new Label("Barcode:"), 0, 0);
        form.add(barcodeField, 1, 0);
        form.add(new Label("ISBN:"), 0, 1);
        form.add(isbnField, 1, 1);
        form.add(new Label("Physical Location:"), 0, 2);
        form.add(locationField, 1, 2);
        form.add(new Label("Status:"), 0, 3);
        form.add(statusCombo, 1, 3);
        
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add Copy");
        Button updateBtn = new Button("Update Copy");
        Button deleteBtn = new Button("Delete Copy");
        Button clearBtn = new Button("Clear");
        buttonBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);
        
        TableView<BookCopy> copyTable = new TableView<>();
        copyTable.setPrefHeight(400);
        
        TableColumn<BookCopy, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBarcode()));
        barcodeCol.setPrefWidth(120);
        
        TableColumn<BookCopy, String> isbnColTable = new TableColumn<>("ISBN");
        isbnColTable.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getBook().getIsbn());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        isbnColTable.setPrefWidth(120);
        
        TableColumn<BookCopy, String> titleCol = new TableColumn<>("Book Title");
        titleCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getBook().getTitle());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        titleCol.setPrefWidth(250);
        
        TableColumn<BookCopy, String> authorsCol = new TableColumn<>("Authors");
        authorsCol.setCellValueFactory(cellData -> {
            try {
                String authors = cellData.getValue().getBook().getAuthors();
                return new SimpleStringProperty(authors != null ? authors : "");
            } catch (Exception e) {
                return new SimpleStringProperty("");
            }
        });
        authorsCol.setPrefWidth(200);
        
        TableColumn<BookCopy, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPhysicalLocation() != null ? 
                cellData.getValue().getPhysicalLocation() : ""));
        locationCol.setPrefWidth(150);
        
        TableColumn<BookCopy, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);
        
        TableColumn<BookCopy, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(cellData -> {
            BookCopy copy = cellData.getValue();
            if (copy.getStatus() == BookStatus.BORROWED) {
                try {
                    List<Loan> activeLoans = libraryService.getActiveLoans();
                    for (Loan loan : activeLoans) {
                        for (BookCopy loanCopy : loan.getBookCopies()) {
                            if (loanCopy.getBarcode().equals(copy.getBarcode())) {
                                return new SimpleStringProperty(loan.getDueDate().format(dateFormatter));
                            }
                        }
                    }
                } catch (Exception e) {
                    return new SimpleStringProperty("Error");
                }
            }
            return new SimpleStringProperty("");
        });
        dueDateCol.setPrefWidth(100);
        
        copyTable.getColumns().addAll(barcodeCol, isbnColTable, titleCol, authorsCol, 
                                      locationCol, statusCol, dueDateCol);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by barcode, ISBN, or title...");
        Button searchBtn = new Button("Search");
        Button refreshBtn = new Button("Refresh");
        HBox searchBox = new HBox(10, searchField, searchBtn, refreshBtn);
        
        vbox.getChildren().addAll(
            new Label("Book Copy Management"),
            form,
            buttonBox,
            new Separator(),
            searchBox,
            copyTable
        );
        
        addBtn.setOnAction(addEvent -> {
            try {
                if (barcodeField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty()) {
                    showAlert("Error", "Barcode and ISBN are required!");
                    return;
                }
                
                Book book = libraryService.getBook(isbnField.getText().trim());
                if (book == null) {
                    showAlert("Error", "Book with ISBN " + isbnField.getText().trim() + " not found!");
                    return;
                }
                
                BookCopy bookCopy = new BookCopy(
                    barcodeField.getText().trim(),
                    locationField.getText().trim(),
                    statusCombo.getValue(),
                    book
                );
                libraryService.createBookCopy(bookCopy);
                refreshBookCopyTable(copyTable);
                clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo);
                showAlert("Success", "Book copy added successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add book copy: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(updateEvent -> {
            try {
                if (barcodeField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book copy to update!");
                    return;
                }
                
                Book book = libraryService.getBook(isbnField.getText().trim());
                if (book == null) {
                    showAlert("Error", "Book with ISBN " + isbnField.getText().trim() + " not found!");
                    return;
                }
                
                BookCopy bookCopy = new BookCopy(
                    barcodeField.getText().trim(),
                    locationField.getText().trim(),
                    statusCombo.getValue(),
                    book
                );
                libraryService.updateBookCopy(bookCopy);
                refreshBookCopyTable(copyTable);
                showAlert("Success", "Book copy updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update book copy: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(deleteEvent -> {
            try {
                if (barcodeField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book copy to delete!");
                    return;
                }
                
                libraryService.deleteBookCopy(barcodeField.getText().trim());
                refreshBookCopyTable(copyTable);
                clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo);
                showAlert("Success", "Book copy deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete book copy: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(clearEvent -> clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo));
        
        searchBtn.setOnAction(searchEvent -> {
            String searchTerm = searchField.getText().trim().toLowerCase();
            if (searchTerm.isEmpty()) {
                refreshBookCopyTable(copyTable);
            } else {
                try {
                    List<BookCopy> allCopies = libraryService.getAllBookCopies();
                    List<BookCopy> filtered = allCopies.stream()
                        .filter(copy -> {
                            try {
                                return copy.getBarcode().toLowerCase().contains(searchTerm) ||
                                       copy.getBook().getIsbn().toLowerCase().contains(searchTerm) ||
                                       copy.getBook().getTitle().toLowerCase().contains(searchTerm);
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
                    copyTable.getItems().setAll(filtered);
                } catch (Exception ex) {
                    showAlert("Error", "Search failed: " + ex.getMessage());
                }
            }
        });
        
        refreshBtn.setOnAction(refreshEvent -> refreshBookCopyTable(copyTable));
        
        copyTable.setOnMouseClicked(clickEvent -> {
            BookCopy selected = copyTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    barcodeField.setText(selected.getBarcode());
                    isbnField.setText(selected.getBook().getIsbn());
                    locationField.setText(selected.getPhysicalLocation() != null ? selected.getPhysicalLocation() : "");
                    statusCombo.setValue(selected.getStatus());
                } catch (Exception ex) {
                    showAlert("Error", "Failed to load copy details: " + ex.getMessage());
                }
            }
        });
        
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                refreshBookCopyTable(copyTable);
            }
        });
        
        tab.setContent(vbox);
        return tab;
    }

    private Tab createLoanTab() {
        Tab tab = new Tab("Loan Management");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        // Create Loan Section
        VBox createLoanBox = new VBox(10);
        createLoanBox.getChildren().add(new Label("Create New Loan"));
        
        GridPane loanForm = new GridPane();
        loanForm.setHgap(10);
        loanForm.setVgap(10);
        
        TextField studentIdField = new TextField();
        TextArea barcodesField = new TextArea();
        barcodesField.setPrefRowCount(3);
        barcodesField.setPromptText("Enter barcodes separated by commas or new lines");
        TextField loanDaysField = new TextField();
        loanDaysField.setText("14");
        
        loanForm.add(new Label("Student Bronco ID:"), 0, 0);
        loanForm.add(studentIdField, 1, 0);
        loanForm.add(new Label("Book Barcodes:"), 0, 1);
        loanForm.add(barcodesField, 1, 1);
        loanForm.add(new Label("Loan Days (max 180):"), 0, 2);
        loanForm.add(loanDaysField, 1, 2);
        
        Button createLoanBtn = new Button("Create Loan");
        createLoanBox.getChildren().addAll(loanForm, createLoanBtn);
        
        VBox activeLoansBox = new VBox(10);
        activeLoansBox.getChildren().add(new Label("Active Loans"));
        
        TableView<Loan> activeLoanTable = new TableView<>();
        activeLoanTable.setPrefHeight(300);
        
        TableColumn<Loan, String> loanNumCol = new TableColumn<>("Loan #");
        loanNumCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLoanNumber().toString()));
        loanNumCol.setPrefWidth(80);
        
        TableColumn<Loan, String> studentNameCol = new TableColumn<>("Student Name");
        studentNameCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getStudent().getName());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        studentNameCol.setPrefWidth(150);
        
        TableColumn<Loan, String> studentIdCol = new TableColumn<>("Bronco ID");
        studentIdCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getStudent().getBroncoId());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        studentIdCol.setPrefWidth(100);
        
        TableColumn<Loan, String> borrowDateCol = new TableColumn<>("Borrow Date");
        borrowDateCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBorrowingDate().format(dateFormatter)));
        borrowDateCol.setPrefWidth(100);
        
        TableColumn<Loan, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDueDate().format(dateFormatter)));
        dueDateCol.setPrefWidth(100);
        
        TableColumn<Loan, String> bookCountCol = new TableColumn<>("Books");
        bookCountCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getBookCopies().size()));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        bookCountCol.setPrefWidth(70);
        
        TableColumn<Loan, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            if (loan.isOverdue()) {
                return new SimpleStringProperty("OVERDUE");
            } else {
                return new SimpleStringProperty("ACTIVE");
            }
        });
        statusCol.setPrefWidth(80);
        
        TableColumn<Loan, String> booksListCol = new TableColumn<>("Book Titles");
        booksListCol.setCellValueFactory(cellData -> {
            try {
                StringBuilder titles = new StringBuilder();
                for (BookCopy copy : cellData.getValue().getBookCopies()) {
                    if (titles.length() > 0) titles.append(", ");
                    titles.append(copy.getBook().getTitle());
                }
                return new SimpleStringProperty(titles.toString());
            } catch (Exception e) {
                return new SimpleStringProperty("Error loading titles");
            }
        });
        booksListCol.setPrefWidth(300);
        
        activeLoanTable.getColumns().addAll(loanNumCol, studentNameCol, studentIdCol, 
                                           borrowDateCol, dueDateCol, bookCountCol, statusCol, booksListCol);
        
        Button returnLoanBtn = new Button("Return Selected Loan");
        Button showReceiptBtn = new Button("Show Receipt");
        Button checkOverdueBtn = new Button("Check Overdue");
        Button refreshLoansBtn = new Button("Refresh");
        HBox loanButtonBox = new HBox(10, returnLoanBtn, showReceiptBtn, checkOverdueBtn, refreshLoansBtn);
        
        activeLoansBox.getChildren().addAll(activeLoanTable, loanButtonBox);
        
        vbox.getChildren().addAll(
            createLoanBox,
            new Separator(),
            activeLoansBox
        );
        
        createLoanBtn.setOnAction(createEvent -> {
            try {
                String studentId = studentIdField.getText().trim();
                String barcodesText = barcodesField.getText().trim();
                int loanDays = Integer.parseInt(loanDaysField.getText().trim());
                
                if (loanDays <= 0) {
                    showAlert("Error", "Loan days must be greater than 0!");
                    return;
                }

                if (studentId.isEmpty() || barcodesText.isEmpty()) {
                    showAlert("Error", "Student ID and barcodes are required!");
                    return;
                }
                
                if (loanDays > 180) {
                    showAlert("Error", "Loan duration cannot exceed 180 days!");
                    return;
                }
                
                String[] barcodes = barcodesText.split("[,\\n\\r]+");
                Set<BookCopy> bookCopies = new HashSet<>();
                
                for (String barcode : barcodes) {
                    barcode = barcode.trim();
                    if (!barcode.isEmpty()) {
                        BookCopy copy = libraryService.getBookCopy(barcode);
                        if (copy == null) {
                            showAlert("Error", "Book copy with barcode " + barcode + " not found!");
                            return;
                        }
                        if (copy.getStatus() != BookStatus.AVAILABLE) {
                            showAlert("Error", "Book copy " + barcode + " is not available!");
                            return;
                        }
                        bookCopies.add(copy);
                    }
                }
                
                if (bookCopies.isEmpty()) {
                    showAlert("Error", "No valid book copies found!");
                    return;
                }
                
                if (bookCopies.size() > 5) {
                    showAlert("Error", "Cannot borrow more than 5 book copies in a single loan!");
                    return;
                }
                
                Loan loan = libraryService.createLoan(studentId, bookCopies, loanDays);
                refreshActiveLoanTable(activeLoanTable);
                
                studentIdField.clear();
                barcodesField.clear();
                loanDaysField.setText("14");
                
                showAlert("Success", "Loan created successfully! Loan Number: " + loan.getLoanNumber());
                showReceipt(loan);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number for loan days!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to create loan: " + ex.getMessage());
            }
        });

        returnLoanBtn.setOnAction(returnEvent -> {
            Loan selected = activeLoanTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    libraryService.returnLoan(selected.getLoanNumber());
                    refreshActiveLoanTable(activeLoanTable);
                    showAlert("Success", "Loan returned successfully!");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to return loan: " + ex.getMessage());
                }
            } else {
                showAlert("Warning", "Please select a loan to return.");
            }
        });
        
        showReceiptBtn.setOnAction(receiptEvent -> {
            Loan selected = activeLoanTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showReceipt(selected);
            } else {
                showAlert("Warning", "Please select a loan to show receipt.");
            }
        });
        
        checkOverdueBtn.setOnAction(overdueEvent -> {
            try {
                List<Loan> overdueLoans = libraryService.getOverdueLoans();
                showOverdueLoansDialog(overdueLoans);
            } catch (Exception ex) {
                showAlert("Error", "Failed to check overdue loans: " + ex.getMessage());
            }
        });
        
        refreshLoansBtn.setOnAction(refreshLoanEvent -> refreshActiveLoanTable(activeLoanTable));
        
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                refreshActiveLoanTable(activeLoanTable);
            }
        });
        
        tab.setContent(vbox);
        return tab;
    }
    
    private Tab createReportTab() {
        Tab tab = new Tab("Reports");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        GridPane filterForm = new GridPane();
        filterForm.setHgap(10);
        filterForm.setVgap(10);
        
        TextField studentFilterField = new TextField();
        studentFilterField.setPromptText("Student Bronco ID (optional)");
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        
        filterForm.add(new Label("Student Filter:"), 0, 0);
        filterForm.add(studentFilterField, 1, 0);
        filterForm.add(new Label("Start Date:"), 0, 1);
        filterForm.add(startDatePicker, 1, 1);
        filterForm.add(new Label("End Date:"), 0, 2);
        filterForm.add(endDatePicker, 1, 2);
        
        Button generateReportBtn = new Button("Generate Consolidated Report");
        Button showOverdueBtn = new Button("Show Overdue Loans");
        Button showActiveBtn = new Button("Show Active Loans");
        Button showAllLoansBtn = new Button("Show All Loans");
        HBox reportButtonBox = new HBox(10, generateReportBtn, showOverdueBtn, showActiveBtn, showAllLoansBtn);
        
        TableView<Loan> reportTable = new TableView<>();
        reportTable.setPrefHeight(450);
        
        TableColumn<Loan, String> reportLoanNumCol = new TableColumn<>("Loan #");
        reportLoanNumCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLoanNumber().toString()));
        reportLoanNumCol.setPrefWidth(80);
        
        TableColumn<Loan, String> reportStudentCol = new TableColumn<>("Student");
        reportStudentCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getStudent().getName());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        reportStudentCol.setPrefWidth(150);
        
        TableColumn<Loan, String> reportBroncoIdCol = new TableColumn<>("Bronco ID");
        reportBroncoIdCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getStudent().getBroncoId());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        reportBroncoIdCol.setPrefWidth(100);
        
        TableColumn<Loan, String> reportBorrowCol = new TableColumn<>("Borrow Date");
        reportBorrowCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBorrowingDate().format(dateFormatter)));
        reportBorrowCol.setPrefWidth(100);
        
        TableColumn<Loan, String> reportDueCol = new TableColumn<>("Due Date");
        reportDueCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDueDate().format(dateFormatter)));
        reportDueCol.setPrefWidth(100);
        
        TableColumn<Loan, String> reportReturnCol = new TableColumn<>("Return Date");
        reportReturnCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getReturnDate() != null ? 
                cellData.getValue().getReturnDate().format(dateFormatter) : ""));
        reportReturnCol.setPrefWidth(100);
        
        TableColumn<Loan, String> reportStatusCol = new TableColumn<>("Status");
        reportStatusCol.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            if (loan.getReturnDate() != null) {
                return new SimpleStringProperty("RETURNED");
            } else if (loan.isOverdue()) {
                return new SimpleStringProperty("OVERDUE");
            } else {
                return new SimpleStringProperty("ACTIVE");
            }
        });
        reportStatusCol.setPrefWidth(100);
        
        TableColumn<Loan, String> reportBookCountCol = new TableColumn<>("Books");
        reportBookCountCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getBookCopies().size()));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        reportBookCountCol.setPrefWidth(70);
        
        TableColumn<Loan, String> reportBooksCol = new TableColumn<>("Book Titles");
        reportBooksCol.setCellValueFactory(cellData -> {
            try {
                StringBuilder titles = new StringBuilder();
                for (BookCopy copy : cellData.getValue().getBookCopies()) {
                    if (titles.length() > 0) titles.append(", ");
                    titles.append(copy.getBook().getTitle());
                }
                return new SimpleStringProperty(titles.toString());
            } catch (Exception e) {
                return new SimpleStringProperty("Error loading titles");
            }
        });
        reportBooksCol.setPrefWidth(300);
        
        reportTable.getColumns().addAll(reportLoanNumCol, reportStudentCol, reportBroncoIdCol,
                                        reportBorrowCol, reportDueCol, reportReturnCol, 
                                        reportStatusCol, reportBookCountCol, reportBooksCol);
        
        vbox.getChildren().addAll(
            new Label("Loan Reports"),
            filterForm,
            reportButtonBox,
            new Separator(),
            reportTable
        );
        
        generateReportBtn.setOnAction(generateEvent -> {
            try {
                List<Loan> loans;
                String studentFilter = studentFilterField.getText().trim();
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                
                if (!studentFilter.isEmpty()) {
                    loans = libraryService.getLoansByStudent(studentFilter);
                } else if (startDate != null && endDate != null) {
                    loans = libraryService.getLoansByDateRange(startDate, endDate);
                } else {
                    loans = libraryService.getAllLoans();
                }
                
                reportTable.getItems().setAll(loans);
                showAlert("Success", "Report generated with " + loans.size() + " loans.");
            } catch (Exception ex) {
                showAlert("Error", "Failed to generate report: " + ex.getMessage());
            }
        });
        
        showOverdueBtn.setOnAction(overdueEvent -> {
            try {
                List<Loan> overdueLoans = libraryService.getOverdueLoans();
                reportTable.getItems().setAll(overdueLoans);
                showAlert("Info", "Showing " + overdueLoans.size() + " overdue loans.");
            } catch (Exception ex) {
                showAlert("Error", "Failed to show overdue loans: " + ex.getMessage());
            }
        });
        
        showActiveBtn.setOnAction(activeEvent -> {
            try {
                List<Loan> activeLoans = libraryService.getActiveLoans();
                reportTable.getItems().setAll(activeLoans);
                showAlert("Info", "Showing " + activeLoans.size() + " active loans.");
            } catch (Exception ex) {
                showAlert("Error", "Failed to show active loans: " + ex.getMessage());
            }
        });
        
        showAllLoansBtn.setOnAction(allLoansEvent -> {
            try {
                List<Loan> allLoans = libraryService.getAllLoans();
                reportTable.getItems().setAll(allLoans);
                showAlert("Info", "Showing " + allLoans.size() + " total loans.");
            } catch (Exception ex) {
                showAlert("Error", "Failed to show all loans: " + ex.getMessage());
            }
        });
        
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                try {
                    List<Loan> allLoans = libraryService.getAllLoans();
                    reportTable.getItems().setAll(allLoans);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to load initial loan data: " + ex.getMessage());
                }
            }
        });
        
        tab.setContent(vbox);
        return tab;
    }

    // Helper methods
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showOverdueLoansDialog(List<Loan> overdueLoans) {
        if (overdueLoans.isEmpty()) {
            showAlert("Info", "No overdue loans found.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Overdue Loans");
        alert.setHeaderText("Found " + overdueLoans.size() + " overdue loans");
        
        TableView<Loan> overdueTable = new TableView<>();
        overdueTable.setPrefHeight(300);
        overdueTable.setPrefWidth(600);
        
        TableColumn<Loan, String> loanCol = new TableColumn<>("Loan #");
        loanCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLoanNumber().toString()));
        loanCol.setPrefWidth(80);
        
        TableColumn<Loan, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getStudent().getName());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });
        studentCol.setPrefWidth(150);
        
        TableColumn<Loan, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDueDate().format(dateFormatter)));
        dueCol.setPrefWidth(100);
        
        TableColumn<Loan, String> daysCol = new TableColumn<>("Days Overdue");
        daysCol.setCellValueFactory(cellData -> {
            long days = LocalDate.now().toEpochDay() - cellData.getValue().getDueDate().toEpochDay();
            return new SimpleStringProperty(String.valueOf(days));
        });
        daysCol.setPrefWidth(100);
        
        TableColumn<Loan, String> booksCol = new TableColumn<>("Books");
        booksCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getBookCopies().size()));
            } catch (Exception e) {
                return new SimpleStringProperty("0");
            }
        });
        booksCol.setPrefWidth(70);
        
        overdueTable.getColumns().addAll(loanCol, studentCol, dueCol, daysCol, booksCol);
        overdueTable.getItems().setAll(overdueLoans);
        
        alert.getDialogPane().setExpandableContent(overdueTable);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
    
    private void showReceipt(Loan loan) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== LIBRARY LOAN RECEIPT ===\n\n");
        receipt.append("Loan Number: ").append(loan.getLoanNumber()).append("\n");
        
        try {
            receipt.append("Student Name: ").append(loan.getStudent().getName()).append("\n");
            receipt.append("Bronco ID: ").append(loan.getStudent().getBroncoId()).append("\n");
        } catch (Exception e) {
            receipt.append("Student: Error loading student details\n");
        }
        
        receipt.append("Borrowing Date: ").append(loan.getBorrowingDate().format(dateFormatter)).append("\n");
        receipt.append("Due Date: ").append(loan.getDueDate().format(dateFormatter)).append("\n");
        if (loan.getReturnDate() != null) {
            receipt.append("Return Date: ").append(loan.getReturnDate().format(dateFormatter)).append("\n");
        }
        
        try {
            receipt.append("\nBorrowed Items (").append(loan.getBookCopies().size()).append("):\n");
            receipt.append("-".repeat(40)).append("\n");
            
            for (BookCopy copy : loan.getBookCopies()) {
                receipt.append("• ").append(copy.getBook().getTitle()).append("\n");
                receipt.append("  Authors: ").append(copy.getBook().getAuthors() != null ? copy.getBook().getAuthors() : "").append("\n");
                receipt.append("  ISBN: ").append(copy.getBook().getIsbn()).append("\n");
                receipt.append("  Barcode: ").append(copy.getBarcode()).append("\n");
                receipt.append("  Location: ").append(copy.getPhysicalLocation() != null ? copy.getPhysicalLocation() : "").append("\n\n");
            }
        } catch (Exception e) {
            receipt.append("\nError loading book details\n");
        }
        
        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Please return all items by the due date.\n");
        receipt.append("All items must be returned together.\n");
        receipt.append("Maximum loan period: 180 days\n");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Receipt");
        alert.setHeaderText("Loan Receipt #" + loan.getLoanNumber());
        
        TextArea textArea = new TextArea(receipt.toString());
        textArea.setEditable(false);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);
        
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
    
    private void showBookAvailability(Book book) {
        try {
            List<BookCopy> copies = libraryService.getBookCopiesByISBN(book.getIsbn());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Availability");
            alert.setHeaderText("Availability for: " + book.getTitle());
            
            TableView<BookCopy> availabilityTable = new TableView<>();
            availabilityTable.setPrefHeight(250);
            availabilityTable.setPrefWidth(500);
            
            TableColumn<BookCopy, String> barcodeCol = new TableColumn<>("Barcode");
            barcodeCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getBarcode()));
            barcodeCol.setPrefWidth(120);
            
            TableColumn<BookCopy, String> locationCol = new TableColumn<>("Location");
            locationCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getPhysicalLocation() != null ? 
                    cellData.getValue().getPhysicalLocation() : ""));
            locationCol.setPrefWidth(150);
            
            TableColumn<BookCopy, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));
            statusCol.setPrefWidth(100);
            
            TableColumn<BookCopy, String> dueDateCol = new TableColumn<>("Due Date");
            dueDateCol.setCellValueFactory(cellData -> {
                BookCopy copy = cellData.getValue();
                if (copy.getStatus() == BookStatus.BORROWED) {
                    try {
                        List<Loan> activeLoans = libraryService.getActiveLoans();
                        for (Loan loan : activeLoans) {
                            for (BookCopy loanCopy : loan.getBookCopies()) {
                                if (loanCopy.getBarcode().equals(copy.getBarcode())) {
                                    return new SimpleStringProperty(loan.getDueDate().format(dateFormatter));
                                }
                            }
                        }
                    } catch (Exception e) {
                        return new SimpleStringProperty("Error");
                    }
                }
                return new SimpleStringProperty("");
            });
            dueDateCol.setPrefWidth(100);
            
            availabilityTable.getColumns().addAll(barcodeCol, locationCol, statusCol, dueDateCol);
            availabilityTable.setItems(javafx.collections.FXCollections.observableArrayList(copies));
            
            int available = (int) copies.stream().filter(c -> c.getStatus() == BookStatus.AVAILABLE).count();
            int borrowed = copies.size() - available;
            
            String summary = String.format("Total Copies: %d | Available: %d | Borrowed: %d", 
                                          copies.size(), available, borrowed);
            alert.setContentText(summary);
            
            alert.getDialogPane().setExpandableContent(availabilityTable);
            alert.getDialogPane().setExpanded(true);
            alert.showAndWait();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to show book availability: " + e.getMessage());
        }
    }

    private void refreshStudentTable(TableView<Student> table) {
        try {
            table.getItems().setAll(libraryService.getAllStudents());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh student list: " + e.getMessage());
        }
    }
    
    private void refreshBookTable(TableView<Book> table) {
        try {
            table.getItems().setAll(libraryService.getAllBooks());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh book list: " + e.getMessage());
        }
    }
    
    private void refreshBookCopyTable(TableView<BookCopy> table) {
        try {
            table.getItems().setAll(libraryService.getAllBookCopies());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh book copy list: " + e.getMessage());
        }
    }
    
    private void refreshActiveLoanTable(TableView<Loan> table) {
        try {
            table.getItems().setAll(libraryService.getActiveLoans());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh loans list: " + e.getMessage());
        }
    }
    
    private void clearStudentForm(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    
    private void clearBookForm(TextField isbnField, TextField titleField, TextArea descriptionField,
                              TextField authorsField, TextField pagesField, TextField publisherField,
                              DatePicker publicationDatePicker) {
        isbnField.clear();
        titleField.clear();
        descriptionField.clear();
        authorsField.clear();
        pagesField.clear();
        publisherField.clear();
        publicationDatePicker.setValue(null);
    }
    
    private void clearBookCopyForm(TextField barcodeField, TextField isbnField, 
                                  TextField locationField, ComboBox<BookStatus> statusCombo) {
        barcodeField.clear();
        isbnField.clear();
        locationField.clear();
        statusCombo.setValue(BookStatus.AVAILABLE);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}