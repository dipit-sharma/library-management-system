package com.spry.library_service.dto;

public class BookSearchRequest {
    
    private String author;
    private Integer publishedYear;
    private String searchTerm;
    private int page = 0;
    private int size = 10;
    
    public BookSearchRequest() {}
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Integer getPublishedYear() {
        return publishedYear;
    }
    
    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    @Override
    public String toString() {
        return "BookSearchRequest{" +
                "author='" + author + '\'' +
                ", publishedYear=" + publishedYear +
                ", searchTerm='" + searchTerm + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
