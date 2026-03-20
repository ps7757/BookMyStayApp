/**
 * Book My Stay App
 * Use Case 7: Add-On Service Selection
 *
 * Demonstrates how optional services can be attached to reservations
 * without modifying core booking or inventory logic.
 *
 * Uses Map<String, List<Service>> to model one-to-many relationship.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// -------------------- Service Model --------------------
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() { return name; }
    public double getCost() { return cost; }

    public void display() {
        System.out.println(name + " - $" + cost);
    }
}

// -------------------- Add-On Service Manager --------------------
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service '" + service.getName() +
                "' to reservation " + reservationId);
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        for (Service s : getServices(reservationId)) {
            total += s.getCost();
        }

        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        System.out.println("\n--- Add-On Services for Reservation: " + reservationId + " ---");

        List<Service> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (Service s : services) {
            s.display();
        }

        System.out.println("Total Add-On Cost: $" + calculateTotalCost(reservationId));
    }
}

// -------------------- Main Class --------------------
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v7.0");
        System.out.println("=====================================");

        // Simulated reservation ID (from previous use case)
        String reservationId = "RES123";

        // Initialize service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 10);
        Service wifi = new Service("WiFi", 5);
        Service airportPickup = new Service("Airport Pickup", 20);

        // Guest selects services
        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, wifi);
        serviceManager.addService(reservationId, airportPickup);

        // Display selected services
        serviceManager.displayServices(reservationId);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}