package lk.ijse.vehicleservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String vehicleId;
    private String userId;
    private String licensePlate;
    private String vehicleType; // CAR, MOTORCYCLE, VAN
    private String brand;
    private String model;
    private String color;
    private String status; // ACTIVE, INACTIVE, PARKED

    // Custom constructor for your specific use case
    public Vehicle(String vehicleId, String userId, String licensePlate,
                   String vehicleType, String brand, String model, String color) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.status = "ACTIVE";
    }
}