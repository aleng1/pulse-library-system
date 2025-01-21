package com.pulse.service.book;

import com.pulse.model.Book;
import com.pulse.model.BookCategory;
import com.pulse.model.BookLocation;
import com.pulse.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book createBook(Book book) {
        book.setAvailableCopies(book.getCopies());
        book.setStatus("AVAILABLE");
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book not found with id: " + book.getId());
        }
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBookStatus(Long id, String status) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setStatus(status);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBookCopies(Long id, int copies) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setCopies(copies);
        book.setAvailableCopies(copies);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByCategory(String category) {
        try {
            BookCategory bookCategory = BookCategory.valueOf(category.toUpperCase());
            return bookRepository.findByCategory(bookCategory);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    @Override
    public boolean isBookAvailable(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return book.getAvailableCopies() > 0;
    }

    // Search method implementations
    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        
        // Try to parse the query as a BookCategory
        try {
            BookCategory category = BookCategory.valueOf(query.toUpperCase());
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategory(
                query, query, category);
        } catch (IllegalArgumentException e) {
            // Not a valid category, just search by title and author
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
        }
    }

    // Location tracking methods
    @Override
    public Book updateBookLocation(Long id, BookLocation location) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        book.setLocation(location);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByLocation(BookLocation location) {
        return bookRepository.findByLocation(location);
    }
} 