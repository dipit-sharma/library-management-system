package com.spry.library_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spry.library_service.dto.WishlistDto;
import com.spry.library_service.entity.Book;
import com.spry.library_service.entity.User;
import com.spry.library_service.entity.Wishlist;
import com.spry.library_service.repository.BookRepository;
import com.spry.library_service.repository.UserRepository;
import com.spry.library_service.repository.WishlistRepository;

@Service
@Transactional
public class WishlistService {
    
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    
    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, 
                          UserRepository userRepository, 
                          BookRepository bookRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }
    
    public WishlistDto addToWishlist(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.getDeleted())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Book book = bookRepository.findById(bookId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        
        if (wishlistRepository.existsByUserIdAndBookIdAndDeletedFalse(userId, bookId)) {
            throw new IllegalArgumentException("Book is already in user's wishlist");
        }
        
        Wishlist wishlist = new Wishlist(user, book);
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        
        return convertToDto(savedWishlist);
    }

    private WishlistDto convertToDto(Wishlist wishlist) {
        return new WishlistDto(
                wishlist.getId(),
                wishlist.getUser().getId(),
                wishlist.getBook().getId(),
                wishlist.getUser().getName(),
                wishlist.getBook().getTitle()
        );
    }
}
