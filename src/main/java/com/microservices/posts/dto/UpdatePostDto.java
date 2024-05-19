package com.microservices.posts.dto;

public record UpdatePostDto (String title, String content, String imageUrl){
    public UpdatePostDto(String imagePath){
        this(null, null, imagePath);
    }
}
