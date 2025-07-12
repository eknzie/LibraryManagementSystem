package com.library.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_number")
    private Long loanNumber;
    
    @Column(name = "borrowing_date", nullable = false)
    private LocalDate borrowingDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bronco_id", nullable = false)
    private Student student;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "loan_book_copies",
        joinColumns = @JoinColumn(name = "loan_number"),
        inverseJoinColumns = @JoinColumn(name = "barcode")
    )
    private Set<BookCopy> bookCopies = new HashSet<>();
    
    public Loan() {}
    
    public Loan(LocalDate borrowingDate, LocalDate dueDate, Student student) {
        this.borrowingDate = borrowingDate;
        this.dueDate = dueDate;
        this.student = student;
    }
    
    // Getters and Setters
    public Long getLoanNumber() { return loanNumber; }
    public void setLoanNumber(Long loanNumber) { this.loanNumber = loanNumber; }
    
    public LocalDate getBorrowingDate() { return borrowingDate; }
    public void setBorrowingDate(LocalDate borrowingDate) { this.borrowingDate = borrowingDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Set<BookCopy> getBookCopies() { return bookCopies; }
    public void setBookCopies(Set<BookCopy> bookCopies) { this.bookCopies = bookCopies; }
    
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
}