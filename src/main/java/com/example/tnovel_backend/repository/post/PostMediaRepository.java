package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Integer> {
}
