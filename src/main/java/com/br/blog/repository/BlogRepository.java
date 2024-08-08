package com.br.blog.repository;

import com.br.blog.dtos.blog.blogResponse.BlogResponseDto;
import com.br.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {


    @Query("SELECT b Blog FROM  Blog b WHERE b.author.username=:username ")
    List<Blog> findAllMyPosts(@Param("username") String username);

    @Query("""
            SELECT new com.br.blog.dtos.blog.blogResponse.BlogResponseDto
            (b.author.username, b.title, b.content, b.created_at, b.updated_at)
             FROM Blog b
            """)
    List<BlogResponseDto> findAllPosts();

    @Query("""
        SELECT new com.br.blog.dtos.blog.blogResponse.BlogResponseDto
          (b.author.username, b.title, b.content, b.created_at, b.updated_at)
            FROM Blog b WHERE (:title IS NULL OR b.title LIKE %:title%)
            AND (:username IS NULL OR b.author.username LIKE %:username%)
        """)
    List<BlogResponseDto> findByTitleOrAuthor(@Param("title") String title, @Param("username") String username);

    @Query("SELECT b FROM Blog b WHERE b.author.id = :id")
    Blog findBlogByAuthorId(@Param("id") Long id);

    @Query("SELECT b FROM Blog b WHERE b.author.username = :username")
    Optional<Blog> findBlogByAuthorUsername(@Param("username") String username);
}
