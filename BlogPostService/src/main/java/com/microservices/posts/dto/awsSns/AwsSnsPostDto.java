package com.microservices.posts.dto.awsSns;

public record AwsSnsPostDto (String postId, String title, String action, String message){
}
