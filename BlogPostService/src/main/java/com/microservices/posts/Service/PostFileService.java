package com.microservices.posts.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.microservices.posts.Entity.Posts;
import com.microservices.posts.dto.DownloadFileDto;
import com.microservices.posts.dto.UpdatePostDto;
import com.microservices.posts.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PostFileService {
    private final AmazonS3 amazonS3Client;
    private final PostsService postsService;
    @Value("${aws.s3Bucket}")
    private String AWS_BUCKET_NAME;

    public PostFileService(AmazonS3 amazonS3,
                           PostsService postsService){
        this.amazonS3Client = amazonS3;
        this.postsService = postsService;
    }

    public void uploadFile(String postId, MultipartFile file){
        File convertedFile = FileUtils.convertMultipartToFile(file);
        String fileName = FileUtils.generateFileName(file);
        String filePath = "spring_microservice/" +fileName;
        UpdatePostDto updatePostDto = new UpdatePostDto(filePath);

        Posts post = postsService.getPost(postId);

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

        postsService.updatePost(postId, updatePostDto);
    }

    public DownloadFileDto downloadFile(String id) throws IOException {
        Posts post = postsService.getPost(id);
        String filePath = post.getImageId();
        String fileName = post.getImageId().split("/")[1];

        System.out.println("File path : " + post.getImageId() + " fileName : " + fileName);

        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(AWS_BUCKET_NAME, filePath));

        System.out.println("S3Object => " + s3Object.getBucketName() + " key : " + s3Object.getKey());

        return new DownloadFileDto(s3Object.getObjectContent().readAllBytes(), fileName, s3Object.getObjectMetadata().getContentType());
    }
}
