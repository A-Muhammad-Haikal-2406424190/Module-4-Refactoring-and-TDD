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
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String BANK_NAME_KEY = "bankName";
    private static final String REFERENCE_CODE_KEY = "referenceCode";
    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int VOUCHER_LENGTH = 16;
    private static final int VOUCHER_DIGIT_COUNT = 8;

    @Autowired
    private PaymentRepository paymentRepository;
    private final Map<String, Order> paymentOrderMap = new HashMap<>();

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = buildNewPayment(method, paymentData);
        if (METHOD_VOUCHER_CODE.equals(method) && isVoucherCodeValid(paymentData)) {
            payment.setStatus(Payment.SUCCESS);
        }
        if (METHOD_BANK_TRANSFER.equals(method) && isBankTransferValid(paymentData)) {
            payment.setStatus(Payment.SUCCESS);
        }
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

    private boolean isVoucherCodeValid(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }
        String voucherCode = paymentData.get(VOUCHER_CODE_KEY);
        return hasVoucherCodeFormat(voucherCode);
    }

    private boolean hasVoucherCodeFormat(String voucherCode) {
        if (voucherCode == null) {
            return false;
        }
        if (voucherCode.length() != VOUCHER_LENGTH) {
            return false;
        }
        if (!voucherCode.startsWith(VOUCHER_PREFIX)) {
            return false;
        }
        return countDigits(voucherCode) == VOUCHER_DIGIT_COUNT;
    }

    private int countDigits(String value) {
        int digitCount = 0;
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount += 1;
            }
        }
        return digitCount;
    }

    private boolean isBankTransferValid(Map<String, String> paymentData) {
        return hasRequiredNonBlankFields(paymentData, BANK_NAME_KEY, REFERENCE_CODE_KEY);
    }

    private boolean hasRequiredNonBlankFields(Map<String, String> data, String... keys) {
        if (data == null) {
            return false;
        }
        for (String key : keys) {
            if (!isNotBlank(data.get(key))) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
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
