package com.microservices.posts.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.microservices.posts.Entity.Posts;
import com.microservices.posts.Repository.PostsRepository;
import com.microservices.posts.dto.DownloadFileDto;
import com.microservices.posts.dto.UpdatePostDto;
import com.microservices.posts.models.CreatePostRequest;
import com.microservices.posts.models.ResponsePosts;
import com.microservices.posts.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final AmazonS3 amazonS3Client;
    public PostsService(PostsRepository postsRepository,
                        AmazonS3 amazonS3Client){
        this.postsRepository = postsRepository;
        this.amazonS3Client = amazonS3Client;
    }
    private final String AWS_BUCKET_NAME = "psotmicroservicebucket";
    public String createPost(CreatePostRequest request, String userName){
        Posts posts = new Posts(request.getTitle(), request.getContent(), userName);
        Posts createdPost = postsRepository.save(posts);
        System.out.println("created post id : " + createdPost.getId());
        return createdPost.getId();
    }

    public void uploadFile(String postId, MultipartFile file){
        File convertedFile = FileUtils.convertMultipartToFile(file);
        String fileName = FileUtils.generateFileName(file);
        String filePath = "spring_microservice/" +fileName;
        UpdatePostDto updatePostDto = new UpdatePostDto(filePath);

        Posts post = getPost(postId);

        if (post.getImageId() != null && !post.getImageId().trim().isEmpty()){
            amazonS3Client.deleteObject(new DeleteObjectRequest(AWS_BUCKET_NAME, post.getImageId()));
        }

        PutObjectResult putObjectResult = amazonS3Client.putObject(new PutObjectRequest
                (AWS_BUCKET_NAME, "spring_microservice/"+fileName, convertedFile)
        );

        System.out.println("Put result => versionId : " + putObjectResult.getVersionId() + "\n"
        + "contentMD5 : " + putObjectResult.getContentMd5() + "\n"
        + "metadata : " + putObjectResult.getMetadata() + "\n"
        + "ETag : " + putObjectResult.getETag());

        updatePost(postId, updatePostDto);
    }

    public DownloadFileDto downloadFile(String id) throws IOException {
        Posts post = getPost(id);
        String filePath = post.getImageId();
        String fileName = post.getImageId().split("/")[1];

        System.out.println("File path : " + post.getImageId() + " fileName : " + fileName);

        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(AWS_BUCKET_NAME, filePath));

        System.out.println("S3Object => " + s3Object.getBucketName() + " key : " + s3Object.getKey());

        return new DownloadFileDto(s3Object.getObjectContent().readAllBytes(), fileName, s3Object.getObjectMetadata().getContentType());
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
        postsRepository.deleteById(id);
    }
}
