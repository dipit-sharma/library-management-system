package com.spry.library_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spry.library_service.dto.BookDto;
import com.spry.library_service.dto.BookSearchRequest;
import com.spry.library_service.entity.AvailabilityStatus;
import com.spry.library_service.entity.Book;
import com.spry.library_service.repository.BookRepository;

@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final NotificationService notificationService;
    
    public BookService(BookRepository bookRepository, NotificationService notificationService) {
        this.bookRepository = bookRepository;
        this.notificationService = notificationService;
    }
    
    public BookDto createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbnAndDeletedFalse(bookDto.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + bookDto.getIsbn() + " already exists");
        }
        
        validatePublishedYear(bookDto.getPublishedYear());
        
        Book book = new Book(
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getIsbn(),
                bookDto.getPublishedYear(),
                bookDto.getAvailabilityStatus()
        );
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    @Transactional(readOnly = true)
    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .filter(book -> !book.getDeleted())
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDto> getBooks(BookSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                Sort.by(Sort.Direction.ASC, "title")
        );
        
        Page<Book> books;
        
        if (searchRequest.getSearchTerm() != null && !searchRequest.getSearchTerm().trim().isEmpty()) {
            books = bookRepository.searchBooks(searchRequest.getSearchTerm().trim(), pageable);
        } else if (searchRequest.getAuthor() != null && searchRequest.getPublishedYear() != null) {
            books = bookRepository.findByAuthorContainingIgnoreCaseAndPublishedYearAndDeletedFalse(
                    searchRequest.getAuthor(), searchRequest.getPublishedYear(), pageable);
        } else if (searchRequest.getAuthor() != null) {
            books = bookRepository.findByAuthorContainingIgnoreCaseAndDeletedFalse(
                    searchRequest.getAuthor(), pageable);
        } else if (searchRequest.getPublishedYear() != null) {
            books = bookRepository.findByPublishedYearAndDeletedFalse(
                    searchRequest.getPublishedYear(), pageable);
        } else {
            books = bookRepository.findByDeletedFalse(pageable);
        }
        
        return books.map(this::convertToDto);
    }
    
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .filter(book -> !book.getDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        
        if (!existingBook.getIsbn().equals(bookDto.getIsbn()) && 
            bookRepository.existsByIsbnAndDeletedFalse(bookDto.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + bookDto.getIsbn() + " already exists");
        }
        
        validatePublishedYear(bookDto.getPublishedYear());
        
        boolean shouldNotify = existingBook.getAvailabilityStatus() == AvailabilityStatus.BORROWED &&
                              bookDto.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE;
        
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setIsbn(bookDto.getIsbn());
        existingBook.setPublishedYear(bookDto.getPublishedYear());
        existingBook.setAvailabilityStatus(bookDto.getAvailabilityStatus());
        
        Book updatedBook = bookRepository.save(existingBook);
        
        if (shouldNotify) {
            notificationService.notifyWishlistUsersAsync(updatedBook);
        }
        
        return convertToDto(updatedBook);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        
        book.softDelete();
        bookRepository.save(book);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDto> searchBooks(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));
        Page<Book> books = bookRepository.searchBooks(searchTerm, pageable);
        return books.map(this::convertToDto);
    }
    
    private void validatePublishedYear(Integer publishedYear) {
        int currentYear = java.time.Year.now().getValue();
        if (publishedYear < 1000 || publishedYear > currentYear) {
            throw new IllegalArgumentException("Published year must be between 1000 and " + currentYear);
        }
    }
    
    private BookDto convertToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getAvailabilityStatus()
        );
    }
}
