package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    List<Payment> payments;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payments = new ArrayList<>();
        payments.add(new Payment("pay-1", "VOUCHER_CODE", "SUCCESS", paymentData));
        payments.add(new Payment("pay-2", "BANK_TRANSFER", "REJECTED", paymentData));
    }

    @Test
    void testAddPayment() {
        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals(paymentData, result.getPaymentData());
    }

    @Test
    void testAddPaymentVoucherCodeValidShouldBeSuccess() {
        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInvalidShouldBeRejected() {
        Map<String, String> invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "ESHOPABC5678");
        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", invalidVoucherData);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatus() {
        Payment payment = payments.get(1);
        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).getPayment(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfNotFound() {
        doReturn(null).when(paymentRepository).getPayment("pay-404");

        Payment result = paymentService.getPayment("pay-404");
        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).getAllPayments();

        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size());
    }
}
