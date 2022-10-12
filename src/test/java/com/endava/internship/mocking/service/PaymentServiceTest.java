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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private static final Double AMOUNT = 100.0;
    private static final Integer ID = 0;
    private static final String MSG = "My message";
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(ID, "Ivan", Status.ACTIVE);
    }

    @Test
    void createPayment() {
        Payment payment = new Payment(user.getId(), AMOUNT, "Payment from user " + user.getName());
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenReturn(payment);

        Payment paymentResult = paymentService.createPayment(user.getId(), AMOUNT);

        verify(validationService).validateUser(user);
        verify(validationService).validateUserId(user.getId());
        verify(validationService).validateAmount(AMOUNT);
        verify(paymentRepository).save(paymentCaptor.capture());
        Payment paymentSave = paymentCaptor.getValue();
        Assertions.assertAll(
                () -> assertThat(paymentResult.getMessage()).isEqualTo(paymentSave.getMessage()),
                () -> assertThat(paymentResult.getUserId()).isEqualTo(paymentSave.getUserId()),
                () -> assertThat(paymentResult.getAmount()).isEqualTo(paymentSave.getAmount())
        );
    }

    @Test
    void editMessage() {
        Payment payment = new Payment(user.getId(), AMOUNT, MSG);
        when(paymentRepository.editMessage(payment.getPaymentId(), MSG)).thenReturn(payment);

        Payment paymentResult = paymentService.editPaymentMessage(payment.getPaymentId(), MSG);

        assertThat(paymentResult).isEqualTo(payment);

        verify(validationService).validatePaymentId(payment.getPaymentId());
        verify(validationService).validateMessage(MSG);
    }

    @Test
    void getAllByAmountExceeding() {
        Payment paymentNr1 = new Payment(ID + 1, AMOUNT * 1, MSG);
        Payment paymentNr2 = new Payment(ID + 2, AMOUNT * 2, MSG);
        Payment paymentNr3 = new Payment(ID + 3, AMOUNT * 3, MSG);
        Payment paymentNr4 = new Payment(ID + 4, AMOUNT * 4, MSG);
        Payment paymentNr5 = new Payment(ID + 5, AMOUNT * 5, MSG);
        List<Payment> paymentList = Arrays.asList(paymentNr1, paymentNr2, paymentNr3, paymentNr4, paymentNr5);
        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<Payment> paymentListResult = paymentService.getAllByAmountExceeding(300.0);

        assertThat(paymentListResult).isEqualTo(Arrays.asList(paymentNr4, paymentNr5));
    }
}
