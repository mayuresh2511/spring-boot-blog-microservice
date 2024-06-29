package com.microservices.posts.Controller;

import com.microservices.posts.Entity.Posts;
import com.microservices.posts.Service.PostsService;
import com.microservices.posts.dto.DownloadFileDto;
import com.microservices.posts.dto.UpdatePostDto;
import com.microservices.posts.models.CreatePostRequest;
import com.microservices.posts.models.ResponsePosts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post/api/v1/")
public class PostsController {
    final private Logger logger = LoggerFactory.getLogger(PostsController.class);
    private final PostsService postsService;
    public PostsController(PostsService postsService){
        this.postsService = postsService;
    }
    @PostMapping(value = "post",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPost(@Valid @RequestBody CreatePostRequest postRequest, HttpServletRequest request){
        logger.info("Inside createPost method");
        logger.info("User name is : " + request.getHeader("loggedInUser"));
        String postId = postsService.createPost(postRequest, request.getHeader("loggedInUser"));
        return ResponseEntity.status(201).body("Post Created Successfully : " + postId);
    }

    @GetMapping(value = "post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Posts> getPost(@PathVariable("postId") String postId){
        logger.info("Inside getPost method");
        logger.info("Post Id is : " + postId);
        Posts post = postsService.getPost(postId);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping(value = "posts/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Posts> getAllPostsOfUser(@PathVariable("userId") String userId){
        logger.info("Inside getAllPostsOfUser method");
        logger.info("Fetch posts of user : " + userId);
        Posts post = postsService.getAllPostsOfUser(userId);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping(value = "posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponsePosts>> getAllPosts(HttpServletRequest request){
        logger.info("Inside getAllPosts method");
        logger.info("User id : " + request.getHeader("loggedInUser"));
        List<ResponsePosts> posts = postsService.getAllPosts(request.getHeader("loggedInUser"));
        return ResponseEntity.ok().body(posts);
    }

    @PutMapping(value = "post/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Posts> updatePost(@PathVariable("postId") String postId,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content){
        logger.info("Inside updatePost method");
        logger.info("Post Id is : " + postId + "\n"
        + "Title is : " + title + "\n"
        + "Content is : " + content);
        UpdatePostDto updatePost = new UpdatePostDto(title, content, null);
        Posts post = postsService.updatePost(postId, updatePost);
        return ResponseEntity.ok().body(post);
    }

    @DeleteMapping(value = "post/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePost(@PathVariable("postId") String postId){
        logger.info("Inside deletePost method");
        logger.info("Post Id is : " + postId);
        postsService.deletePost(postId);
        return ResponseEntity.ok().body("Post " + postId + " deleted successfully");
    }
}
