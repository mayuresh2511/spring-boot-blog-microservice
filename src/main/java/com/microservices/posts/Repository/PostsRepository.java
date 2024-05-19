package com.microservices.posts.Repository;

import com.microservices.posts.Entity.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends MongoRepository<Posts, String> {

    @Query("{author:'?0'}")
    Posts findPostByAuthor(String author);
}
