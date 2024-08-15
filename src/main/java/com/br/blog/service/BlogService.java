package com.br.blog.service;


import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import com.br.blog.dtos.blog.blogResponse.BlogResponseDto;
import com.br.blog.exception.personalizedExceptions.BadRequestException;
import com.br.blog.model.Blog;
import com.br.blog.model.User;
import com.br.blog.repository.BlogRepository;
import com.br.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authService;

    public List<BlogResponseDto> findAllPost() {
        return blogRepository.findAllPosts();
    }

    public List<Blog> findAllMyPosts() {
        String usernameToUser = authService.getCurrentUser().getUsername();
        return blogRepository.findAllMyPosts(usernameToUser);
    }

    public List<BlogResponseDto> findByTitleOrAuthorOrDateCreated(String title, String author, String content ) {
        return blogRepository.FilterByAuthorOrTitleOrContent(title, author, content);
    }

    public Blog findById(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog not found, check the id"));
    }
//    public Blog findByCreatedDate(LocalDateTime date){
//        return blogRepository.findByCreated
//    }

    @Transactional
    public Blog save(BlogDtoPost blogDtoPost) {
        User user = userRepository.findByUsernameToUSER(authService.getCurrentUser().getUsername())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        Blog blog = new Blog(blogDtoPost, user);
        return blogRepository.save(blog);
    }

    @Transactional
    public void update(BlogDtoPut blogDtoPut) {
        User user = userRepository.findByUsernameToUSER(authService.getCurrentUser().getUsername())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        Blog blogToBeUpdate = this.findById(blogDtoPut.getId());

        if(user.equals(blogToBeUpdate.getAuthor())) {
            Blog blogToSave = new Blog(blogDtoPut, user);
            blogToSave.setId(blogDtoPut.getId());
            blogToSave.setCreated_at(blogToBeUpdate.getCreated_at());
            blogRepository.save(blogToSave);
            return;
        }
        throw new BadRequestException("Update failed, check your login and if you have a blog with ID " + blogDtoPut.getId());
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findByUsernameToUSER(authService.getCurrentUser().getUsername())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        Blog blogCurrentUser = blogRepository.findBlogByAuthorId(user.getId());
        Blog blogToDelete = this.findById(id);
        if(blogToDelete.equals(blogCurrentUser)) {
            blogRepository.delete(blogToDelete);
            return;
        }
        throw new BadRequestException("Delete failed, check your login and if you have a blog with ID " + id);
    }
}
