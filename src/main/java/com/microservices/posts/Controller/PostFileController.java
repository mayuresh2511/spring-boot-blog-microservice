package com.microservices.posts.Controller;

import com.microservices.posts.Service.PostFileService;
import com.microservices.posts.dto.DownloadFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/post/api/v1/")
public class PostFileController {
    final private Logger logger = LoggerFactory.getLogger(PostFileController.class);
    final private PostFileService postFileService;

    public PostFileController(PostFileService postFileService){
        this.postFileService = postFileService;
    }
    @PostMapping(value = "upload/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file,
                                             @RequestParam("postId") String postId){
        logger.info("Inside uploadFile method");
        logger.info("Post id : " + postId);
        logger.info("File name : " + file.getName() + "\n"
                + " content type : " + file.getContentType());
        postFileService.uploadFile(postId, file);
        return ResponseEntity.status(201).body("File uploaded Successfully");
    }

    @GetMapping(value = "download/{postId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("postId") String postId){
        logger.info("Inside downloadFile methods");
        logger.info("Post Id is : " + postId);

        String fileName;
        byte[] bytes = new byte[0];
        try {
            DownloadFileDto downloadFile = postFileService.downloadFile(postId);
            bytes = downloadFile.fileBytes();
            fileName = downloadFile.fileName();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(bytes);
        }
    }
}
