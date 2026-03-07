package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("pay-1", "VOUCHER_CODE", "SUCCESS", this.paymentData);

        assertEquals("pay-1", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(this.paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentRejectedStatus() {
        Payment payment = new Payment("pay-1", "VOUCHER_CODE", "REJECTED", this.paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("pay-1", "VOUCHER_CODE", "MEOW", this.paymentData));
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("pay-1", "VOUCHER_CODE", "REJECTED", this.paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusInvalidStatus() {
        Payment payment = new Payment("pay-1", "VOUCHER_CODE", "SUCCESS", this.paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}
