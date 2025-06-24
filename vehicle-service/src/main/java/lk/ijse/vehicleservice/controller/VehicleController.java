package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.model.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    // Thread-safe list
    private final List<Vehicle> vehicles = new CopyOnWriteArrayList<>();

    // Register a vehicle
    @PostMapping("/register")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Vehicle vehicle) {
        vehicle.setVehicleId(UUID.randomUUID().toString());
        vehicle.setStatus("ACTIVE");
        vehicles.add(vehicle);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicles);
    }

    // Get a vehicle by ID
    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String vehicleId) {
        Optional<Vehicle> vehicle = vehicles.stream()
                .filter(v -> v.getVehicleId().equals(vehicleId))
                .findFirst();

        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get vehicles by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByUser(@PathVariable String userId) {
        List<Vehicle> userVehicles = vehicles.stream()
                .filter(vehicle -> vehicle.getUserId().equals(userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userVehicles);
    }

    // Update a vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String vehicleId, @RequestBody Vehicle updatedVehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleId().equals(vehicleId)) {
                updatedVehicle.setVehicleId(vehicleId);
                vehicles.set(i, updatedVehicle);
                return ResponseEntity.ok(updatedVehicle);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Simulate vehicle entry
    @PostMapping("/{vehicleId}/entry")
    public ResponseEntity<String> simulateEntry(@PathVariable String vehicleId) {
        Optional<Vehicle> vehicle = vehicles.stream()
                .filter(v -> v.getVehicleId().equals(vehicleId))
                .findFirst();

        if (vehicle.isPresent()) {
            vehicle.get().setStatus("PARKED");
            return ResponseEntity.ok("Vehicle " + vehicle.get().getLicensePlate() + " entered parking area");
        }
        return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
    }

    // Simulate vehicle exit
    @PostMapping("/{vehicleId}/exit")
    public ResponseEntity<String> simulateExit(@PathVariable String vehicleId) {
        Optional<Vehicle> vehicle = vehicles.stream()
                .filter(v -> v.getVehicleId().equals(vehicleId))
                .findFirst();

        if (vehicle.isPresent()) {
            vehicle.get().setStatus("ACTIVE");
            return ResponseEntity.ok("Vehicle " + vehicle.get().getLicensePlate() + " exited parking area");
        }
        return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
    }

    // Delete a vehicle
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vehicleId) {
        boolean removed = vehicles.removeIf(vehicle -> vehicle.getVehicleId().equals(vehicleId));
        if (removed) {
            return ResponseEntity.ok("Vehicle deleted successfully");
        }
        return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
    }
}
