package com.microservices.posts.models;

public record ResponsePosts (String id,
                             String title,
                             String content,
                             String createdAt,
                             String updatedAt,
                             String author,
                             String imageId,
                             boolean isOwned){
}
