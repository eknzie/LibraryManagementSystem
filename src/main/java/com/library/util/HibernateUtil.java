package com.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.library.model.*;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Add annotated classes
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Book.class);
            configuration.addAnnotatedClass(BookCopy.class);
            configuration.addAnnotatedClass(Loan.class);
            
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}