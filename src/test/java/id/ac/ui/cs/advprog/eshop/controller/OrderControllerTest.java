package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderController controller;

    @Test
    void createOrderPageShouldReturnViewAndSetOrderForm() {
        Model model = new ExtendedModelMap();

        String viewName = controller.createOrderPage(model);

        assertEquals("CreateOrder", viewName);
        assertNotNull(model.getAttribute("order"));
        assertInstanceOf(OrderController.OrderForm.class, model.getAttribute("order"));
    }

    @Test
    void orderHistoryGetShouldReturnViewAndSetEmptyOrders() {
        Model model = new ExtendedModelMap();

        String viewName = controller.orderHistoryGet(model);

        assertEquals("OrderHistory", viewName);
        Object ordersAttribute = model.getAttribute("orders");
        assertInstanceOf(List.class, ordersAttribute);
        assertEquals(0, ((List<?>) ordersAttribute).size());
    }

    @Test
    void orderHistoryPostShouldUseAuthorAndSetOrders() {
        OrderController.OrderForm orderForm = new OrderController.OrderForm();
        orderForm.setAuthor("alice");
        Order order = org.mockito.Mockito.mock(Order.class);
        List<Order> orders = List.of(order);
        when(orderService.findAllByAuthor("alice")).thenReturn(orders);

        Model model = new ExtendedModelMap();
        String viewName = controller.orderHistoryPost(orderForm, model);

        verify(orderService).findAllByAuthor("alice");
        assertEquals("OrderHistory", viewName);
        assertEquals(orders, model.getAttribute("orders"));
    }

    @Test
    void payOrderPageShouldUseExistingOrderWhenFound() {
        String orderId = "ord-1";
        Order order = org.mockito.Mockito.mock(Order.class);
        when(orderService.findById(orderId)).thenReturn(order);
        Model model = new ExtendedModelMap();

        String viewName = controller.payOrderPage(orderId, model);

        verify(orderService).findById(orderId);
        assertEquals("OrderPay", viewName);
        assertEquals(order, model.getAttribute("order"));
    }

    @Test
    void payOrderPageShouldUseFallbackViewModelWhenOrderNotFound() {
        String orderId = "missing-order";
        when(orderService.findById(orderId)).thenReturn(null);
        Model model = new ExtendedModelMap();

        String viewName = controller.payOrderPage(orderId, model);

        verify(orderService).findById(orderId);
        assertEquals("OrderPay", viewName);
        Object orderAttribute = model.getAttribute("order");
        assertInstanceOf(OrderController.OrderPayViewModel.class, orderAttribute);
        OrderController.OrderPayViewModel viewModel = (OrderController.OrderPayViewModel) orderAttribute;
        assertEquals(orderId, viewModel.getId());
        assertEquals("", viewModel.getAuthor());
        assertEquals("", viewModel.getStatus());
    }

    @Test
    void payOrderPostShouldBuildPaymentDataAndSetPayment() {
        String orderId = "ord-2";
        Order order = org.mockito.Mockito.mock(Order.class);
        Payment payment = new Payment("pay-1", "VOUCHER_CODE", Payment.SUCCESS, new HashMap<>());
        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("VOUCHER_CODE"), anyMap())).thenReturn(payment);

        Model model = new ExtendedModelMap();
        String viewName = controller.payOrderPost(
                orderId,
                "VOUCHER_CODE",
                "ESHOP1234ABC5678",
                "BCA",
                "REF-001",
                model
        );

        ArgumentCaptor<Map<String, String>> paymentDataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(paymentService).addPayment(eq(order), eq("VOUCHER_CODE"), paymentDataCaptor.capture());
        Map<String, String> paymentData = paymentDataCaptor.getValue();
        assertEquals("ESHOP1234ABC5678", paymentData.get("voucherCode"));
        assertEquals("BCA", paymentData.get("bankName"));
        assertEquals("REF-001", paymentData.get("referenceCode"));

        assertEquals("OrderPayResult", viewName);
        assertEquals(payment, model.getAttribute("payment"));
    }

    @Test
    void payOrderPostShouldAllowNullPaymentFields() {
        String orderId = "ord-3";
        Order order = org.mockito.Mockito.mock(Order.class);
        Payment payment = new Payment("pay-2", "BANK_TRANSFER", Payment.SUCCESS, new HashMap<>());
        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), anyMap())).thenReturn(payment);

        Model model = new ExtendedModelMap();
        controller.payOrderPost(orderId, "BANK_TRANSFER", null, null, null, model);

        ArgumentCaptor<Map<String, String>> paymentDataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(paymentService).addPayment(eq(order), eq("BANK_TRANSFER"), paymentDataCaptor.capture());
        Map<String, String> paymentData = paymentDataCaptor.getValue();
        assertNull(paymentData.get("voucherCode"));
        assertNull(paymentData.get("bankName"));
        assertNull(paymentData.get("referenceCode"));
    }

    @Test
    void orderFormGetterSetterShouldWork() {
        OrderController.OrderForm orderForm = new OrderController.OrderForm();

        orderForm.setAuthor("eve");

        assertEquals("eve", orderForm.getAuthor());
    }

    @Test
    void orderPayViewModelGettersShouldWork() {
        OrderController.OrderPayViewModel viewModel = new OrderController.OrderPayViewModel("ord-10", "frank", "WAITING_PAYMENT");

        assertEquals("ord-10", viewModel.getId());
        assertEquals("frank", viewModel.getAuthor());
        assertEquals("WAITING_PAYMENT", viewModel.getStatus());
    }
}
