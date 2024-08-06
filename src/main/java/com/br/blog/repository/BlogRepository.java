package com.br.blog.repository;

import com.br.blog.dtos.blog.blogResponse.BlogResponseDto;
import com.br.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
//    @Query("SELECT b FROM Blog b WHERE b.author.username = :username")
//    List<Blog> findAllByUserSpecif(@Param("username") String username);

    @Query("""
            SELECT new com.br.blog.dtos.blog.blogResponse.BlogResponseDto
            (b.id, b.title, b.content, b.created_at, b.updated_at)
             FROM Blog b
            WHERE b.author.username = :username
            """)
    List<BlogResponseDto> findAllByUserSpecif(@Param("username") String username);


    @Query("SELECT b FROM Blog b WHERE b.author.id = :id")
    Blog findBlogByAuthorId(@Param("id") Long id);
}
