package com.spry.library_service.repository;

import com.spry.library_service.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    Optional<Wishlist> findByUserIdAndBookIdAndDeletedFalse(Long userId, Long bookId);
    
    boolean existsByUserIdAndBookIdAndDeletedFalse(Long userId, Long bookId);
    
    @Query("SELECT w FROM Wishlist w JOIN FETCH w.user WHERE w.book.id = :bookId AND w.deleted = false")
    List<Wishlist> findByBookIdAndDeletedFalse(@Param("bookId") Long bookId);
    
    @Query("SELECT w FROM Wishlist w JOIN FETCH w.book WHERE w.user.id = :userId AND w.deleted = false")
    List<Wishlist> findByUserIdAndDeletedFalse(@Param("userId") Long userId);
}
