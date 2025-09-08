package com.example.tnovel_backend.controller.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시물", description = "게시물 관련 API")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

   
}
