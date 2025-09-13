package com.spry.library_service.dto;

public class WishlistDto {
    
    private Long id;
    private Long userId;
    private Long bookId;
    private String userName;
    private String bookTitle;
    
    public WishlistDto() {}
    
    public WishlistDto(Long id, Long userId, Long bookId, String userName, String bookTitle) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.userName = userName;
        this.bookTitle = bookTitle;
    }
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    @Override
    public String toString() {
        return "WishlistDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", userName='" + userName + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                '}';
    }
}
