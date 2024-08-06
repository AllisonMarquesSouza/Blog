package com.br.blog.controller;
import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import com.br.blog.dtos.blog.blogResponse.BlogResponseDto;
import com.br.blog.model.Blog;
import com.br.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("listAll")
    public ResponseEntity<List<BlogResponseDto>> findAll() {
        return ResponseEntity.ok(blogService.findAll());
    }
    @PostMapping("create")
    public ResponseEntity<Blog> create(@RequestBody BlogDtoPost blogDto) {
        return new ResponseEntity<>(blogService.save(blogDto), HttpStatus.CREATED);
    }
    @PutMapping("update")
    public ResponseEntity<Void> update(@RequestBody BlogDtoPut blogDtoPut) {
        blogService.update(blogDtoPut);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
