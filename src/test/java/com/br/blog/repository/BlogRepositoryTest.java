package com.br.blog.repository;

import com.br.blog.enums.UserRole;
import com.br.blog.model.Blog;
import com.br.blog.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class BlogRepositoryTest {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        blogRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find all posts of one user when is successful")
    void findAllMyPosts() {
        User user1 = new User(null, "allison", "123456", "allison@email.com", UserRole.USER);
        User user2 = new User(null, "thefool", "123456", "thefool@email.com", UserRole.ADMIN);
        userRepository.save(user1);
        userRepository.save(user2);

        Blog blog1 = new Blog(null,  user1, "Today", "Today is very hot", LocalDateTime.now().plusHours(2), null);
        Blog blog2 = new Blog(null,  user2, "Tomorrow", "will be so cold", LocalDateTime.now().plusHours(5), null);
        blogRepository.save(blog1);
        blogRepository.save(blog2);

        List<Blog> postsUser1 = blogRepository.findAllMyPosts(user1.getUsername());

        assertEquals(postsUser1.size(), 1);
        assertEquals(postsUser1.get(0), blog1);
    }

    @Test
    @DisplayName("Should filter blog By Author , Title or Content , when is successful")
    void filterByAuthorOrTitleOrContent() {
        User user = new User(null, "allison", "123456", "allison@email.com", UserRole.USER);
        Blog blog1 = new Blog(null,  user, "Today", "Today is very hot", LocalDateTime.now().plusHours(2), null);

        userRepository.save(user);
        blogRepository.save(blog1);

        List<Blog> title = blogRepository.filterByAuthorOrTitleOrContent("Today", "", "");
        List<Blog> nameAuthor= blogRepository.filterByAuthorOrTitleOrContent("", "allison", "");
        List<Blog> content = blogRepository.filterByAuthorOrTitleOrContent
                ("", "", "Today is very hot");

        assertEquals(title.size(), 1);
        assertEquals(nameAuthor.size(), 1);
        assertEquals(content.size(), 1);

        assertEquals(title.get(0), blog1);
        assertEquals(nameAuthor.get(0), blog1);
        assertEquals(content.get(0), blog1);
    }

    @Test
    @DisplayName("Should find blog by author id when is successful")
    void findBlogByAuthorId() {
        User user = new User(null, "allison", "123456", "allison@email.com", UserRole.USER);
        Blog blog = new Blog(null,  user, "Today", "Today is very hot", LocalDateTime.now().plusHours(2), null);
        userRepository.save(user);
        blogRepository.save(blog);

        Blog blogByAuthorID = blogRepository.findBlogByAuthorId(user.getId());

        assertNotNull(blogByAuthorID);
        assertEquals(blogByAuthorID, blog);
    }
}