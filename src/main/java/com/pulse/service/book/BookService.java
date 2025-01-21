package com.pulse.service.book;

import com.pulse.model.Book;
import com.pulse.model.BookLocation;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Long id);
    Book getBookByIsbn(String isbn);
    Book updateBookStatus(Long id, String status);
    Book updateBookCopies(Long id, int copies);
    List<Book> findBooksByCategory(String category);
    boolean isBookAvailable(Long id);
    
    // Search methods
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooks(String query);
    
    // Location tracking methods
    Book updateBookLocation(Long id, BookLocation location);
    List<Book> findBooksByLocation(BookLocation location);
} 