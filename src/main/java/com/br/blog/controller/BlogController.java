package com.br.blog.controller;

import com.br.blog.model.Blog;
import com.br.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blog")
//@RequiredArgsConstructor
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("")
    public ResponseEntity<List<Blog>> findAllBlog() {
        return ResponseEntity.ok(blogRepository.findAll());
    }
}
