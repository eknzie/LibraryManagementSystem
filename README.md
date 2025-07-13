# Library Management System

A Java desktop application for managing library operations including students, books, and loan transactions.

## Requirements

- Java 11 or higher
- Maven 3.6+
- PostgreSQL 12+

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE library_management;
```

2. Update database credentials in `src/main/resources/hibernate.cfg.xml`:
```xml
<property name="connection.driver_class">org.postgresql.Driver</property>
<property name="connection.url">jdbc:postgresql://localhost:5432/library_management</property>
<property name="connection.username">your_username</property>
<property name="connection.password">your_password</property>
<property name="dialect">org.hibernate.dialect.PostgreSQLDialect</property>
```

## How to Run

1. Clone the repository:
```bash
git clone <repository-url>
cd library-management-system
```

2. Compile and run:
```bash
mvn clean compile
mvn javafx:run
```

## Features

### Student Management
- Add, update, delete students
- Search students by name or Bronco ID
- View active loan count for each student

### Book Management
- Add, update, delete books with ISBN, title, authors, publisher details
- Search books by title, author, or ISBN
- View book availability and total copies

### Book Copy Management
- Manage individual copies of books with unique barcodes
- Track physical location and status (Available/Borrowed)
- View due dates for borrowed copies

### Loan Management
- Create loans for students with multiple books
- Set loan duration (maximum 180 days)
- Return loans and update book status
- Check for overdue loans

### Reporting
- View all loans with filtering options
- Generate reports by student or date range
- Display loan status (Active, Returned, Overdue)

## Constraints

- Students can borrow maximum 5 books at a time
- Maximum loan duration is 180 days
- Students with overdue books cannot borrow new items
- All books in a loan must be returned together

## Project Structure

```
src/
├── main/java/com/library/
│   ├── dao/           # Database access layer
│   ├── model/         # Entity classes
│   ├── service/       # Business logic
│   ├── ui/            # JavaFX user interface
│   └── util/          # Hibernate configuration
└── main/resources/
    └── hibernate.cfg.xml
```

The application uses Hibernate to automatically create database tables on first run.