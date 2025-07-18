package ecommerce_website.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RazorpayPaymentService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() {
        try {
            razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
        } catch (RazorpayException e) {
            log.error("Failed to initialize RazorpayClient", e);
            throw new RuntimeException("RazorpayClient init failed", e);
        }
    }

    public Order createOrder(double amount, String currency, String receiptId) {
        try {
            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100)); // Convert to paise
            options.put("currency", currency);
            options.put("receipt", receiptId);
            options.put("payment_capture", 1);

            return razorpayClient.orders.create(options);
        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order", e);
            throw new RuntimeException("Order creation failed", e);
        }
    }

    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        Map<String, String> params = new HashMap<>();
        params.put("razorpay_order_id", razorpayOrderId);
        params.put("razorpay_payment_id", razorpayPaymentId);
        params.put("razorpay_signature", razorpaySignature);

        try {
            JSONObject options = new JSONObject(params);
            return Utils.verifyPaymentSignature(options, razorpaySecret);
        } catch (RazorpayException e) {
            log.error("Signature verification failed", e);
            return false;
        }
    }

    public Payment fetchPaymentDetails(String paymentId) {
        try {
            return razorpayClient.payments.fetch(paymentId);
        } catch (RazorpayException e) {
            log.error("Failed to fetch payment details", e);
            throw new RuntimeException("Payment fetch failed", e);
        }
    }
}
