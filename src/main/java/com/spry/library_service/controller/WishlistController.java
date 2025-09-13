package com.spry.library_service.controller;

import com.spry.library_service.dto.WishlistDto;
import com.spry.library_service.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    
    @PostMapping("/users/{userId}/books/{bookId}")
    public ResponseEntity<WishlistDto> addToWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        try {
            WishlistDto wishlist = wishlistService.addToWishlist(userId, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
}
