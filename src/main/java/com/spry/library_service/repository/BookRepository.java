package com.spry.library_service.repository;

import com.spry.library_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbnAndDeletedFalse(String isbn);
    
    boolean existsByIsbnAndDeletedFalse(String isbn);
    
    Page<Book> findByAuthorContainingIgnoreCaseAndDeletedFalse(String author, Pageable pageable);
    
    Page<Book> findByPublishedYearAndDeletedFalse(Integer publishedYear, Pageable pageable);
    
    Page<Book> findByAuthorContainingIgnoreCaseAndPublishedYearAndDeletedFalse(
            String author, Integer publishedYear, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    Page<Book> findByDeletedFalse(Pageable pageable);
}
