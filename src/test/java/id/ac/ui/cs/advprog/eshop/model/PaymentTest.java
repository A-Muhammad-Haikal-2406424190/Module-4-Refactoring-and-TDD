package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PaymentTest {

    Payment payment;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        this.payment.setId("pay-1");
        this.payment.setMethod("VOUCHER_CODE");
        this.payment.setStatus("SUCCESS");
        this.payment.setPaymentData(paymentData);
    }

    @Test
    void getId() {
        assertEquals("pay-1", this.payment.getId());
    }

    @Test
    void getMethod() {
        assertEquals("VOUCHER_CODE", this.payment.getMethod());
    }

    @Test
    void getStatus() {
        assertEquals("SUCCESS", this.payment.getStatus());
    }

    @Test
    void getPaymentData() {
        assertSame(paymentData, this.payment.getPaymentData());
        assertEquals("ESHOP1234ABC5678", this.payment.getPaymentData().get("voucherCode"));
    }
}
