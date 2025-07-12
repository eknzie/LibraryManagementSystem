package com.library.dao;

import com.library.model.BookCopy;
import com.library.model.BookStatus;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class BookCopyDAO {
    
    public void saveBookCopy(BookCopy bookCopy) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookCopy);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public void updateBookCopy(BookCopy bookCopy) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(bookCopy);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public void deleteBookCopy(String barcode) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            BookCopy bookCopy = session.get(BookCopy.class, barcode);
            if (bookCopy != null) {
                session.delete(bookCopy);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    public BookCopy getBookCopy(String barcode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BookCopy.class, barcode);
        }
    }
    
    public List<BookCopy> getAllBookCopies() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from BookCopy", BookCopy.class).list();
        }
    }
    
    public List<BookCopy> getBookCopiesByISBN(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BookCopy> query = session.createQuery(
                "from BookCopy bc where bc.book.isbn = :isbn", BookCopy.class);
            query.setParameter("isbn", isbn);
            return query.list();
        }
    }
    
    public List<BookCopy> getAvailableBookCopies(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BookCopy> query = session.createQuery(
                "from BookCopy bc where bc.book.isbn = :isbn and bc.status = :status", 
                BookCopy.class);
            query.setParameter("isbn", isbn);
            query.setParameter("status", BookStatus.AVAILABLE);
            return query.list();
        }
    }
}