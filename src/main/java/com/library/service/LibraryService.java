package com.library.service;

import com.library.dao.*;
import com.library.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class LibraryService {
    private StudentDAO studentDAO;
    private BookDAO bookDAO;
    private BookCopyDAO bookCopyDAO;
    private LoanDAO loanDAO;
    
    public LibraryService() {
        this.studentDAO = new StudentDAO();
        this.bookDAO = new BookDAO();
        this.bookCopyDAO = new BookCopyDAO();
        this.loanDAO = new LoanDAO();
    }
    
    // Student Management - 1, 2, 3, 4
    public void createStudent(Student student) {
        studentDAO.saveStudent(student);
    }
    
    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }
    
    public void deleteStudent(String broncoId) {
        studentDAO.deleteStudent(broncoId);
    }
    
    public Student getStudent(String broncoId) {
        return studentDAO.getStudent(broncoId);
    }
    
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
    
    public List<Student> searchStudents(String searchTerm) {
        return studentDAO.searchStudents(searchTerm);
    }
    
    // Book Management - 5
    public void createBook(Book book) {
        bookDAO.saveBook(book);
    }
    
    public void updateBook(Book book) {
        bookDAO.updateBook(book);
    }
    
    public void deleteBook(String isbn) {
        bookDAO.deleteBook(isbn);
    }
    
    public Book getBook(String isbn) {
        return bookDAO.getBook(isbn);
    }
    
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    public List<Book> searchBooks(String searchTerm) {
        return bookDAO.searchBooks(searchTerm);
    }
    
    // Book Copy Management - 6, 8
    public void createBookCopy(BookCopy bookCopy) {
        bookCopyDAO.saveBookCopy(bookCopy);
    }
    
    public void updateBookCopy(BookCopy bookCopy) {
        bookCopyDAO.updateBookCopy(bookCopy);
    }
    
    public void deleteBookCopy(String barcode) {
        bookCopyDAO.deleteBookCopy(barcode);
    }
    
    public BookCopy getBookCopy(String barcode) {
        return bookCopyDAO.getBookCopy(barcode);
    }
    
    public List<BookCopy> getAllBookCopies() {
        return bookCopyDAO.getAllBookCopies();
    }
    
    public List<BookCopy> getBookCopiesByISBN(String isbn) {
        return bookCopyDAO.getBookCopiesByISBN(isbn);
    }
    
    public List<BookCopy> getAvailableBookCopies(String isbn) {
        return bookCopyDAO.getAvailableBookCopies(isbn);
    }
    
    // Loan Management 
    
    // 10: Enforce 5-book limit per student
    // 11: Enforce 180-day maximum loan duration  
    // 12: Prevent loans to students with overdue items
    public boolean canCreateLoan(String broncoId, Set<BookCopy> bookCopies) {
        Student student = studentDAO.getStudent(broncoId);
        if (student == null) {
            throw new IllegalArgumentException("Student with Bronco ID " + broncoId + " not found");
        }
        
        // Check for overdue loans - 12
        List<Loan> overdueLoans = loanDAO.getOverdueLoans();
        for (Loan loan : overdueLoans) {
            if (loan.getStudent().getBroncoId().equals(broncoId)) {
                throw new IllegalArgumentException("Cannot create loan: Student has overdue items. Please return overdue books first.");
            }
        }
        
        // Check 5-book limit - 10
        List<Loan> activeLoans = loanDAO.getActiveLoans();
        int currentBorrowedCount = 0;
        for (Loan loan : activeLoans) {
            if (loan.getStudent().getBroncoId().equals(broncoId)) {
                currentBorrowedCount += loan.getBookCopies().size();
            }
        }
        
        if ((currentBorrowedCount + bookCopies.size()) > 5) {
            throw new IllegalArgumentException("Cannot create loan: Student would exceed the 5-book limit. Currently borrowed: " + currentBorrowedCount + ", attempting to borrow: " + bookCopies.size());
        }
        
        return true;
    }
    
    // 9: Create loan records 
    // 11: Enforce 180-day maximum
    public Loan createLoan(String broncoId, Set<BookCopy> bookCopies, int loanDays) {
        // Enforce 180-day maximum - FR-11
        if (loanDays > 180) {
            throw new IllegalArgumentException("Loan duration cannot exceed 180 days");
        }
        
        if (loanDays <= 0) {
            throw new IllegalArgumentException("Loan duration must be at least 1 day");
        }
        
        // Validate loan can be created (checks overdue and 5-book limit)
        canCreateLoan(broncoId, bookCopies);
        
        Student student = studentDAO.getStudent(broncoId);
        LocalDate borrowingDate = LocalDate.now();
        LocalDate dueDate = borrowingDate.plusDays(loanDays);
        
        Loan loan = new Loan(borrowingDate, dueDate, student);
        loan.setBookCopies(bookCopies);
        
        // Update book copy status to borrowed
        for (BookCopy bookCopy : bookCopies) {
            bookCopy.setStatus(BookStatus.BORROWED);
            bookCopyDAO.updateBookCopy(bookCopy);
        }
        
        loanDAO.saveLoan(loan);
        return loan;
    }
    
    // 13: Process loan returns - 16: All items must be returned together
    public void returnLoan(Long loanNumber) {
        Loan loan = loanDAO.getLoan(loanNumber);
        if (loan == null) {
            throw new IllegalArgumentException("Loan with number " + loanNumber + " not found");
        }
        
        if (loan.getReturnDate() != null) {
            throw new IllegalArgumentException("Loan has already been returned");
        }
        
        // Set return date
        loan.setReturnDate(LocalDate.now());
        
        // Update all book copy status to available - 16: All items returned together
        for (BookCopy bookCopy : loan.getBookCopies()) {
            bookCopy.setStatus(BookStatus.AVAILABLE);
            bookCopyDAO.updateBookCopy(bookCopy);
        }
        
        loanDAO.updateLoan(loan);
    }
    
    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }
    
    public List<Loan> getActiveLoans() {
        return loanDAO.getActiveLoans();
    }
    
    public List<Loan> getLoansByStudent(String broncoId) {
        return loanDAO.getLoansByStudent(broncoId);
    }
    
    public List<Loan> getOverdueLoans() {
        return loanDAO.getOverdueLoans();
    }
    
    // 15: Generate consolidated reports filtered by student and/or period
    public List<Loan> getLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        return loanDAO.getLoansByDateRange(startDate, endDate);
    }
    
    // Additional helper methods 
    public int getCurrentBorrowedCount(String broncoId) {
        List<Loan> activeLoans = getActiveLoans();
        int count = 0;
        for (Loan loan : activeLoans) {
            if (loan.getStudent().getBroncoId().equals(broncoId)) {
                count += loan.getBookCopies().size();
            }
        }
        return count;
    }
    
    public boolean hasOverdueLoans(String broncoId) {
        List<Loan> overdueLoans = getOverdueLoans();
        for (Loan loan : overdueLoans) {
            if (loan.getStudent().getBroncoId().equals(broncoId)) {
                return true;
            }
        }
        return false;
    }
}