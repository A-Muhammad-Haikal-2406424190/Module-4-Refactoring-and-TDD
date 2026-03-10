package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("pay-1", "VOUCHER_CODE", "SUCCESS", paymentData1);
        payments.add(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "TRX-0001");
        Payment payment2 = new Payment("pay-2", "BANK_TRANSFER", "REJECTED", paymentData2);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
        assertNotNull(findResult);
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(1);
        paymentRepository.save(payment);

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("bankName", "BCA");
        updatedData.put("referenceCode", "TRX-UPDATED");
        Payment updatedPayment = new Payment(payment.getId(), "BANK_TRANSFER", "SUCCESS", updatedData);

        Payment result = paymentRepository.save(updatedPayment);
        Payment findResult = paymentRepository.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
        assertNotNull(findResult);
        assertEquals("SUCCESS", findResult.getStatus());
        assertEquals("TRX-UPDATED", findResult.getPaymentData().get("referenceCode"));
    }

    @Test
    void testGetPaymentIfIdFound() {
        paymentRepository.save(payments.get(0));
        paymentRepository.save(payments.get(1));

        Payment findResult = paymentRepository.getPayment("pay-2");
        assertNotNull(findResult);
        assertEquals("pay-2", findResult.getId());
        assertEquals("BANK_TRANSFER", findResult.getMethod());
    }

    @Test
    void testGetPaymentIfIdNotFound() {
        paymentRepository.save(payments.get(0));

        Payment findResult = paymentRepository.getPayment("pay-404");
        assertNull(findResult);
    }

    @Test
    void testGetAllPayments() {
        paymentRepository.save(payments.get(0));
        paymentRepository.save(payments.get(1));

        List<Payment> result = paymentRepository.getAllPayments();
        assertEquals(2, result.size());
        assertEquals("pay-1", result.get(0).getId());
        assertEquals("pay-2", result.get(1).getId());
    }
}
