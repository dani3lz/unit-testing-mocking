package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemUserRepositoryTest {
    private final InMemUserRepository inMemUserRepository = new InMemUserRepository();

    @Test
    void findByIdWithValidId_shouldReturnUserWithThisId() {
        Optional<User> user = inMemUserRepository.findById(3);

        assertEquals(user, Optional.of(new User(3, "Peter", Status.INACTIVE)));
    }

    @Test
    void findByIdWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> inMemUserRepository.findById(null));
    }
}
