package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemPaymentRepositoryTest {
    private InMemPaymentRepository inMemPaymentRepository;
    private final Payment payment1 = new Payment(1, 25.7, "Skip");
    private final Payment payment2 = new Payment(2, 17.1, "Skip");
    private final Payment payment3 = new Payment(3, 99.9, "Skip");


    @BeforeEach
    void setUp() {
        inMemPaymentRepository = new InMemPaymentRepository();
    }

    @Test
    void findByIdWithValidIdPayment_shouldReturnPaymentWithThisId() {
        UUID uuid = payment1.getPaymentId();
        inMemPaymentRepository.save(payment1);

        assertEquals(inMemPaymentRepository.findById(uuid), Optional.of(payment1));
    }

    @Test
    void findByIdWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.findById(null));
    }

    @Test
    void findAll_shouldReturnListWithPayments() {
        List<Payment> paymentList = asList(payment1, payment2, payment3);
        inMemPaymentRepository.save(payment1);
        inMemPaymentRepository.save(payment2);
        inMemPaymentRepository.save(payment3);

        assertThat(inMemPaymentRepository.findAll()).containsExactlyInAnyOrderElementsOf(paymentList);
    }

    @Test
    void saveMethodWithValidPayment_shouldReturnThisPayment() {
        assertEquals(inMemPaymentRepository.save(payment1), payment1);
    }

    @Test
    void saveMethodWithNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.save(null));
    }

    @Test
    void saveMethod_shouldThrowExceptionWhenPaymentIsAlreadySaved() {
        inMemPaymentRepository.save(payment1);

        assertAll(
                () -> assertTrue(inMemPaymentRepository.findById(payment1.getPaymentId()).isPresent()),
                () -> assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.save(payment1))
        );
    }

    @Test
    void editMessageWithValidPaymentId_shouldReturnThisPaymentWithNewMessage() {
        inMemPaymentRepository.save(payment1);
        String msg = "Some Message";
        inMemPaymentRepository.editMessage(payment1.getPaymentId(), msg);

        assertEquals(inMemPaymentRepository.editMessage(payment1.getPaymentId(), msg), payment1);
    }

    @Test
    void editMessageWithInvalidPaymentId_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> inMemPaymentRepository.editMessage(UUID.randomUUID(), "Skip"));
    }
}
