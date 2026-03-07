package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final String ORDER_HISTORY_VIEW = "OrderHistory";
    private static final String CREATE_ORDER_VIEW = "CreateOrder";
    private static final String ORDER_PAY_VIEW = "OrderPay";
    private static final String ORDER_PAY_RESULT_VIEW = "OrderPayResult";
    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String PAYMENT_ATTRIBUTE = "payment";
    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String BANK_NAME_KEY = "bankName";
    private static final String REFERENCE_CODE_KEY = "referenceCode";

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Autowired
    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        model.addAttribute(ORDER_ATTRIBUTE, new OrderForm());
        return CREATE_ORDER_VIEW;
    }

    @GetMapping("/history")
    public String orderHistoryGet(Model model) {
        setOrdersToModel(model, new ArrayList<>());
        return ORDER_HISTORY_VIEW;
    }

    @PostMapping("/history")
    public String orderHistoryPost(@ModelAttribute OrderForm orderForm, Model model) {
        List<Order> orders = orderService.findAllByAuthor(orderForm.getAuthor());
        setOrdersToModel(model, orders);
        return ORDER_HISTORY_VIEW;
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        model.addAttribute(ORDER_ATTRIBUTE, resolveOrderPayModel(orderId));
        return ORDER_PAY_VIEW;
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(
            @PathVariable String orderId,
            @RequestParam("method") String method,
            @RequestParam(value = "voucherCode", required = false) String voucherCode,
            @RequestParam(value = "bankName", required = false) String bankName,
            @RequestParam(value = "referenceCode", required = false) String referenceCode,
            Model model
    ) {
        Order order = orderService.findById(orderId);
        Map<String, String> paymentData = buildPaymentData(voucherCode, bankName, referenceCode);
        Payment payment = paymentService.addPayment(order, method, paymentData);
        setPaymentToModel(model, payment);
        return ORDER_PAY_RESULT_VIEW;
    }

    private void setOrdersToModel(Model model, List<Order> orders) {
        model.addAttribute(ORDERS_ATTRIBUTE, orders);
    }

    private Object resolveOrderPayModel(String orderId) {
        Order order = orderService.findById(orderId);
        return order != null ? order : new OrderPayViewModel(orderId, "", "");
    }

    private Map<String, String> buildPaymentData(String voucherCode, String bankName, String referenceCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(VOUCHER_CODE_KEY, voucherCode);
        paymentData.put(BANK_NAME_KEY, bankName);
        paymentData.put(REFERENCE_CODE_KEY, referenceCode);
        return paymentData;
    }

    private void setPaymentToModel(Model model, Payment payment) {
        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
    }

    public static class OrderForm {
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

    public static class OrderPayViewModel {
        private final String id;
        private final String author;
        private final String status;

        public OrderPayViewModel(String id, String author, String status) {
            this.id = id;
            this.author = author;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getStatus() {
            return status;
        }
    }
}
