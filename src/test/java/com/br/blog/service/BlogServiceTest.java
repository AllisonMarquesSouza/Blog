package com.br.blog.service;

import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import com.br.blog.exception.personalizedExceptions.BadRequestException;
import com.br.blog.model.Blog;
import com.br.blog.model.User;
import com.br.blog.repository.BlogRepository;
import com.br.blog.repository.UserRepository;
import com.br.blog.util.BlogCreate;
import com.br.blog.util.UserCreate;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Log4j2
class BlogServiceTest {
    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BlogService blogService;

    @Test
    @DisplayName("Should find all blogs when is successful")
    void findAllPost() {
        Blog blogExpected1 = BlogCreate.createBlogUser1();
        Blog blogExpected2 = BlogCreate.createBlogUser2();

        Mockito.when(blogRepository.findAll())
                .thenReturn(List.of(blogExpected1, blogExpected2));

        List<Blog> allPosts = blogService.findAllPost();

        assertEquals(allPosts.size(), 2);
        assertEquals(allPosts.get(0), blogExpected1);
        assertEquals(allPosts.get(1), blogExpected2);
    }

    @Test
    @DisplayName("Should find all blogs by User current When is successful")
    void findAllMyPosts() {
        User user = UserCreate.createUser1();
        Blog blogExpected = BlogCreate.createBlogUser1();

        Mockito.when(authService.getCurrentUser()).thenReturn(user);

        Mockito.when(blogRepository.findAllMyPosts(user.getUsername()))
                .thenReturn(List.of(blogExpected));

        List<Blog> allMyPosts = blogService.findAllMyPosts();

        assertEquals(allMyPosts.size(), 1);
        assertEquals(allMyPosts.get(0), blogExpected);
    }
    @Test
    @DisplayName("Should throw BadRequestException when find all Blog isn't found ")
    void findAllMyPostsCaseError() {
        User user = UserCreate.createUser1();

        Mockito.when(authService.getCurrentUser()).thenReturn(user);

        Mockito.when(blogRepository.findAllMyPosts(user.getUsername()))
                .thenThrow(new BadRequestException("You haven't posts"));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                blogService.findAllMyPosts());

        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "You haven't posts");

    }

    @Test
    @DisplayName("Should filter Blog By Author , Title or Content , when is successful")
    void findByTitleOrAuthorOrDateCreated() {
        Blog blogExpected = BlogCreate.createBlogUser1();

        Mockito.when(blogRepository.filterByAuthorOrTitleOrContent("Today", "allison", "Today is very hot"))
                .thenReturn(List.of(blogExpected));

        List<Blog> findByFilters = blogService.findByTitleOrAuthorOrDateCreated
                ("Today", "allison", "Today is very hot");

        assertEquals(findByFilters.size(), 1);
        assertEquals(findByFilters.get(0), blogExpected);

    }

    @Test
    @DisplayName("Should throw BadRequestException when no Blog is found using filters by Author, Title, or Content")
    void findByTitleOrAuthorOrDateCreatedCaseError() {
        Mockito.when(blogRepository.filterByAuthorOrTitleOrContent("Today", "allison", "Today is very hot"))
                .thenThrow(new BadRequestException("There aren't any posts with the filters"));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                blogService.findByTitleOrAuthorOrDateCreated("Today", "allison", "Today is very hot"));

        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "There aren't any posts with the filters");

    }

    @Test
    @DisplayName("Should find Blog by id when is successful")
    void findById() {
        Blog blogExpected = BlogCreate.createBlogUser1();

        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(blogExpected));

        Blog blogFound = blogService.findById(1L);

        assertNotNull(blogFound);
        assertEquals(blogFound, blogExpected);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when no Blog is found by id")
    void findByIdCaseError() {
        Mockito.when(blogRepository.findById(1L))
                .thenThrow(new EntityNotFoundException("Blog not found by id"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                blogService.findById(1L));

        assertSame(exception.getClass(), EntityNotFoundException.class);
        assertEquals(exception.getMessage(), "Blog not found by id");
    }

    @Test
    @DisplayName("Should save a Blog when is successful")
    void save() {
        User user = UserCreate.createUser1();
        Blog blogExpected = BlogCreate.createBlogUser1();
        BlogDtoPost blogDtoPost = new BlogDtoPost("Today" ,"Today is very hot");

        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));
        Mockito.when(authService.getCurrentUser()).thenReturn(user);
        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blogExpected);

        Blog blogSaved = blogService.save(blogDtoPost);

        assertNotNull(blogSaved);
        assertEquals(blogSaved, blogExpected);

    }

    @Test
    @DisplayName("Should update a Blog when is successful")
    void update() {
        User user = UserCreate.createUser1();
        Blog blogExpected = BlogCreate.createBlogUser1();
        BlogDtoPut blogPutDto = new BlogDtoPut(1L,"Today", "Today I'm thinking very hot");

        Mockito.when(authService.getCurrentUser()).thenReturn(user);
        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));

        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(blogExpected));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blogExpected);

        assertDoesNotThrow(() -> blogService.update(blogPutDto));

        verify(blogRepository).save(Mockito.argThat(updatedBlog ->
                updatedBlog.getId().equals(blogPutDto.getId()) &&
                        updatedBlog.getTitle().equals(blogPutDto.getTitle()) &&
                        updatedBlog.getContent().equals(blogPutDto.getContent())
        ));
    }
    @Test
    @DisplayName("Should throw BadRequestException when update failed ")
    void updateCaseError() {
        User user = UserCreate.createUser1();
        Blog blogExpected = BlogCreate.createBlogUser1();
        BlogDtoPut blogPutDto = new BlogDtoPut(1L,"Today", "Today I'm thinking very hot");

        Mockito.when(authService.getCurrentUser()).thenReturn(user);
        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));

        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(blogExpected));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class)))
                .thenThrow(new BadRequestException("Update failed"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> blogService.update(blogPutDto));


        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "Update failed");
    }

    @Test
    @DisplayName("Should delete a Blog by id when is successful")
    void delete() {
        User user = UserCreate.createUser1();
        Blog expectedBlog = BlogCreate.createBlogUser1();

        BlogDtoPost blogPostDto = new BlogDtoPost("Today", "Today is very hot");

        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));
        Mockito.when(authService.getCurrentUser()).thenReturn(user);

        Mockito.when(blogRepository.findBlogByAuthorId(1L)).thenReturn(expectedBlog);
        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(expectedBlog));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(expectedBlog);

        Blog blogSaved = blogService.save(blogPostDto);
        blogSaved.setId(1L);

        blogService.delete(1L);
        verify(blogRepository).deleteById(1L);

    }
    @Test
    @DisplayName("Should throw BadRequestException when delete by id failed ")
    void deleteCaseError() {
        User user = UserCreate.createUser1();
        Blog blogExpected = BlogCreate.createBlogUser1();

        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));
        Mockito.when(authService.getCurrentUser()).thenReturn(user);

        Mockito.when(blogRepository.findBlogByAuthorId(1L)).thenReturn(blogExpected);
        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(blogExpected));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blogExpected);

        Mockito.doThrow(new BadRequestException("Delete failed")).when(blogRepository).deleteById(1L);


        BadRequestException exception = assertThrows(BadRequestException.class, () -> blogService.delete(1L));


        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "Delete failed");
        verify(blogRepository).deleteById(1L);

    }
}