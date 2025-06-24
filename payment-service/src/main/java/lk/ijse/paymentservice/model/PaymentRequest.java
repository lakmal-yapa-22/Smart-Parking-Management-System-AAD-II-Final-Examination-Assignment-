package lk.ijse.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String userId;
    private String spaceId;
    private String vehicleId;
    private double amount;
    private String paymentMethod;
    private LocalDateTime parkingStartTime;
    private LocalDateTime parkingEndTime;
    private String description;

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public PaymentRequest(String userId, String spaceId, String vehicleId, double amount, String paymentMethod) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.vehicleId = vehicleId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}