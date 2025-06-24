package lk.ijse.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String paymentId;
    private String userId;
    private String spaceId;
    private String vehicleId;
    private double amount;
    private String currency;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET, CASH
    private String transactionStatus; // PENDING, COMPLETED, FAILED, REFUNDED
    private LocalDateTime paymentDateTime;
    private LocalDateTime parkingStartTime;
    private LocalDateTime parkingEndTime;
    private long parkingDurationMinutes;
    private String description;
    private String receiptNumber;

    private String cardHolderName;
    private String expiryDate;
    private String bankResponse;
    private String transactionReference;

    @Setter(lombok.AccessLevel.NONE)
    private String cardNumber;

    public Payment(String userId, String spaceId, String vehicleId, double amount, String paymentMethod) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.vehicleId = vehicleId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.currency = "LKR";
        this.transactionStatus = "PENDING";
        this.paymentDateTime = LocalDateTime.now();
    }


    public void setCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.length() >= 4) {
            this.cardNumber = "****-****-****-**" + cardNumber.substring(cardNumber.length() - 4);
        } else {
            this.cardNumber = cardNumber;
        }
    }
}