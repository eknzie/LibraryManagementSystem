-- ===============================================
-- Library Management System - Sample Data
-- 20 rows per table for demonstration purposes
-- ===============================================

-- Clear existing data 
DELETE FROM loan_book_copies;
DELETE FROM loans;
DELETE FROM book_copies;
DELETE FROM books;
DELETE FROM students;

-- ===============================================
-- STUDENTS TABLE - 20 Sample Students
-- ===============================================

INSERT INTO students (bronco_id, name, address, degree) VALUES
('123456789', 'John Doe', '123 Main St, Pomona, CA 91768', 'Computer Science'),
('987654321', 'Jane Smith', '456 Oak Ave, Pomona, CA 91768', 'Engineering'),
('555666777', 'Bob Johnson', '789 Pine St, Pomona, CA 91768', 'Mathematics'),
('111222333', 'Alice Brown', '321 Elm Dr, Pomona, CA 91768', 'Business Administration'),
('444555666', 'Charlie Wilson', '654 Maple Rd, Pomona, CA 91768', 'Psychology'),
('777888999', 'Diana Chen', '987 Cedar Ln, Pomona, CA 91768', 'Biology'),
('222333444', 'Ethan Davis', '147 Birch St, Pomona, CA 91768', 'Physics'),
('888999000', 'Fiona Garcia', '258 Spruce Ave, Pomona, CA 91768', 'Chemistry'),
('333444555', 'George Lee', '369 Walnut Blvd, Pomona, CA 91768', 'English Literature'),
('666777888', 'Hannah Martinez', '741 Sycamore Dr, Pomona, CA 91768', 'Art History'),
('101010101', 'Ian Rodriguez', '852 Poplar St, Pomona, CA 91768', 'Mechanical Engineering'),
('202020202', 'Julia Kim', '963 Hickory Ave, Pomona, CA 91768', 'Electrical Engineering'),
('303030303', 'Kevin Wong', '159 Redwood Ln, Pomona, CA 91768', 'Computer Engineering'),
('404040404', 'Laura Thompson', '267 Magnolia Rd, Pomona, CA 91768', 'Civil Engineering'),
('505050505', 'Michael Anderson', '384 Willow Dr, Pomona, CA 91768', 'Environmental Science'),
('606060606', 'Nicole Taylor', '495 Cypress St, Pomona, CA 91768', 'Graphic Design'),
('707070707', 'Oliver Jackson', '516 Juniper Ave, Pomona, CA 91768', 'Music'),
('808080808', 'Priya Patel', '627 Aspen Blvd, Pomona, CA 91768', 'Accounting'),
('909090909', 'Quinn Murphy', '738 Dogwood Dr, Pomona, CA 91768', 'Marketing'),
('121212121', 'Rachel Green', '849 Chestnut St, Pomona, CA 91768', 'Finance');

-- ===============================================
-- BOOKS TABLE - 20 Sample Books
-- ===============================================

INSERT INTO books (isbn, title, description, authors, number_of_pages, publisher, publication_date) VALUES
('978-0134685991', 'Effective Java', 'Best practices for the Java platform covering Java 7, 8, and 9', 'Joshua Bloch', 416, 'Addison-Wesley Professional', '2017-12-27'),
('978-0135166307', 'Clean Code', 'A handbook of agile software craftsmanship', 'Robert C. Martin', 464, 'Prentice Hall', '2008-08-01'),
('978-0596009205', 'Head First Design Patterns', 'A brain-friendly guide to design patterns', 'Eric Freeman, Elisabeth Robson', 694, 'O''Reilly Media', '2004-10-25'),
('978-0321125217', 'Domain-Driven Design', 'Tackling complexity in the heart of software', 'Eric Evans', 560, 'Addison-Wesley Professional', '2003-08-22'),
('978-0134494166', 'Clean Architecture', 'A craftsman''s guide to software structure and design', 'Robert C. Martin', 432, 'Prentice Hall', '2017-09-12'),
('978-0201633610', 'Design Patterns', 'Elements of reusable object-oriented software', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', 395, 'Addison-Wesley Professional', '1994-10-21'),
('978-0132350884', 'Clean Code', 'A handbook of agile software craftsmanship', 'Robert C. Martin', 464, 'Prentice Hall', '2008-08-01'),
('978-0321934956', 'Spring in Action', 'Covers Spring 4', 'Craig Walls', 624, 'Manning Publications', '2014-11-28'),
('978-1617291203', 'Java 8 in Action', 'Lambdas, streams, and functional-style programming', 'Raoul-Gabriel Urma, Mario Fusco, Alan Mycroft', 424, 'Manning Publications', '2014-08-28'),
('978-0134052311', 'Hibernate in Action', 'Practical object/relational mapping', 'Christian Bauer, Gavin King', 400, 'Manning Publications', '2004-08-01'),
('978-0596007126', 'Head First SQL', 'Your brain on SQL -- a learner''s guide', 'Lynn Beighley', 608, 'O''Reilly Media', '2007-08-27'),
('978-0321356680', 'Effective C++', '55 specific ways to improve your programs and designs', 'Scott Meyers', 320, 'Addison-Wesley Professional', '2005-05-12'),
('978-0136083238', 'Introduction to Algorithms', 'Comprehensive introduction to algorithms', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein', 1312, 'MIT Press', '2009-07-31'),
('978-0201634983', 'Structure and Interpretation of Computer Programs', 'Building abstractions with procedures, data, and objects', 'Harold Abelson, Gerald Jay Sussman', 657, 'MIT Press', '1996-07-25'),
('978-0132126041', 'Patterns of Enterprise Application Architecture', 'Enterprise application architecture patterns', 'Martin Fowler', 560, 'Addison-Wesley Professional', '2002-11-05'),
('978-0321127426', 'Refactoring', 'Improving the design of existing code', 'Martin Fowler', 431, 'Addison-Wesley Professional', '1999-07-08'),
('978-0596516178', 'JavaScript: The Good Parts', 'Unearthing the excellence in JavaScript', 'Douglas Crockford', 176, 'O''Reilly Media', '2008-05-01'),
('978-0134494203', 'The Clean Coder', 'A code of conduct for professional programmers', 'Robert C. Martin', 256, 'Prentice Hall', '2011-05-23'),
('978-0321534408', 'Test Driven Development', 'By example', 'Kent Beck', 240, 'Addison-Wesley Professional', '2002-11-08'),
('978-0201657883', 'Code Complete', 'A practical handbook of software construction', 'Steve McConnell', 960, 'Microsoft Press', '2004-06-09');

-- ===============================================
-- BOOK_COPIES TABLE - 60 Sample Book Copies (3 per book)
-- ===============================================

INSERT INTO book_copies (barcode, physical_location, status, isbn) VALUES
-- Effective Java (3 copies)
('BC001001', 'Section A - Shelf 1 - Position 1', 'AVAILABLE', '978-0134685991'),
('BC001002', 'Section A - Shelf 1 - Position 2', 'AVAILABLE', '978-0134685991'),
('BC001003', 'Section A - Shelf 1 - Position 3', 'BORROWED', '978-0134685991'),

-- Clean Code (3 copies)
('BC002001', 'Section A - Shelf 2 - Position 1', 'AVAILABLE', '978-0135166307'),
('BC002002', 'Section A - Shelf 2 - Position 2', 'AVAILABLE', '978-0135166307'),
('BC002003', 'Section A - Shelf 2 - Position 3', 'AVAILABLE', '978-0135166307'),

-- Head First Design Patterns (3 copies)
('BC003001', 'Section A - Shelf 3 - Position 1', 'AVAILABLE', '978-0596009205'),
('BC003002', 'Section A - Shelf 3 - Position 2', 'BORROWED', '978-0596009205'),
('BC003003', 'Section A - Shelf 3 - Position 3', 'AVAILABLE', '978-0596009205'),

-- Domain-Driven Design (3 copies)
('BC004001', 'Section B - Shelf 1 - Position 1', 'AVAILABLE', '978-0321125217'),
('BC004002', 'Section B - Shelf 1 - Position 2', 'AVAILABLE', '978-0321125217'),
('BC004003', 'Section B - Shelf 1 - Position 3', 'AVAILABLE', '978-0321125217'),

-- Clean Architecture (3 copies)
('BC005001', 'Section B - Shelf 2 - Position 1', 'BORROWED', '978-0134494166'),
('BC005002', 'Section B - Shelf 2 - Position 2', 'AVAILABLE', '978-0134494166'),
('BC005003', 'Section B - Shelf 2 - Position 3', 'AVAILABLE', '978-0134494166'),

-- Design Patterns (3 copies)
('BC006001', 'Section B - Shelf 3 - Position 1', 'AVAILABLE', '978-0201633610'),
('BC006002', 'Section B - Shelf 3 - Position 2', 'AVAILABLE', '978-0201633610'),
('BC006003', 'Section B - Shelf 3 - Position 3', 'BORROWED', '978-0201633610'),

-- Clean Code (Second Edition) (3 copies)
('BC007001', 'Section C - Shelf 1 - Position 1', 'AVAILABLE', '978-0132350884'),
('BC007002', 'Section C - Shelf 1 - Position 2', 'AVAILABLE', '978-0132350884'),
('BC007003', 'Section C - Shelf 1 - Position 3', 'AVAILABLE', '978-0132350884'),

-- Spring in Action (3 copies)
('BC008001', 'Section C - Shelf 2 - Position 1', 'AVAILABLE', '978-0321934956'),
('BC008002', 'Section C - Shelf 2 - Position 2', 'BORROWED', '978-0321934956'),
('BC008003', 'Section C - Shelf 2 - Position 3', 'AVAILABLE', '978-0321934956'),

-- Java 8 in Action (3 copies)
('BC009001', 'Section C - Shelf 3 - Position 1', 'AVAILABLE', '978-1617291203'),
('BC009002', 'Section C - Shelf 3 - Position 2', 'AVAILABLE', '978-1617291203'),
('BC009003', 'Section C - Shelf 3 - Position 3', 'AVAILABLE', '978-1617291203'),

-- Hibernate in Action (3 copies)
('BC010001', 'Section D - Shelf 1 - Position 1', 'BORROWED', '978-0134052311'),
('BC010002', 'Section D - Shelf 1 - Position 2', 'AVAILABLE', '978-0134052311'),
('BC010003', 'Section D - Shelf 1 - Position 3', 'AVAILABLE', '978-0134052311'),

-- Head First SQL (3 copies)
('BC011001', 'Section D - Shelf 2 - Position 1', 'AVAILABLE', '978-0596007126'),
('BC011002', 'Section D - Shelf 2 - Position 2', 'AVAILABLE', '978-0596007126'),
('BC011003', 'Section D - Shelf 2 - Position 3', 'BORROWED', '978-0596007126'),

-- Effective C++ (3 copies)
('BC012001', 'Section D - Shelf 3 - Position 1', 'AVAILABLE', '978-0321356680'),
('BC012002', 'Section D - Shelf 3 - Position 2', 'AVAILABLE', '978-0321356680'),
('BC012003', 'Section D - Shelf 3 - Position 3', 'AVAILABLE', '978-0321356680'),

-- Introduction to Algorithms (3 copies)
('BC013001', 'Section E - Shelf 1 - Position 1', 'BORROWED', '978-0136083238'),
('BC013002', 'Section E - Shelf 1 - Position 2', 'AVAILABLE', '978-0136083238'),
('BC013003', 'Section E - Shelf 1 - Position 3', 'AVAILABLE', '978-0136083238'),

-- Structure and Interpretation of Computer Programs (3 copies)
('BC014001', 'Section E - Shelf 2 - Position 1', 'AVAILABLE', '978-0201634983'),
('BC014002', 'Section E - Shelf 2 - Position 2', 'AVAILABLE', '978-0201634983'),
('BC014003', 'Section E - Shelf 2 - Position 3', 'AVAILABLE', '978-0201634983'),

-- Patterns of Enterprise Application Architecture (3 copies)
('BC015001', 'Section E - Shelf 3 - Position 1', 'AVAILABLE', '978-0132126041'),
('BC015002', 'Section E - Shelf 3 - Position 2', 'BORROWED', '978-0132126041'),
('BC015003', 'Section E - Shelf 3 - Position 3', 'AVAILABLE', '978-0132126041'),

-- Refactoring (3 copies)
('BC016001', 'Section F - Shelf 1 - Position 1', 'AVAILABLE', '978-0321127426'),
('BC016002', 'Section F - Shelf 1 - Position 2', 'AVAILABLE', '978-0321127426'),
('BC016003', 'Section F - Shelf 1 - Position 3', 'AVAILABLE', '978-0321127426'),

-- JavaScript: The Good Parts (3 copies)
('BC017001', 'Section F - Shelf 2 - Position 1', 'BORROWED', '978-0596516178'),
('BC017002', 'Section F - Shelf 2 - Position 2', 'AVAILABLE', '978-0596516178'),
('BC017003', 'Section F - Shelf 2 - Position 3', 'AVAILABLE', '978-0596516178'),

-- The Clean Coder (3 copies)
('BC018001', 'Section F - Shelf 3 - Position 1', 'AVAILABLE', '978-0134494203'),
('BC018002', 'Section F - Shelf 3 - Position 2', 'AVAILABLE', '978-0134494203'),
('BC018003', 'Section F - Shelf 3 - Position 3', 'BORROWED', '978-0134494203'),

-- Test Driven Development (3 copies)
('BC019001', 'Section G - Shelf 1 - Position 1', 'AVAILABLE', '978-0321534408'),
('BC019002', 'Section G - Shelf 1 - Position 2', 'AVAILABLE', '978-0321534408'),
('BC019003', 'Section G - Shelf 1 - Position 3', 'AVAILABLE', '978-0321534408'),

-- Code Complete (3 copies)
('BC020001', 'Section G - Shelf 2 - Position 1', 'BORROWED', '978-0201657883'),
('BC020002', 'Section G - Shelf 2 - Position 2', 'AVAILABLE', '978-0201657883'),
('BC020003', 'Section G - Shelf 2 - Position 3', 'AVAILABLE', '978-0201657883');

-- ===============================================
-- LOANS TABLE - 20 Sample Loans
-- ===============================================

INSERT INTO loans (borrowing_date, due_date, return_date, bronco_id) VALUES
-- Active loans (no return date)
('2025-06-15', '2025-07-29', NULL, '123456789'),          -- Loan 1: John Doe
('2025-06-20', '2025-08-03', NULL, '987654321'),          -- Loan 2: Jane Smith  
('2025-06-25', '2025-08-08', NULL, '555666777'),          -- Loan 3: Bob Johnson
('2025-07-01', '2025-08-14', NULL, '111222333'),          -- Loan 4: Alice Brown
('2025-07-05', '2025-08-18', NULL, '444555666'),          -- Loan 5: Charlie Wilson
('2025-07-08', '2025-08-21', NULL, '777888999'),          -- Loan 6: Diana Chen
('2025-07-10', '2025-08-23', NULL, '222333444'),          -- Loan 7: Ethan Davis
('2025-07-12', '2025-08-25', NULL, '888999000'),          -- Loan 8: Fiona Garcia

-- Completed loans (with return dates)
('2025-05-01', '2025-06-14', '2025-06-10', '333444555'),  -- Loan 9: George Lee (returned early)
('2025-05-05', '2025-06-18', '2025-06-18', '666777888'),  -- Loan 10: Hannah Martinez (returned on time)
('2025-05-10', '2025-06-23', '2025-06-20', '101010101'),  -- Loan 11: Ian Rodriguez (returned early)
('2025-05-15', '2025-06-28', '2025-06-28', '202020202'),  -- Loan 12: Julia Kim (returned on time)
('2025-05-20', '2025-07-03', '2025-07-01', '303030303'),  -- Loan 13: Kevin Wong (returned early)
('2025-05-25', '2025-07-08', '2025-07-08', '404040404'),  -- Loan 14: Laura Thompson (returned on time)
('2025-05-30', '2025-07-13', '2025-07-10', '505050505'),  -- Loan 15: Michael Anderson (returned early)

-- Overdue loans (past due date, not returned)
('2025-05-01', '2025-06-14', NULL, '606060606'),          -- Loan 16: Nicole Taylor (OVERDUE)
('2025-05-10', '2025-06-23', NULL, '707070707'),          -- Loan 17: Oliver Jackson (OVERDUE)

-- Recent completed loans
('2025-06-01', '2025-07-15', '2025-07-12', '808080808'),  -- Loan 18: Priya Patel (returned early)
('2025-06-05', '2025-07-19', '2025-07-19', '909090909'),  -- Loan 19: Quinn Murphy (returned on time)
('2025-06-10', '2025-07-24', '2025-07-20', '121212121');  -- Loan 20: Rachel Green (returned early)

-- ===============================================
-- LOAN_BOOK_COPIES TABLE - Junction Table Entries
-- ===============================================

-- Note: loan_number will be auto-generated, so we use the sequence values
-- Assuming loans are numbered 1-20 based on insertion order

INSERT INTO loan_book_copies (loan_number, barcode) VALUES
-- Loan 1 (John Doe) - 2 books
(1, 'BC001003'),  -- Effective Java
(1, 'BC003002'),  -- Head First Design Patterns

-- Loan 2 (Jane Smith) - 1 book
(2, 'BC005001'),  -- Clean Architecture

-- Loan 3 (Bob Johnson) - 3 books  
(3, 'BC006003'),  -- Design Patterns
(3, 'BC008002'),  -- Spring in Action
(3, 'BC010001'),  -- Hibernate in Action

-- Loan 4 (Alice Brown) - 1 book
(4, 'BC011003'),  -- Head First SQL

-- Loan 5 (Charlie Wilson) - 2 books
(5, 'BC013001'),  -- Introduction to Algorithms
(5, 'BC015002'),  -- Patterns of Enterprise Application Architecture

-- Loan 6 (Diana Chen) - 1 book
(6, 'BC017001'),  -- JavaScript: The Good Parts

-- Loan 7 (Ethan Davis) - 2 books
(7, 'BC018003'),  -- The Clean Coder
(7, 'BC020001'),  -- Code Complete

-- Loan 8 (Fiona Garcia) - 1 book
(8, 'BC012001'),  -- Effective C++ (assuming this was borrowed)

-- Completed loans (9-15) - various combinations
(9, 'BC007001'),   -- Clean Code (Second Edition)
(10, 'BC009001'),  -- Java 8 in Action
(11, 'BC014001'),  -- Structure and Interpretation of Computer Programs
(11, 'BC016001'),  -- Refactoring
(12, 'BC019001'),  -- Test Driven Development
(13, 'BC002001'),  -- Clean Code
(13, 'BC004001'),  -- Domain-Driven Design
(14, 'BC011001'),  -- Head First SQL
(15, 'BC015001'),  -- Patterns of Enterprise Application Architecture

-- Overdue loans (16-17)
(16, 'BC003001'),  -- Head First Design Patterns (OVERDUE)
(16, 'BC005002'),  -- Clean Architecture (OVERDUE)
(17, 'BC013002'),  -- Introduction to Algorithms (OVERDUE)

-- Recent completed loans (18-20)
(18, 'BC017002'),  -- JavaScript: The Good Parts
(19, 'BC018002'),  -- The Clean Coder
(20, 'BC020002');  -- Code Complete

-- ===============================================
-- VERIFICATION QUERIES
-- ===============================================

-- Check record counts
/*
SELECT 'Students' as table_name, COUNT(*) as record_count FROM students
UNION ALL
SELECT 'Books', COUNT(*) FROM books  
UNION ALL
SELECT 'Book Copies', COUNT(*) FROM book_copies
UNION ALL  
SELECT 'Loans', COUNT(*) FROM loans
UNION ALL
SELECT 'Loan Book Copies', COUNT(*) FROM loan_book_copies;
*/

/*
-- Check active loans
SELECT l.loan_number, s.name, l.borrowing_date, l.due_date
FROM loans l 
JOIN students s ON l.bronco_id = s.bronco_id 
WHERE l.return_date IS NULL
ORDER BY l.loan_numbe
Check overdue loans  
SELECT l.loan_number, s.name, l.due_date, 
       CURRENT_DATE - l.due_date AS days_overdue
FROM loans l
JOIN students s ON l.bronco_id = s.bronco_id
WHERE l.return_date IS NULL AND l.due_date < CURRENT_DATE
ORDER BY days_overdue DESC;
*/