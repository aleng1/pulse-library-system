package com.pulse.repository;

import com.pulse.model.Book;
import com.pulse.model.BookCategory;
import com.pulse.model.BookLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    Book findByIsbn(String isbn);
    Book findByTitle(String title);
    
    // Search methods
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategory(BookCategory category);
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategory(
        String title, String author, BookCategory category);
        
    // Location methods
    List<Book> findByLocation(BookLocation location);
} 