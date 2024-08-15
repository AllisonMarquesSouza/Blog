package com.br.blog.repository;

import com.br.blog.dtos.blog.blogResponse.BlogResponseDto;
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
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

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

        log.info(postsUser1);
        assertThat(postsUser1.size()).isEqualTo(1);
        assertThat(postsUser1.get(0)).isEqualTo(blog1);
        assertThat(postsUser1.get(0).getAuthor()).isEqualTo(blog1.getAuthor());
        assertThat(postsUser1.get(0).getAuthor()).isNotEqualTo(blog2.getAuthor());

    }

    @Test
    @DisplayName("Find all posts when is successful")
    void findAllPosts() {
        User user = UserCreate.createUser1();
        User user2 = UserCreate.createUser2();
        userRepository.save(user);
        userRepository.save(user2);

        Blog blog1 = BlogCreate.createBlogUser1();
        Blog blog2 = BlogCreate.createBlogUser2();
        blogRepository.save(blog1);
        blogRepository.save(blog2);

        List<Blog> allPosts = blogRepository.findAll();

        assertThat(allPosts).hasSize(2);
        assertThat(blog1).isEqualTo(allPosts.get(0));
        assertThat(blog2).isEqualTo(allPosts.get(1));

    }

    @Test
    @DisplayName("Filter blog By Author , Title or Content , when is successful")
    void filterByAuthorOrTitleOrContent() {
        User user = UserCreate.createUser1();
        Blog blog1 = BlogCreate.createBlogUser1();
        userRepository.save(user);
        blogRepository.save(blog1);

        List<BlogResponseDto> title = blogRepository.FilterByAuthorOrTitleOrContent("Today", "", "");
        List<BlogResponseDto> nameAuthor= blogRepository.FilterByAuthorOrTitleOrContent("", "allison", "");
        List<BlogResponseDto> content = blogRepository.FilterByAuthorOrTitleOrContent
                ("", "", "Today I'm guessing very hot, because the temperature is 38 Celsius");

        assertThat(title.size()).isEqualTo(1);
        assertThat(nameAuthor.size()).isEqualTo(1);
        assertThat(content.size()).isEqualTo(1);


        assertThat(title.get(0).getTitle()).isEqualTo(blog1.getTitle());
        assertThat(title.get(0).getAuthor()).isEqualTo(blog1.getAuthor().getUsername());
        assertThat(title.get(0).getContent()).isEqualTo(blog1.getContent());

        assertThat(nameAuthor.get(0).getTitle()).isEqualTo(blog1.getTitle());
        assertThat(nameAuthor.get(0).getAuthor()).isEqualTo(blog1.getAuthor().getUsername());
        assertThat(nameAuthor.get(0).getContent()).isEqualTo(blog1.getContent());

        assertThat(content.get(0).getTitle()).isEqualTo(blog1.getTitle());
        assertThat(content.get(0).getAuthor()).isEqualTo(blog1.getAuthor().getUsername());
        assertThat(content.get(0).getContent()).isEqualTo(blog1.getContent());

    }

    @Test
    @DisplayName("Find blog by author id when is successful")
    void findBlogByAuthorId() {
        User user = UserCreate.createUser1();
        Blog blog1 = BlogCreate.createBlogUser1();
        userRepository.save(user);
        blogRepository.save(blog1);

        Blog blogByAuthorID = blogRepository.findBlogByAuthorId(user.getId());

        assertThat(blogByAuthorID).isNotNull();
        assertThat(blogByAuthorID).isEqualTo(blog1);
        assertThat(blogByAuthorID.getAuthor()).isEqualTo(user);
    }
}