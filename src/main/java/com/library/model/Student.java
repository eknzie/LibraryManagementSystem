package com.library.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "bronco_id")
    private String broncoId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "degree")
    private String degree;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Loan> loans = new HashSet<>();
    
    public Student() {}
    
    public Student(String broncoId, String name, String address, String degree) {
        this.broncoId = broncoId;
        this.name = name;
        this.address = address;
        this.degree = degree;
    }
    
    // Getters and Setters
    public String getBroncoId() { return broncoId; }
    public void setBroncoId(String broncoId) { this.broncoId = broncoId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    
    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }
}