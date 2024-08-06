package com.br.blog.model;
import com.br.blog.dtos.blog.BlogDtoPost;
import com.br.blog.dtos.blog.BlogDtoPut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "blog_post")
@Getter
@NoArgsConstructor
@Setter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    public Blog(BlogDtoPost blogDtoPost, User author) {
        this.author = author;
        this.title = blogDtoPost.getTitle();
        this.content = blogDtoPost.getContent();
        this.created_at = LocalDateTime.now();
    }
    public Blog(BlogDtoPut blogDtoPut, User author) {
        this.author = author;
        this.title = blogDtoPut.getTitle();
        this.content = blogDtoPut.getContent();
        this.updated_at = LocalDateTime.now();
    }

}

