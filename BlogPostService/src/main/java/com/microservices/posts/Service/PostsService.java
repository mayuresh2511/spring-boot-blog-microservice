package com.microservices.posts.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;
import com.microservices.posts.Entity.Posts;
import com.microservices.posts.Repository.PostsRepository;
import com.microservices.posts.dto.UpdatePostDto;
import com.microservices.posts.dto.awsSns.AwsSnsPostDto;
import com.microservices.posts.models.CreatePostRequest;
import com.microservices.posts.models.ResponsePosts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostsService {
    private final PostsRepository postsRepository;
    final private AmazonSNS amazonSNSClient;
    @Value("${aws.snsArn}")
    private String snsArn;
    @Value("${aws.s3Bucket}")
    private String AWS_BUCKET_NAME;

    public PostsService(PostsRepository postsRepository,
                        AmazonSNS amazonSNSClient){
        this.postsRepository = postsRepository;
        this.amazonSNSClient = amazonSNSClient;
    }

    public String createPost(CreatePostRequest request, String userName){
        Posts posts = new Posts(request.getTitle(), request.getContent(), userName);
        Posts createdPost = postsRepository.save(posts);
        System.out.println("created post id : " + createdPost.getId());

        AwsSnsPostDto postDetails = new AwsSnsPostDto(createdPost.getId(),
                request.getTitle(),
                "Create",
                "Hi there you have created new post");
        Gson gson = new Gson();
        PublishResult publishResult = amazonSNSClient.publish(new PublishRequest
                (snsArn, gson.toJson(postDetails), "Post Activity")
        );

        System.out.println("Publish result => " + publishResult.getMessageId());

        return createdPost.getId();
    }

    public Posts getPost(String id){
        return postsRepository.findById(id).orElse(null);
    }

    public Posts getAllPostsOfUser(String userId){
        return postsRepository.findPostByAuthor(userId);
    }

    public List<ResponsePosts> getAllPosts(String userName){
        return postsRepository.findAll().stream()
                .map((post) -> {
                    if (post.getAuthor().equalsIgnoreCase(userName)){
                        return new ResponsePosts(post.getId(),
                                post.getTitle(),
                                post.getContent(),
                                post.getCreatedAt(),
                                post.getUpdatedAt(),
                                post.getAuthor(),
                                post.getImageId(),
                                true);
                    }else {
                        return new ResponsePosts(post.getId(),
                                post.getTitle(),
                                post.getContent(),
                                post.getCreatedAt(),
                                post.getUpdatedAt(),
                                post.getAuthor(),
                                post.getImageId(),
                                false);
                    }
                }).toList();
    }

    public Posts updatePost(String id, UpdatePostDto updatePost){
        Posts post = getPost(id);

        if (updatePost.title() != null && !updatePost.title().trim().isEmpty()){
            post.setTitle(updatePost.title());
        }
        if (updatePost.content() != null && !updatePost.content().trim().isEmpty()){
            post.setContent(updatePost.content());
        }
        if (updatePost.imageUrl() != null && !updatePost.imageUrl().trim().isEmpty()){
            post.setImageId(updatePost.imageUrl());
        }
        post.setUpdatedAt();
        return postsRepository.save(post);
    }

    public void deletePost(String id){
        Posts post = postsRepository.findById(id).orElse(null);

        if (post != null){
            postsRepository.deleteById(id);
            AwsSnsPostDto postDetails = new AwsSnsPostDto(post.getId(),
                    post.getTitle(),
                    "Delete",
                    "Hi there you have deleted post");
            Gson gson = new Gson();
            PublishResult publishResult = amazonSNSClient.publish(new PublishRequest
                    (snsArn, gson.toJson(postDetails))
            );
            System.out.println("Publish result => " + publishResult.getMessageId());
        }
    }
}
