package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private PaymentService paymentService;
    @Captor
    ArgumentCaptor<Payment> paymentCaptor;
    private final Double AMOUNT = 100.0;
    private final Integer ID = 0;
    private final String MSG = "My message";
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(ID, "Ivan", Status.ACTIVE);
    }

    @Test
    void createPayment() {
        // Given
        Payment payment = new Payment(user.getId(), AMOUNT, "Payment from user " + user.getName());

        // When
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenReturn(payment);
        Payment paymentResult = paymentService.createPayment(user.getId(), AMOUNT);

        // Then
        Assertions.assertEquals(payment, paymentResult);

        verify(validationService).validateUser(user);
        verify(validationService).validateUserId(user.getId());
        verify(validationService).validateAmount(AMOUNT);
        verify(paymentRepository).save(paymentCaptor.capture());
        Payment paymentSave = paymentCaptor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals(paymentSave.getMessage(), paymentResult.getMessage()),
                () -> Assertions.assertEquals(paymentSave.getUserId(), paymentResult.getUserId()),
                () -> Assertions.assertEquals(paymentSave.getAmount(), paymentResult.getAmount())
        );
    }

    @Test
    void editMessage() {
        // Given
        Payment payment = new Payment(user.getId(), AMOUNT, MSG);

        // When
        when(paymentRepository.editMessage(payment.getPaymentId(), MSG)).thenReturn(payment);
        Payment paymentResult = paymentService.editPaymentMessage(payment.getPaymentId(), MSG);

        // Then
        Assertions.assertEquals(payment, paymentResult);

        verify(validationService).validatePaymentId(payment.getPaymentId());
        verify(validationService).validateMessage(MSG);
    }

    @Test
    void getAllByAmountExceeding() {
        // Given
        Payment paymentNr1 = new Payment(ID + 1, AMOUNT * 1, MSG);
        Payment paymentNr2 = new Payment(ID + 2, AMOUNT * 2, MSG);
        Payment paymentNr3 = new Payment(ID + 3, AMOUNT * 3, MSG);
        Payment paymentNr4 = new Payment(ID + 4, AMOUNT * 4, MSG);
        Payment paymentNr5 = new Payment(ID + 5, AMOUNT * 5, MSG);
        List<Payment> paymentList = Arrays.asList(paymentNr1, paymentNr2, paymentNr3, paymentNr4, paymentNr5);

        // When
        when(paymentRepository.findAll()).thenReturn(paymentList);
        List<Payment> paymentListResult = paymentService.getAllByAmountExceeding(300.0);

        // Then
        Assertions.assertEquals(Arrays.asList(paymentNr4, paymentNr5), paymentListResult);
    }
}
