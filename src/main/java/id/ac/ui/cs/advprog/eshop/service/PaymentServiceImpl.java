package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    private final Map<String, Order> paymentOrderMap = new HashMap<>();

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = buildNewPayment(method, paymentData);
        paymentOrderMap.put(payment.getId(), order);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        syncOrderStatus(payment, status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.getPayment(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

    private Payment buildNewPayment(String method, Map<String, String> paymentData) {
        return new Payment(
                UUID.randomUUID().toString(),
                method,
                Payment.REJECTED,
                paymentData
        );
    }

    private void syncOrderStatus(Payment payment, String paymentStatus) {
        Order order = paymentOrderMap.get(payment.getId());
        if (order == null) {
            return;
        }

        if (Payment.SUCCESS.equals(paymentStatus)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (Payment.REJECTED.equals(paymentStatus)) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
    }
}
