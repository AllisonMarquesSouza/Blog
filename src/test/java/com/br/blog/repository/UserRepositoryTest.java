package com.br.blog.repository;

import com.br.blog.model.User;
import com.br.blog.util.UserCreate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@Log4j2
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find User by username and return a User when is successful")
    void findByUsernameToUSER() {
        User user1 = UserCreate.createUser1();
        User user2 = UserCreate.createUser2();
        userRepository.save(user1);
        userRepository.save(user2);

        User userFound = userRepository.findByUsernameToUSER(user1.getUsername()).orElseThrow();

        assertNotNull(userFound);
        assertEquals(userFound, user1);
    }
}