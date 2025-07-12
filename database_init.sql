-- Connect to PostgreSQL and create database
-- Run this in psql or pgAdmin

CREATE DATABASE library_db;

-- Connect to the new database
\c library_db;

-- Tables will be created automatically by Hibernate
-- But you can insert sample data after first run

-- Sample data (run after first application start)
INSERT INTO students (bronco_id, name, address, degree) VALUES
('123456789', 'John Doe', '123 Main St, Pomona, CA', 'Computer Science'),
('987654321', 'Jane Smith', '456 Oak Ave, Pomona, CA', 'Engineering'),
('555666777', 'Bob Johnson', '789 Pine St, Pomona, CA', 'Mathematics')
ON CONFLICT DO NOTHING;

INSERT INTO books (isbn, title, description, authors, number_of_pages, publisher, publication_date) VALUES
('978-0134685991', 'Effective Java', 'Best practices for Java programming', 'Joshua Bloch', 416, 'Addison-Wesley', '2017-12-27'),
('978-0135166307', 'Clean Code', 'A handbook of agile software craftsmanship', 'Robert C. Martin', 464, 'Prentice Hall', '2008-08-01'),
('978-0596009205', 'Head First Design Patterns', 'A brain-friendly guide to design patterns', 'Eric Freeman, Elisabeth Robson', 694, 'O''Reilly Media', '2004-10-25')
ON CONFLICT DO NOTHING;

INSERT INTO book_copies (barcode, physical_location, status, isbn) VALUES
('BC001', 'A1-001', 'AVAILABLE', '978-0134685991'),
('BC002', 'A1-002', 'AVAILABLE', '978-0134685991'),
('BC003', 'A2-001', 'AVAILABLE', '978-0135166307'),
('BC004', 'A2-002', 'AVAILABLE', '978-0135166307'),
('BC005', 'A3-001', 'AVAILABLE', '978-0596009205')
ON CONFLICT DO NOTHING;
