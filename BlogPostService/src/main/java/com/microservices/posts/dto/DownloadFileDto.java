package com.microservices.posts.dto;

public record DownloadFileDto(byte[] fileBytes, String fileName, String fileType){
}
