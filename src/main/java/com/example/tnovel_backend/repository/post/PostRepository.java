package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {

}
