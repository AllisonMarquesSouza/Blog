package com.br.blog.service;

import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import com.br.blog.enums.UserRole;
import com.br.blog.exception.personalizedExceptions.BadRequestException;
import com.br.blog.model.Blog;
import com.br.blog.model.User;
import com.br.blog.repository.BlogRepository;
import com.br.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
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

    private User user;
    private Blog existingBlog;

    @BeforeEach
    void setUp() {
        user = new User(1L, "allison", "123456", "allison@email.com", UserRole.USER);
        existingBlog = new Blog(1L,  user, "Today", "Today is very hot", LocalDateTime.now().plusHours(2), null);
        Mockito.when(userRepository.findByUsernameToUSER("allison")).thenReturn(Optional.of(user));
        Mockito.when(authService.getCurrentUser()).thenReturn(user);
    }


    @Test
    @DisplayName("Should find all blogs when is successful")
    void findAllPost() {
        Mockito.when(blogRepository.findAll())
                .thenReturn(List.of(existingBlog));

        List<Blog> allPosts = blogService.findAllPost();

        assertEquals(allPosts.size(), 1);
        assertEquals(allPosts.get(0), existingBlog);
    }

    @Test
    @DisplayName("Should find all blogs by User current When is successful")
    void findAllMyPosts() {
        Mockito.when(blogRepository.findAllMyPosts("allison"))
                .thenReturn(List.of(existingBlog));

        List<Blog> allMyPosts = blogService.findAllMyPosts();

        assertEquals(allMyPosts.size(), 1);
        assertEquals(allMyPosts.get(0), existingBlog);
    }
    @Test
    @DisplayName("Should throw BadRequestException when find all Blog isn't found ")
    void findAllMyPostsCaseError() {

        Mockito.when(blogRepository.findAllMyPosts("allison"))
                .thenThrow(new BadRequestException("You haven't posts"));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                blogService.findAllMyPosts());

        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "You haven't posts");

    }

    @Test
    @DisplayName("Should filter Blog By Author , Title or Content , when is successful")
    void findByTitleOrAuthorOrDateCreated() {

        Mockito.when(blogRepository.filterByAuthorOrTitleOrContent("Today", "allison", "Today is very hot"))
                .thenReturn(List.of(existingBlog));

        List<Blog> findByFilters = blogService.findByTitleOrAuthorOrDateCreated
                ("Today", "allison", "Today is very hot");

        assertEquals(findByFilters.size(), 1);
        assertEquals(findByFilters.get(0), existingBlog);

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

        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(existingBlog));

        Blog blogFound = blogService.findById(1L);

        assertNotNull(blogFound);
        assertEquals(blogFound, existingBlog);
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
        BlogDtoPost blogDtoPost = new BlogDtoPost("Today" ,"Today is very hot");

        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(existingBlog);

        Blog blogSaved = blogService.save(blogDtoPost);

        assertNotNull(blogSaved);
        assertEquals(blogSaved, existingBlog);

    }

    @Test
    @DisplayName("Should update a Blog when is successful")
    void update() {
        User userUpdated = new User(2L, "thefool", "123456", "thefool@email.com", UserRole.USER);

        Blog updateBlog = new Blog(2L, userUpdated, "new-blog", "Today I'm thinking very hot",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        BlogDtoPut blogPutDto = new BlogDtoPut(2L,"new-blog", "Today I'm thinking very hot");

        Mockito.when(blogRepository.findById(2L)).thenReturn(Optional.of(this.existingBlog));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(updateBlog);

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
        BlogDtoPut blogPutDto = new BlogDtoPut(1L,"Today", "Today I'm thinking very hot");

        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(this.existingBlog));
        Mockito.when(blogRepository.save(Mockito.any(Blog.class)))
                .thenThrow(new BadRequestException("Update failed"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> blogService.update(blogPutDto));


        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "Update failed");
    }

    @Test
    @DisplayName("Should delete a Blog by id when is successful")
    void delete() {
        Mockito.when(blogRepository.findBlogByAuthorId(1L)).thenReturn(this.existingBlog);
        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(this.existingBlog));

        assertDoesNotThrow(() -> blogService.delete(1L));
        verify(blogRepository).deleteById(1L);

    }
    @Test
    @DisplayName("Should throw BadRequestException when delete by id failed ")
    void deleteCaseError() {
        Mockito.when(blogRepository.findBlogByAuthorId(1L)).thenReturn(this.existingBlog);
        Mockito.when(blogRepository.findById(1L)).thenReturn(Optional.of(this.existingBlog));

        Mockito.doThrow(new BadRequestException("Delete failed")).when(blogRepository).deleteById(1L);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> blogService.delete(1L));

        assertSame(exception.getClass(), BadRequestException.class);
        assertEquals(exception.getMessage(), "Delete failed");
        verify(blogRepository).deleteById(1L);

    }
}