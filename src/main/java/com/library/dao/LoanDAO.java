package com.library.dao;

import com.library.model.Loan;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.List;

public class LoanDAO {
    
    public void saveLoan(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public void updateLoan(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public void deleteLoan(Long loanNumber) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Loan loan = session.get(Loan.class, loanNumber);
            if (loan != null) {
                session.delete(loan);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public Loan getLoan(Long loanNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Eagerly fetch student and book copies with their books
            Query<Loan> query = session.createQuery(
                "FROM Loan l " +
                "JOIN FETCH l.student " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book " +
                "WHERE l.loanNumber = :loanNumber", 
                Loan.class);
            query.setParameter("loanNumber", loanNumber);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Loan> getAllLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Eagerly fetch all related entities
            Query<Loan> query = session.createQuery(
                "SELECT DISTINCT l FROM Loan l " +
                "JOIN FETCH l.student " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book", 
                Loan.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<Loan> getActiveLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "SELECT DISTINCT l FROM Loan l " +
                "JOIN FETCH l.student " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book " +
                "WHERE l.returnDate IS NULL", 
                Loan.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<Loan> getLoansByStudent(String broncoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "SELECT DISTINCT l FROM Loan l " +
                "JOIN FETCH l.student s " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book " +
                "WHERE s.broncoId = :broncoId", 
                Loan.class);
            query.setParameter("broncoId", broncoId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<Loan> getOverdueLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "SELECT DISTINCT l FROM Loan l " +
                "JOIN FETCH l.student " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book " +
                "WHERE l.returnDate IS NULL AND l.dueDate < :currentDate", 
                Loan.class);
            query.setParameter("currentDate", LocalDate.now());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<Loan> getLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "SELECT DISTINCT l FROM Loan l " +
                "JOIN FETCH l.student " +
                "LEFT JOIN FETCH l.bookCopies bc " +
                "LEFT JOIN FETCH bc.book " +
                "WHERE l.borrowingDate BETWEEN :startDate AND :endDate", 
                Loan.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}