package lk.ijse.parkingspaceservice.dto;

public class ParkingStatsDTO {
    private long total;
    private long available;
    private long occupied;
    private long reserved;
    private long maintenance;
    private double occupancyRate;

    public ParkingStatsDTO(long total, long available, long occupied, long reserved, long maintenance, double occupancyRate) {
        this.total = total;
        this.available = available;
        this.occupied = occupied;
        this.reserved = reserved;
        this.maintenance = maintenance;
        this.occupancyRate = occupancyRate;
    }

    // Getters
    public long getTotal() {
        return total;
    }

    public long getAvailable() {
        return available;
    }

    public long getOccupied() {
        return occupied;
    }

    public long getReserved() {
        return reserved;
    }

    public long getMaintenance() {
        return maintenance;
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }
}
