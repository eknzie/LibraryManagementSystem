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
            return session.get(Loan.class, loanNumber);
        }
    }
    
    public List<Loan> getAllLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Loan", Loan.class).list();
        }
    }
    
    public List<Loan> getActiveLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "from Loan l where l.returnDate is null", Loan.class);
            return query.list();
        }
    }
    
    public List<Loan> getLoansByStudent(String broncoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "from Loan l where l.student.broncoId = :broncoId", Loan.class);
            query.setParameter("broncoId", broncoId);
            return query.list();
        }
    }
    
    public List<Loan> getOverdueLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "from Loan l where l.returnDate is null and l.dueDate < :currentDate", 
                Loan.class);
            query.setParameter("currentDate", LocalDate.now());
            return query.list();
        }
    }
    
    public List<Loan> getLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                "from Loan l where l.borrowingDate between :startDate and :endDate", 
                Loan.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        }
    }
}