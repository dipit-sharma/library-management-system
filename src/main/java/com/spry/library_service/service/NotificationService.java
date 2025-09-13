package com.spry.library_service.service;

import com.spry.library_service.entity.Book;
import com.spry.library_service.entity.Wishlist;
import com.spry.library_service.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    private final WishlistRepository wishlistRepository;
    
    @Autowired
    public NotificationService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }
    
    @Async
    public CompletableFuture<Void> notifyWishlistUsersAsync(Book book) {
        logger.info("Starting asynchronous notification process for book: {}", book.getTitle());
        
        try {
            List<Wishlist> wishlists = wishlistRepository.findByBookIdAndDeletedFalse(book.getId());
            
            if (wishlists.isEmpty()) {
                logger.info("No users found in wishlist for book: {}", book.getTitle());
                return CompletableFuture.completedFuture(null);
            }
            
            for (Wishlist wishlist : wishlists) {
                String notificationMessage = String.format(
                    "Notification prepared for user_id: %d - Book [%s] is now available.",
                    wishlist.getUser().getId(),
                    book.getTitle()
                );
                
                logger.info(notificationMessage);
            }
            
            logger.info("Completed notification process for book: {} - Notified {} users", 
                       book.getTitle(), wishlists.size());
            
        } catch (Exception e) {
            logger.error("Error occurred during notification process for book: {}", book.getTitle(), e);
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
