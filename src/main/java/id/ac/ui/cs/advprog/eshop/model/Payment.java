package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class Payment {
    public static final String SUCCESS = "SUCCESS";
    public static final String REJECTED = "REJECTED";

    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment() {
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidStatus(String status) {
        return SUCCESS.equals(status) || REJECTED.equals(status);
    }
}
