package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController controller;

    @Test
    void paymentDetailPageShouldSetNewPaymentAndReturnView() {
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentDetailPage(model);

        assertEquals("PaymentDetail", viewName);
        assertNotNull(model.getAttribute("payment"));
        assertInstanceOf(Payment.class, model.getAttribute("payment"));
    }

    @Test
    void paymentDetailByIdPageShouldUseExistingPayment() {
        String paymentId = "pay-1";
        Payment payment = new Payment(paymentId, "VOUCHER_CODE", Payment.SUCCESS, new HashMap<>());
        when(paymentService.getPayment(paymentId)).thenReturn(payment);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentDetailByIdPage(paymentId, model);

        verify(paymentService).getPayment(paymentId);
        assertEquals("PaymentDetail", viewName);
        assertEquals(payment, model.getAttribute("payment"));
    }

    @Test
    void paymentDetailByIdPageShouldUseFallbackPaymentWhenMissing() {
        String paymentId = "missing";
        when(paymentService.getPayment(paymentId)).thenReturn(null);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentDetailByIdPage(paymentId, model);

        verify(paymentService).getPayment(paymentId);
        assertEquals("PaymentDetail", viewName);
        Object paymentObj = model.getAttribute("payment");
        assertInstanceOf(Payment.class, paymentObj);
        Payment fallback = (Payment) paymentObj;
        assertEquals(paymentId, fallback.getId());
        assertEquals("", fallback.getMethod());
        assertEquals(Payment.REJECTED, fallback.getStatus());
        assertNotNull(fallback.getPaymentData());
    }

    @Test
    void paymentAdminListPageShouldSetPaymentsWhenServiceReturnsList() {
        Payment payment = new Payment("pay-2", "BANK_TRANSFER", Payment.SUCCESS, new HashMap<>());
        List<Payment> payments = List.of(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentAdminListPage(model);

        verify(paymentService).getAllPayments();
        assertEquals("PaymentAdminList", viewName);
        assertEquals(payments, model.getAttribute("payments"));
    }

    @Test
    void paymentAdminListPageShouldSetEmptyListWhenServiceReturnsNull() {
        when(paymentService.getAllPayments()).thenReturn(null);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentAdminListPage(model);

        verify(paymentService).getAllPayments();
        assertEquals("PaymentAdminList", viewName);
        Object paymentsObj = model.getAttribute("payments");
        assertInstanceOf(List.class, paymentsObj);
        assertEquals(0, ((List<?>) paymentsObj).size());
    }

    @Test
    void paymentAdminDetailPageShouldSetPaymentAndReturnView() {
        String paymentId = "pay-3";
        Payment payment = new Payment(paymentId, "VOUCHER_CODE", Payment.SUCCESS, new HashMap<>());
        when(paymentService.getPayment(paymentId)).thenReturn(payment);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentAdminDetailPage(paymentId, model);

        verify(paymentService).getPayment(paymentId);
        assertEquals("PaymentAdminDetail", viewName);
        assertEquals(payment, model.getAttribute("payment"));
    }

    @Test
    void paymentAdminDetailPageShouldSetFallbackPaymentWhenMissing() {
        String paymentId = "missing-admin-payment";
        when(paymentService.getPayment(paymentId)).thenReturn(null);
        Model model = new ExtendedModelMap();

        String viewName = controller.paymentAdminDetailPage(paymentId, model);

        verify(paymentService).getPayment(paymentId);
        assertEquals("PaymentAdminDetail", viewName);
        Payment fallback = (Payment) model.getAttribute("payment");
        assertNotNull(fallback);
        assertEquals(paymentId, fallback.getId());
        assertEquals(Payment.REJECTED, fallback.getStatus());
    }

    @Test
    void setStatusShouldUpdateStatusWhenPaymentExists() {
        String paymentId = "pay-4";
        Payment payment = new Payment(paymentId, "BANK_TRANSFER", Payment.REJECTED, new HashMap<>());
        when(paymentService.getPayment(paymentId)).thenReturn(payment);
        PaymentController.PaymentStatusForm form = new PaymentController.PaymentStatusForm();
        form.setStatus(Payment.SUCCESS);

        String redirect = controller.setStatus(paymentId, form);

        verify(paymentService).getPayment(paymentId);
        verify(paymentService).setStatus(payment, Payment.SUCCESS);
        assertEquals("redirect:/payment/admin/detail/" + paymentId, redirect);
    }

    @Test
    void setStatusShouldNotUpdateStatusWhenPaymentDoesNotExist() {
        String paymentId = "missing-pay";
        when(paymentService.getPayment(paymentId)).thenReturn(null);
        PaymentController.PaymentStatusForm form = new PaymentController.PaymentStatusForm();
        form.setStatus(Payment.SUCCESS);

        String redirect = controller.setStatus(paymentId, form);

        verify(paymentService).getPayment(paymentId);
        verify(paymentService, never()).setStatus(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
        assertEquals("redirect:/payment/admin/detail/" + paymentId, redirect);
    }

    @Test
    void paymentStatusFormGetterSetterShouldWork() {
        PaymentController.PaymentStatusForm form = new PaymentController.PaymentStatusForm();

        form.setStatus(Payment.SUCCESS);

        assertEquals(Payment.SUCCESS, form.getStatus());
    }
}
