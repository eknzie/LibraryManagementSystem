package com.library.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @Column(name = "barcode")
    private String barcode;
    
    @Column(name = "physical_location")
    private String physicalLocation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn", nullable = false)
    private Book book;
    
    @ManyToMany(mappedBy = "bookCopies")
    private Set<Loan> loans = new HashSet<>();
    
    public BookCopy() {}
    
    public BookCopy(String barcode, String physicalLocation, BookStatus status, Book book) {
        this.barcode = barcode;
        this.physicalLocation = physicalLocation;
        this.status = status;
        this.book = book;
    }
    
    // Getters and Setters
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    
    public String getPhysicalLocation() { return physicalLocation; }
    public void setPhysicalLocation(String physicalLocation) { this.physicalLocation = physicalLocation; }
    
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }
    
    @Override
    public String toString() {
        return barcode + " - " + (book != null ? book.getTitle() : "No Book") + " (" + status + ")";
    }
}
