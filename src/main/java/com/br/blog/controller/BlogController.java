package com.br.blog.controller;

import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import com.br.blog.model.Blog;
import com.br.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary =  "listAll", method = "GET", description ="List all", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("listAll")
    public ResponseEntity<List<Blog>> findAll() {
        return ResponseEntity.ok(blogService.findAllPost());
    }

    @Operation(summary =  "listAllMine", method = "GET", description ="List all mine", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("listAllMine")
    public ResponseEntity<List<Blog>> findAllMine() {
        return ResponseEntity.ok(blogService.findAllMyPosts());
    }

    @Operation(summary =  "search", method = "GET", description ="Filter the blogs by fields", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("search")
    public ResponseEntity<List<Blog>> filterFields(@RequestParam(required = false) String title,
                                                              @RequestParam(required = false) String author,
                                                              @RequestParam(required = false) String content){
        return ResponseEntity.ok(blogService.findByTitleOrAuthorOrDateCreated(title, author, content));
    }

    @Operation(summary =  "create", method = "POST", description ="Create a blog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation")
    })
    @PostMapping("create")
    public ResponseEntity<Blog> create(@RequestBody BlogDtoPost blogDto) {
        return new ResponseEntity<>(blogService.save(blogDto), HttpStatus.CREATED);
    }

    @Operation(summary =  "update", method = "PUT", description ="Update a blog", responses = {
            @ApiResponse(responseCode = "204", description = "successful operation")
    })
    @PutMapping("update")
    public ResponseEntity<Void> update(@RequestBody BlogDtoPut blogDtoPut) {
        blogService.update(blogDtoPut);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary =  "delete", method = "DELETE", description ="Delete a blog", responses = {
            @ApiResponse(responseCode = "204", description = "successful operation")
    })
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
