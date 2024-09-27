package com.staygo.service.user_ser;

import com.staygo.enity.user.Payment;
import com.staygo.repository.user_repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void addedPayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
