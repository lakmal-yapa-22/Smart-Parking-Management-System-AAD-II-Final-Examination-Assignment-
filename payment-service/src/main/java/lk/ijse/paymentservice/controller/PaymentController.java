package lk.ijse.paymentservice.controller;

import lk.ijse.paymentservice.model.Payment;
import lk.ijse.paymentservice.model.PaymentRequest;
import lk.ijse.paymentservice.model.Receipt;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private List<Payment> payments = new ArrayList<>();
    private List<Receipt> receipts = new ArrayList<>();
    private Random random = new Random();

    // Process a new payment
    @PostMapping("/process")
    public Payment processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Create payment object
        Payment payment = new Payment(
                paymentRequest.getUserId(),
                paymentRequest.getSpaceId(),
                paymentRequest.getVehicleId(),
                paymentRequest.getAmount(),
                paymentRequest.getPaymentMethod()
        );

        // Set payment ID and receipt number
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setReceiptNumber(generateReceiptNumber());

        // Set parking duration details
        if (paymentRequest.getParkingStartTime() != null && paymentRequest.getParkingEndTime() != null) {
            payment.setParkingStartTime(paymentRequest.getParkingStartTime());
            payment.setParkingEndTime(paymentRequest.getParkingEndTime());

            Duration duration = Duration.between(paymentRequest.getParkingStartTime(), paymentRequest.getParkingEndTime());
            payment.setParkingDurationMinutes(duration.toMinutes());
        }

        // Set card details (securely)
        if (paymentRequest.getCardNumber() != null) {
            payment.setCardNumber(paymentRequest.getCardNumber()); // Will be masked automatically
            payment.setCardHolderName(paymentRequest.getCardHolderName());
            payment.setExpiryDate(paymentRequest.getExpiryDate());
            // CVV is not stored for security
        }

        payment.setDescription(paymentRequest.getDescription());

        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentGateway(payment, paymentRequest);

        if (paymentSuccess) {
            payment.setTransactionStatus("COMPLETED");
            payment.setBankResponse("Payment approved - Transaction successful");
            payment.setTransactionReference(generateTransactionReference());
        } else {
            payment.setTransactionStatus("FAILED");
            payment.setBankResponse("Payment declined - Insufficient funds or invalid card details");
        }

        payments.add(payment);

        // Generate digital receipt if payment is successful
        if (paymentSuccess) {
            generateDigitalReceipt(payment);
        }

        return payment;
    }

    @PostMapping("/calculate-fee")
    public Object calculateParkingFee(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam double hourlyRate) {

        Duration duration = Duration.between(startTime, endTime);
        long totalMinutes = duration.toMinutes();
        double hours = Math.ceil(totalMinutes / 60.0); // Round up to next hour

        double subtotal = hours * hourlyRate;
        double taxRate = 0.15; // 15% tax
        double taxAmount = subtotal * taxRate;
        double totalAmount = subtotal + taxAmount;

        return new Object() {
            public final long durationMinutes = totalMinutes;
            public final double durationHours = hours;
            public final double parkingFee = subtotal;
            public final double tax = taxAmount;
            public final double total = totalAmount;
            public final String formattedDuration = String.format("%d hours %d minutes",
                    totalMinutes / 60, totalMinutes % 60);
        };
    }
    // Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return payments;
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public Payment getPayment(@PathVariable String paymentId) {
        return payments.stream()
                .filter(payment -> payment.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    // Get payments by user
    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentsByUser(@PathVariable String userId) {
        return payments.stream()
                .filter(payment -> payment.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public List<Payment> getPaymentsByStatus(@PathVariable String status) {
        return payments.stream()
                .filter(payment -> payment.getTransactionStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    // Refund a payment
    @PostMapping("/{paymentId}/refund")
    public String refundPayment(@PathVariable String paymentId, @RequestParam(required = false) String reason) {
        Payment payment = getPayment(paymentId);
        if (payment == null) {
            return "Payment not found";
        }

        if (!"COMPLETED".equals(payment.getTransactionStatus())) {
            return "Only completed payments can be refunded";
        }

        // Simulate refund processing
        boolean refundSuccess = simulateRefundProcess(payment);

        if (refundSuccess) {
            payment.setTransactionStatus("REFUNDED");
            payment.setBankResponse("Refund processed successfully");

            String refundReason = reason != null ? reason : "Customer requested refund";
            return "Refund of " + payment.getCurrency() + " " + payment.getAmount() +
                    " processed successfully for payment " + paymentId + ". Reason: " + refundReason;
        } else {
            return "Refund processing failed. Please try again later.";
        }
    }

    // Get digital receipt
    @GetMapping("/{paymentId}/receipt")
    public Receipt getReceipt(@PathVariable String paymentId) {
        return receipts.stream()
                .filter(receipt -> receipt.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    // Get receipt by receipt number
    @GetMapping("/receipt/{receiptNumber}")
    public Receipt getReceiptByNumber(@PathVariable String receiptNumber) {
        return receipts.stream()
                .filter(receipt -> receipt.getReceiptNumber().equals(receiptNumber))
                .findFirst()
                .orElse(null);
    }

    // Get all receipts
    @GetMapping("/receipts")
    public List<Receipt> getAllReceipts() {
        return receipts;
    }

    // Validate payment card (mock validation)
    @PostMapping("/validate-card")
    public Object validatePaymentCard(@RequestParam String cardNumber,
                                      @RequestParam String expiryDate,
                                      @RequestParam String cvv) {
        boolean isValid = simulateCardValidation(cardNumber, expiryDate, cvv);

        return new Object() {
            public final boolean valid = isValid;
            public final String message = isValid ? "Card is valid" : "Invalid card details";
            public final String cardType = detectCardType(cardNumber);
        };
    }

    // Get payment statistics
    @GetMapping("/stats")
    public Object getPaymentStats() {
        long totalPayments = payments.size();
        long completedPayments = payments.stream().filter(p -> "COMPLETED".equals(p.getTransactionStatus())).count();
        long failedPayments = payments.stream().filter(p -> "FAILED".equals(p.getTransactionStatus())).count();
        long refundedPayments = payments.stream().filter(p -> "REFUNDED".equals(p.getTransactionStatus())).count();

        double totalRevenue = payments.stream()
                .filter(p -> "COMPLETED".equals(p.getTransactionStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        double refundedAmount = payments.stream()
                .filter(p -> "REFUNDED".equals(p.getTransactionStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        return new Object() {
            public final long total = totalPayments;
            public final long completed = completedPayments;
            public final long failed = failedPayments;
            public final long refunded = refundedPayments;
            public final double revenue = totalRevenue;
            public final double refunds = refundedAmount;
            public final double netRevenue = totalRevenue - refundedAmount;
            public final double successRate = totalPayments > 0 ? (double) completedPayments / totalPayments * 100 : 0;
        };
    }

    // Private helper methods

    private boolean simulatePaymentGateway(Payment payment, PaymentRequest request) {
        // Simulate various payment scenarios
        if (request.getCardNumber() != null) {
            if (!isValidCardNumber(request.getCardNumber())) {
                return false;
            }

            if (!isValidExpiryDate(request.getExpiryDate())) {
                return false;
            }


            return random.nextDouble() > 0.1;
        }


        return random.nextDouble() > 0.05;
    }

    private boolean simulateRefundProcess(Payment payment) {
        return random.nextDouble() > 0.05;
    }

    private boolean simulateCardValidation(String cardNumber, String expiryDate, String cvv) {
        return isValidCardNumber(cardNumber) && isValidExpiryDate(expiryDate) && isValidCvv(cvv);
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Simple validation: check if it's numeric and has appropriate length
        if (cardNumber == null) return false;
        String cleaned = cardNumber.replaceAll("[\\s-]", "");
        return cleaned.matches("\\d{13,19}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null) return false;
        // Expected format: MM/YY or MM/YYYY
        return expiryDate.matches("(0[1-9]|1[0-2])/\\d{2,4}");
    }

    private boolean isValidCvv(String cvv) {
        if (cvv == null) return false;
        return cvv.matches("\\d{3,4}");
    }

    private String detectCardType(String cardNumber) {
        if (cardNumber == null) return "UNKNOWN";
        String cleaned = cardNumber.replaceAll("[\\s-]", "");

        if (cleaned.startsWith("4")) return "VISA";
        if (cleaned.startsWith("5")) return "MASTERCARD";
        if (cleaned.startsWith("3")) return "AMERICAN_EXPRESS";
        return "OTHER";
    }

    private String generateReceiptNumber() {
        return "RCP" + System.currentTimeMillis() + String.format("%03d", random.nextInt(1000));
    }

    private String generateTransactionReference() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private void generateDigitalReceipt(Payment payment) {
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(payment.getReceiptNumber());
        receipt.setPaymentId(payment.getPaymentId());
        receipt.setIssueDateTime(LocalDateTime.now());


        receipt.setCustomerId(payment.getUserId());
        receipt.setCustomerName("Customer " + payment.getUserId().substring(0, 8));


        receipt.setSpaceId(payment.getSpaceId());
        receipt.setSpaceNumber("SPACE-" + payment.getSpaceId().substring(0, 8));
        receipt.setLocation("Mock Location");
        receipt.setVehicleId(payment.getVehicleId());
        receipt.setLicensePlate("ABC-" + random.nextInt(9999));

        // Set time details
        receipt.setParkingStartTime(payment.getParkingStartTime());
        receipt.setParkingEndTime(payment.getParkingEndTime());
        if (payment.getParkingDurationMinutes() > 0) {
            receipt.setDurationHours(payment.getParkingDurationMinutes() / 60);
            receipt.setDurationMinutes(payment.getParkingDurationMinutes() % 60);
        }

        // Set payment details
        double subtotal = payment.getAmount() / 1.15;
        double taxAmount = payment.getAmount() - subtotal;

        receipt.setHourlyRate(100.0);
        receipt.setSubtotal(subtotal);
        receipt.setTaxAmount(taxAmount);
        receipt.setTotalAmount(payment.getAmount());
        receipt.setPaymentMethod(payment.getPaymentMethod());
        receipt.setTransactionReference(payment.getTransactionReference());

        receipts.add(receipt);
    }
}