package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailPage() {
        return "PaymentDetail";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailByIdPage(@PathVariable String paymentId) {
        return "PaymentDetail";
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage() {
        return "PaymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable String paymentId) {
        return "PaymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setStatus(
            @PathVariable String paymentId,
            @RequestParam("status") String status
    ) {
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}
