package com.br.blog.repository;

import com.br.blog.model.Blog;
import com.br.blog.model.User;
import com.br.blog.util.BlogCreate;
import com.br.blog.util.UserCreate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@Log4j2
class BlogRepositoryTest {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find all posts of one user when is successful")
    void findAllMyPosts() {
        User user = UserCreate.createUser1();
        User user2 = UserCreate.createUser2();
        userRepository.save(user);
        userRepository.save(user2);

        Blog blog1 = BlogCreate.createBlogUser1();
        Blog blog2 = BlogCreate.createBlogUser2();
        blogRepository.save(blog1);
        blogRepository.save(blog2);

        List<Blog> postsUser1 = blogRepository.findAllMyPosts(user.getUsername());

        assertEquals(postsUser1.size(), 1);
        assertEquals(postsUser1.get(0), blog1);
    }

    @Test
    @DisplayName("Filter blog By Author , Title or Content , when is successful")
    void filterByAuthorOrTitleOrContent() {
        User user = UserCreate.createUser1();
        Blog blog1 = BlogCreate.createBlogUser1();
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
    @DisplayName("Find blog by author id when is successful")
    void findBlogByAuthorId() {
        User user = UserCreate.createUser1();
        Blog blog1 = BlogCreate.createBlogUser1();
        userRepository.save(user);
        blogRepository.save(blog1);

        Blog blogByAuthorID = blogRepository.findBlogByAuthorId(user.getId());

        assertNotNull(blogByAuthorID);
        assertEquals(blogByAuthorID, blog1);
    }
}