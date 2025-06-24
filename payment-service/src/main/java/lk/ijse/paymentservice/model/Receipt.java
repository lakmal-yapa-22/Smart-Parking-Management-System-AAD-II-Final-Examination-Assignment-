package lk.ijse.paymentservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class Receipt {
    private String receiptNumber;
    private String paymentId;
    private String businessName;
    private String businessAddress;
    private String businessPhone;
    private LocalDateTime issueDateTime;

    private String customerId;
    private String customerName;

    private String spaceId;
    private String spaceNumber;
    private String location;
    private String vehicleId;
    private String licensePlate;

    private LocalDateTime parkingStartTime;
    private LocalDateTime parkingEndTime;
    private long durationHours;
    private long durationMinutes;

    private double hourlyRate;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;
    private String paymentMethod;
    private String transactionReference;
    private String currency;

    private String termsAndConditions;
    private String thankYouMessage;

    public Receipt() {
        this.businessName = "Lakmal Smart Parking Solutions";
        this.businessAddress = "No. 22, Green Park Road, Monaragala, Sri Lanka";
        this.businessPhone = "+94 55 223 4455";
        this.currency = "LKR";
        this.termsAndConditions = "Once paid, parking fees are non-refundable. Please park responsibly.";
        this.thankYouMessage = "We appreciate your trust in Lakmal Smart Parking!";
    }

}