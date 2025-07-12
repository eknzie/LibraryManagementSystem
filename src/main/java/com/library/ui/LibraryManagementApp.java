package com.library.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.library.service.LibraryService;
import com.library.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class LibraryManagementApp extends Application {
    private LibraryService libraryService;
    private TabPane tabPane;
    
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
        
        Scene scene = new Scene(tabPane, 1200, 800);
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
        
        ListView<Student> studentList = new ListView<>();
        studentList.setPrefHeight(300);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search students by name or Bronco ID...");
        Button searchBtn = new Button("Search");
        HBox searchBox = new HBox(10, searchField, searchBtn);
        
        vbox.getChildren().addAll(
            new Label("Student Management"),
            form,
            buttonBox,
            new Separator(),
            searchBox,
            studentList
        );
        
        // Event handlers
        addBtn.setOnAction(e -> {
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
                refreshStudentList(studentList);
                clearStudentForm(broncoIdField, nameField, addressField, degreeField);
                showAlert("Success", "Student added successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add student: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(e -> {
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
                refreshStudentList(studentList);
                showAlert("Success", "Student updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update student: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(e -> {
            try {
                if (broncoIdField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a student to delete!");
                    return;
                }
                
                libraryService.deleteStudent(broncoIdField.getText().trim());
                refreshStudentList(studentList);
                clearStudentForm(broncoIdField, nameField, addressField, degreeField);
                showAlert("Success", "Student deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete student: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(e -> clearStudentForm(broncoIdField, nameField, addressField, degreeField));
        
        searchBtn.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                refreshStudentList(studentList);
            } else {
                studentList.getItems().setAll(libraryService.searchStudents(searchTerm));
            }
        });
        
        studentList.setOnMouseClicked(e -> {
            Student selected = studentList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                broncoIdField.setText(selected.getBroncoId());
                nameField.setText(selected.getName());
                addressField.setText(selected.getAddress() != null ? selected.getAddress() : "");
                degreeField.setText(selected.getDegree() != null ? selected.getDegree() : "");
            }
        });
        
        refreshStudentList(studentList);
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
        
        ListView<Book> bookList = new ListView<>();
        bookList.setPrefHeight(300);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search books by title, author, or ISBN...");
        Button searchBtn = new Button("Search");
        Button showAvailabilityBtn = new Button("Show Availability");
        HBox searchBox = new HBox(10, searchField, searchBtn, showAvailabilityBtn);
        
        vbox.getChildren().addAll(
            new Label("Book Management"),
            form,
            buttonBox,
            new Separator(),
            searchBox,
            bookList
        );
        
        // Event handlers
        addBtn.setOnAction(e -> {
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
                refreshBookList(bookList);
                clearBookForm(isbnField, titleField, descriptionField, authorsField, 
                           pagesField, publisherField, publicationDatePicker);
                showAlert("Success", "Book added successfully!");
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number for pages!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add book: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(e -> {
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
                refreshBookList(bookList);
                showAlert("Success", "Book updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update book: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(e -> {
            try {
                if (isbnField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book to delete!");
                    return;
                }
                
                libraryService.deleteBook(isbnField.getText().trim());
                refreshBookList(bookList);
                clearBookForm(isbnField, titleField, descriptionField, authorsField, 
                           pagesField, publisherField, publicationDatePicker);
                showAlert("Success", "Book deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete book: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(e -> clearBookForm(isbnField, titleField, descriptionField, 
                                              authorsField, pagesField, publisherField, publicationDatePicker));
        
        searchBtn.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                refreshBookList(bookList);
            } else {
                bookList.getItems().setAll(libraryService.searchBooks(searchTerm));
            }
        });
        
        showAvailabilityBtn.setOnAction(e -> {
            Book selected = bookList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showBookAvailability(selected);
            } else {
                showAlert("Info", "Please select a book to view availability.");
            }
        });
        
        bookList.setOnMouseClicked(e -> {
            Book selected = bookList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                isbnField.setText(selected.getIsbn());
                titleField.setText(selected.getTitle());
                descriptionField.setText(selected.getDescription());
                authorsField.setText(selected.getAuthors());
                pagesField.setText(selected.getNumberOfPages() != null ? selected.getNumberOfPages().toString() : "");
                publisherField.setText(selected.getPublisher());
                publicationDatePicker.setValue(selected.getPublicationDate());
            }
        });
        
        refreshBookList(bookList);
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
        
        ListView<BookCopy> copyList = new ListView<>();
        copyList.setPrefHeight(300);
        
        vbox.getChildren().addAll(
            new Label("Book Copy Management"),
            form,
            buttonBox,
            new Separator(),
            copyList
        );
        
        // Event handlers
        addBtn.setOnAction(e -> {
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
                refreshBookCopyList(copyList);
                clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo);
                showAlert("Success", "Book copy added successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to add book copy: " + ex.getMessage());
            }
        });
        
        updateBtn.setOnAction(e -> {
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
                refreshBookCopyList(copyList);
                showAlert("Success", "Book copy updated successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to update book copy: " + ex.getMessage());
            }
        });
        
        deleteBtn.setOnAction(e -> {
            try {
                if (barcodeField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please select a book copy to delete!");
                    return;
                }
                
                libraryService.deleteBookCopy(barcodeField.getText().trim());
                refreshBookCopyList(copyList);
                clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo);
                showAlert("Success", "Book copy deleted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Failed to delete book copy: " + ex.getMessage());
            }
        });
        
        clearBtn.setOnAction(e -> clearBookCopyForm(barcodeField, isbnField, locationField, statusCombo));
        
        copyList.setOnMouseClicked(e -> {
            BookCopy selected = copyList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                barcodeField.setText(selected.getBarcode());
                isbnField.setText(selected.getBook().getIsbn());
                locationField.setText(selected.getPhysicalLocation());
                statusCombo.setValue(selected.getStatus());
            }
        });
        
        refreshBookCopyList(copyList);
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
        
        // Active Loans Section
        VBox activeLoansBox = new VBox(10);
        activeLoansBox.getChildren().add(new Label("Active Loans"));
        
        ListView<Loan> activeLoansList = new ListView<>();
        activeLoansList.setPrefHeight(200);
        
        Button returnLoanBtn = new Button("Return Selected Loan");
        Button showReceiptBtn = new Button("Show Receipt");
        Button checkOverdueBtn = new Button("Check Overdue");
        HBox loanButtonBox = new HBox(10, returnLoanBtn, showReceiptBtn, checkOverdueBtn);
        
        activeLoansBox.getChildren().addAll(activeLoansList, loanButtonBox);
        
        vbox.getChildren().addAll(
            createLoanBox,
            new Separator(),
            activeLoansBox
        );
        
        // Event handlers
        createLoanBtn.setOnAction(e -> {
            
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
                
                // Parse barcodes
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
                refreshActiveLoansList(activeLoansList);
                
                // Clear form
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

        returnLoanBtn.setOnAction(e -> {
            Loan selected = activeLoansList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    libraryService.returnLoan(selected.getLoanNumber());
                    refreshActiveLoansList(activeLoansList);
                    showAlert("Success", "Loan returned successfully!");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to return loan: " + ex.getMessage());
                }
            } else {
                showAlert("Warning", "Please select a loan to return.");
            }
        });
        
        showReceiptBtn.setOnAction(e -> {
            Loan selected = activeLoansList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showReceipt(selected);
            } else {
                showAlert("Warning", "Please select a loan to show receipt.");
            }
        });
        
        checkOverdueBtn.setOnAction(e -> {
            try {
                List<Loan> overdueLoans = libraryService.getOverdueLoans();
                if (overdueLoans.isEmpty()) {
                    showAlert("Info", "No overdue loans found.");
                } else {
                    StringBuilder message = new StringBuilder("Overdue Loans (" + overdueLoans.size() + "):\n\n");
                    for (Loan loan : overdueLoans) {
                        message.append("Loan #").append(loan.getLoanNumber())
                               .append(" - ").append(loan.getStudent().getName())
                               .append(" (Due: ").append(loan.getDueDate()).append(")\n");
                    }
                    showAlert("Overdue Loans", message.toString());
                }
            } catch (Exception ex) {
                showAlert("Error", "Failed to check overdue loans: " + ex.getMessage());
            }
        });
        
        refreshActiveLoansList(activeLoansList);
        tab.setContent(vbox);
        return tab;
    }
    
    private Tab createReportTab() {
        Tab tab = new Tab("Reports");
        tab.setClosable(false);
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        // Report filters
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
        HBox reportButtonBox = new HBox(10, generateReportBtn, showOverdueBtn, showActiveBtn);
        
        TextArea reportArea = new TextArea();
        reportArea.setPrefRowCount(25);
        reportArea.setEditable(false);
        
        vbox.getChildren().addAll(
            new Label("Loan Reports"),
            filterForm,
            reportButtonBox,
            new Separator(),
            reportArea
        );
        
        // Event handlers
        generateReportBtn.setOnAction(e -> {
            try {
                StringBuilder report = new StringBuilder();
                report.append("=== CONSOLIDATED LOAN REPORT ===\n");
                report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
                
                List<Loan> loans;
                String studentFilter = studentFilterField.getText().trim();
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                
                if (!studentFilter.isEmpty()) {
                    loans = libraryService.getLoansByStudent(studentFilter);
                    report.append("Filtered by student: ").append(studentFilter).append("\n");
                } else if (startDate != null && endDate != null) {
                    loans = libraryService.getLoansByDateRange(startDate, endDate);
                    report.append("Date range: ").append(startDate).append(" to ").append(endDate).append("\n");
                } else {
                    loans = libraryService.getAllLoans();
                    report.append("All loans\n");
                }
                
                report.append("Total loans: ").append(loans.size()).append("\n");
                report.append("=".repeat(50)).append("\n\n");
                
                for (Loan loan : loans) {
                    report.append("Loan #").append(loan.getLoanNumber()).append("\n");
                    report.append("Student: ").append(loan.getStudent().getName())
                          .append(" (").append(loan.getStudent().getBroncoId()).append(")\n");
                    report.append("Borrowed: ").append(loan.getBorrowingDate()).append("\n");
                    report.append("Due: ").append(loan.getDueDate()).append("\n");
                    if (loan.getReturnDate() != null) {
                        report.append("Returned: ").append(loan.getReturnDate()).append("\n");
                        report.append("Status: Completed\n");
                    } else {
                        report.append("Status: Active");
                        if (loan.isOverdue()) {
                            report.append(" (OVERDUE)");
                        }
                        report.append("\n");
                    }
                    report.append("Books borrowed (").append(loan.getBookCopies().size()).append("):\n");
                    for (BookCopy copy : loan.getBookCopies()) {
                        report.append("  - ").append(copy.getBook().getTitle())
                              .append(" by ").append(copy.getBook().getAuthors())
                              .append(" (Barcode: ").append(copy.getBarcode())
                              .append(", Location: ").append(copy.getPhysicalLocation()).append(")\n");
                    }
                    report.append("-".repeat(30)).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            } catch (Exception ex) {
                showAlert("Error", "Failed to generate report: " + ex.getMessage());
            }
        });
        
        showOverdueBtn.setOnAction(e -> {
            try {
                List<Loan> overdueLoans = libraryService.getOverdueLoans();
                StringBuilder report = new StringBuilder();
                report.append("=== OVERDUE LOANS REPORT ===\n");
                report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
                report.append("Total overdue loans: ").append(overdueLoans.size()).append("\n");
                report.append("=".repeat(40)).append("\n\n");
                
                for (Loan loan : overdueLoans) {
                    long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
                    report.append("Loan #").append(loan.getLoanNumber()).append("\n");
                    report.append("Student: ").append(loan.getStudent().getName())
                          .append(" (").append(loan.getStudent().getBroncoId()).append(")\n");
                    report.append("Due Date: ").append(loan.getDueDate()).append("\n");
                    report.append("Days Overdue: ").append(daysOverdue).append("\n");
                    report.append("Books:\n");
                    for (BookCopy copy : loan.getBookCopies()) {
                        report.append("  - ").append(copy.getBook().getTitle())
                              .append(" (").append(copy.getBarcode()).append(")\n");
                    }
                    report.append("-".repeat(30)).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            } catch (Exception ex) {
                showAlert("Error", "Failed to show overdue loans: " + ex.getMessage());
            }
        });
        
        showActiveBtn.setOnAction(e -> {
            try {
                List<Loan> activeLoans = libraryService.getActiveLoans();
                StringBuilder report = new StringBuilder();
                report.append("=== ACTIVE LOANS REPORT ===\n");
                report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
                report.append("Total active loans: ").append(activeLoans.size()).append("\n");
                report.append("=".repeat(40)).append("\n\n");
                
                for (Loan loan : activeLoans) {
                    report.append("Loan #").append(loan.getLoanNumber()).append("\n");
                    report.append("Student: ").append(loan.getStudent().getName())
                          .append(" (").append(loan.getStudent().getBroncoId()).append(")\n");
                    report.append("Borrowed: ").append(loan.getBorrowingDate()).append("\n");
                    report.append("Due: ").append(loan.getDueDate());
                    if (loan.isOverdue()) {
                        report.append(" (OVERDUE)");
                    }
                    report.append("\n");
                    report.append("Books (").append(loan.getBookCopies().size()).append("):\n");
                    for (BookCopy copy : loan.getBookCopies()) {
                        report.append("  - ").append(copy.getBook().getTitle())
                              .append(" (").append(copy.getBarcode()).append(")\n");
                    }
                    report.append("-".repeat(30)).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            } catch (Exception ex) {
                showAlert("Error", "Failed to show active loans: " + ex.getMessage());
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
    
    private void showReceipt(Loan loan) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== LIBRARY LOAN RECEIPT ===\n\n");
        receipt.append("Loan Number: ").append(loan.getLoanNumber()).append("\n");
        receipt.append("Student Name: ").append(loan.getStudent().getName()).append("\n");
        receipt.append("Bronco ID: ").append(loan.getStudent().getBroncoId()).append("\n");
        receipt.append("Borrowing Date: ").append(loan.getBorrowingDate()).append("\n");
        receipt.append("Due Date: ").append(loan.getDueDate()).append("\n");
        if (loan.getReturnDate() != null) {
            receipt.append("Return Date: ").append(loan.getReturnDate()).append("\n");
        }
        receipt.append("\nBorrowed Items (").append(loan.getBookCopies().size()).append("):\n");
        receipt.append("-".repeat(40)).append("\n");
        
        for (BookCopy copy : loan.getBookCopies()) {
            receipt.append("• ").append(copy.getBook().getTitle()).append("\n");
            receipt.append("  Authors: ").append(copy.getBook().getAuthors()).append("\n");
            receipt.append("  ISBN: ").append(copy.getBook().getIsbn()).append("\n");
            receipt.append("  Barcode: ").append(copy.getBarcode()).append("\n");
            receipt.append("  Location: ").append(copy.getPhysicalLocation()).append("\n\n");
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
            StringBuilder info = new StringBuilder();
            info.append("Book: ").append(book.getTitle()).append("\n");
            info.append("ISBN: ").append(book.getIsbn()).append("\n");
            info.append("Authors: ").append(book.getAuthors()).append("\n\n");
            info.append("Copy Availability:\n");
            info.append("-".repeat(30)).append("\n");
            
            int available = 0, borrowed = 0;
            
            for (BookCopy copy : copies) {
                info.append("Barcode: ").append(copy.getBarcode()).append("\n");
                info.append("Location: ").append(copy.getPhysicalLocation()).append("\n");
                info.append("Status: ").append(copy.getStatus()).append("\n");
                
                if (copy.getStatus() == BookStatus.AVAILABLE) {
                    available++;
                } else {
                    borrowed++;
                    // Find due date if borrowed
                    List<Loan> activeLoans = libraryService.getActiveLoans();
                    for (Loan loan : activeLoans) {
                        if (loan.getBookCopies().contains(copy)) {
                            info.append("Due Date: ").append(loan.getDueDate()).append("\n");
                            break;
                        }
                    }
                }
                info.append("\n");
            }
            
            info.append("-".repeat(30)).append("\n");
            info.append("Summary:\n");
            info.append("Total Copies: ").append(copies.size()).append("\n");
            info.append("Available: ").append(available).append("\n");
            info.append("Borrowed: ").append(borrowed).append("\n");
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Availability");
            alert.setHeaderText("Availability for: " + book.getTitle());
            
            TextArea textArea = new TextArea(info.toString());
            textArea.setEditable(false);
            textArea.setPrefRowCount(15);
            textArea.setPrefColumnCount(50);
            
            alert.getDialogPane().setExpandableContent(textArea);
            alert.getDialogPane().setExpanded(true);
            alert.showAndWait();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to show book availability: " + e.getMessage());
        }
    }

    private void refreshStudentList(ListView<Student> list) {
        try {
            list.getItems().setAll(libraryService.getAllStudents());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh student list: " + e.getMessage());
        }
    }
    
    private void refreshBookList(ListView<Book> list) {
        try {
            list.getItems().setAll(libraryService.getAllBooks());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh book list: " + e.getMessage());
        }
    }
    
    private void refreshBookCopyList(ListView<BookCopy> list) {
        try {
            list.getItems().setAll(libraryService.getAllBookCopies());
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh book copy list: " + e.getMessage());
        }
    }
    
    private void refreshActiveLoansList(ListView<Loan> list) {
        try {
            list.getItems().setAll(libraryService.getActiveLoans());
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
