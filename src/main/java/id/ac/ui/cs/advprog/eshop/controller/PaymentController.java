package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private static final String PAYMENT_ATTRIBUTE = "payment";
    private static final String PAYMENTS_ATTRIBUTE = "payments";
    private static final String PAYMENT_DETAIL_VIEW = "PaymentDetail";
    private static final String PAYMENT_ADMIN_LIST_VIEW = "PaymentAdminList";
    private static final String PAYMENT_ADMIN_DETAIL_VIEW = "PaymentAdminDetail";
    private static final String PAYMENT_ADMIN_DETAIL_REDIRECT = "redirect:/payment/admin/detail/";

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailPage(Model model) {
        addPaymentToModel(model, new Payment());
        return PAYMENT_DETAIL_VIEW;
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailByIdPage(@PathVariable String paymentId, Model model) {
        addPaymentToModel(model, getPaymentOrEmpty(paymentId));
        return PAYMENT_DETAIL_VIEW;
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage(Model model) {
        addPaymentsToModel(model);
        return PAYMENT_ADMIN_LIST_VIEW;
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable String paymentId, Model model) {
        addPaymentToModel(model, getPaymentOrEmpty(paymentId));
        return PAYMENT_ADMIN_DETAIL_VIEW;
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setStatus(
            @PathVariable String paymentId,
            @ModelAttribute PaymentStatusForm paymentStatusForm
    ) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, paymentStatusForm.getStatus());
        }
        return PAYMENT_ADMIN_DETAIL_REDIRECT + paymentId;
    }

    private void addPaymentToModel(Model model, Payment payment) {
        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
    }

    private Payment getPaymentOrEmpty(String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            return payment;
        }
        return new Payment(paymentId, "", Payment.REJECTED, new HashMap<>());
    }

    private void addPaymentsToModel(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute(PAYMENTS_ATTRIBUTE, payments != null ? payments : new ArrayList<>());
    }

    public static class PaymentStatusForm {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
