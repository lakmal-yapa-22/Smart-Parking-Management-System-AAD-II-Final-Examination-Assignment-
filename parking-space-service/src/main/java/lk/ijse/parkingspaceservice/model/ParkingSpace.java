package lk.ijse.parkingspaceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {
    private String spaceId;
    private String spaceNumber;
    private String location;
    private String zone;
    private String status;
    private String spaceType;
    private double hourlyRate;
    private String ownerId;
    private String reservedBy;
    private LocalDateTime reservationTime;
    private LocalDateTime occupiedSince;
    private String currentVehicleId;

    public ParkingSpace(String spaceNumber, String location, String zone, String spaceType, double hourlyRate, String ownerId) {
        this.spaceNumber = spaceNumber;
        this.location = location;
        this.zone = zone;
        this.spaceType = spaceType;
        this.hourlyRate = hourlyRate;
        this.ownerId = ownerId;
        this.status = "AVAILABLE";
    }
}