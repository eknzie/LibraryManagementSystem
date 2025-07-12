package com.library.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "authors")
    private String authors;
    
    @Column(name = "number_of_pages")
    private Integer numberOfPages;
    
    @Column(name = "publisher")
    private String publisher;
    
    @Column(name = "publication_date")
    private LocalDate publicationDate;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BookCopy> bookCopies = new HashSet<>();
    
    public Book() {}
    
    public Book(String isbn, String title, String description, String authors, 
                Integer numberOfPages, String publisher, LocalDate publicationDate) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.numberOfPages = numberOfPages;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
    }
    
    // Getters and Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
    
    public Integer getNumberOfPages() { return numberOfPages; }
    public void setNumberOfPages(Integer numberOfPages) { this.numberOfPages = numberOfPages; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    
    public Set<BookCopy> getBookCopies() { return bookCopies; }
    public void setBookCopies(Set<BookCopy> bookCopies) { this.bookCopies = bookCopies; }
}