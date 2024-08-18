CREATE TABLE blog_post(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,

    CONSTRAINT FkUserID FOREIGN KEY (author_id)
    REFERENCES blog_user(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);