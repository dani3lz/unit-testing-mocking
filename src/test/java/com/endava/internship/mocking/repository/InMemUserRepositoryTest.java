package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemUserRepositoryTest {
    private InMemUserRepository inMemUserRepository;

    @BeforeEach
    void setUp() {
        inMemUserRepository = new InMemUserRepository();
    }

    @Test
    void findByIdWithValidId_shouldReturnUserWithThisId() {
        Optional<User> user = inMemUserRepository.findById(3);

        assertThat(user).isEqualTo(Optional.of(new User(3, "Peter", Status.INACTIVE)));
    }

    @Test
    void findByIdWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> inMemUserRepository.findById(null));
    }
}
