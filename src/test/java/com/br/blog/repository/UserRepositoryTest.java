package com.br.blog.repository;

import com.br.blog.enums.UserRole;
import com.br.blog.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find User by username when is successful")
    void findByUsernameToUSER() {
        User user1 = new User(1L, "allison", "123456", "allison@email.com", UserRole.USER);
        User user2 = new User(2L, "thefool", "123456", "thefool@email.com", UserRole.ADMIN);
        userRepository.save(user1);
        userRepository.save(user2);

        User userFound = userRepository.findByUsernameToUSER(user1.getUsername()).orElseThrow();

        assertNotNull(userFound);
        assertEquals(userFound, user1);
    }
}