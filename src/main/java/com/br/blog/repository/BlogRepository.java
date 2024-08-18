package com.br.blog.repository;

import com.br.blog.model.Blog;
import com.br.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findByAuthor(User author);

    @Query("SELECT b Blog FROM  Blog b WHERE b.author.username=:username ")
    List<Blog> findAllMyPosts(@Param("username") String username);

    @Query("""
    SELECT b FROM Blog b
        WHERE (:title IS NULL OR b.title LIKE CONCAT('%', :title, '%'))
        AND (:username IS NULL OR b.author.username LIKE CONCAT('%', :username, '%'))
         AND (:content IS NULL OR b.content LIKE CONCAT('%', :content, '%'))
    """)
    List<Blog> filterByAuthorOrTitleOrContent(@Param("title") String title, @Param("username") String username, @Param("content") String content);

    @Query("SELECT b FROM Blog b WHERE b.author.id = :id")
    Blog findBlogByAuthorId(@Param("id") Long id);

}
