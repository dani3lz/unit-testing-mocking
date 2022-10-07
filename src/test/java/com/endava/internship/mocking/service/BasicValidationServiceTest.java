package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BasicValidationServiceTest {
    private BasicValidationService basicValidationService;

    @BeforeEach
    void setUp() {
        basicValidationService = new BasicValidationService();
    }

    @Test
    void validateAmountWithValidAmount_shouldNotThrowAnyException() {
        assertDoesNotThrow(() -> basicValidationService.validateAmount(33.3));
    }

    @Test
    void validateAmountWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validateAmount(null));
    }

    @Test
    void validateAmountWithNegativeAmount_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validateAmount(-25.7));
    }

    @Test
    void validatePaymentIdWithValidId_shouldNotThrowAnyException() {
        assertDoesNotThrow(() -> basicValidationService.validatePaymentId(UUID.randomUUID()));
    }

    @Test
    void validatePaymentIdWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validatePaymentId(null));
    }

    @Test
    void validateUserIdWithValidId_shouldNotThrowAnyException() {
        assertDoesNotThrow(() -> basicValidationService.validateUserId(1));
    }

    @Test
    void validateUserIdWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validateUserId(null));
    }

    @Test
    void validateUserWithActiveStatus_shouldNotThrowAnyException() {
        User userActive = new User(1, "Ivan", Status.ACTIVE);
        assertDoesNotThrow(() -> basicValidationService.validateUser(userActive));
    }

    @Test
    void validateUserWithInactiveStatus_shouldThrowException() {
        User userInactive = new User(2, "Marcel cel Mare", Status.INACTIVE);
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validateUser(userInactive));
    }

    @Test
    void validateMessageWithValidMessage_shouldNotThrowAnyException() {
        assertDoesNotThrow(() -> basicValidationService.validateMessage("Valid Payment Message"));
    }

    @Test
    void validateMessageWithNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> basicValidationService.validateMessage(null));
    }
}
