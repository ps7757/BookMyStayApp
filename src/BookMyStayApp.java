/**
 * Book My Stay App
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Demonstrates how multiple threads interact with shared resources
 * and how synchronization prevents race conditions and double booking.
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// -------------------- Shared Inventory --------------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    // Thread-safe booking method
    public synchronized boolean allocateRoom(String roomType, String guestName) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            System.out.println("Booking SUCCESS for " + guestName +
                    " | Room Type: " + roomType +
                    " | Remaining: " + (available - 1));
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName +
                    " | No availability for " + roomType);
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Final Inventory ---");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}

// -------------------- Booking Task (Thread) --------------------
class BookingTask implements Runnable {

    private Reservation reservation;
    private RoomInventory inventory;

    public BookingTask(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        inventory.allocateRoom(
                reservation.getRoomType(),
                reservation.getGuestName()
        );
    }
}

// -------------------- Main Class --------------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v11.0");
        System.out.println("=====================================");

        RoomInventory inventory = new RoomInventory();

        // Simulate multiple concurrent booking requests
        List<Thread> threads = new ArrayList<>();

        Reservation[] reservations = {
                new Reservation("Alice", "Single Room"),
                new Reservation("Bob", "Single Room"),
                new Reservation("Charlie", "Single Room"), // may fail due to limit
                new Reservation("David", "Double Room"),
                new Reservation("Eve", "Suite Room")
        };

        // Create and start threads
        for (Reservation r : reservations) {
            Thread t = new Thread(new BookingTask(r, inventory));
            threads.add(t);
            t.start();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        // Display final inventory
        inventory.displayInventory();

        System.out.println("\nConcurrent booking simulation completed safely.");
    }
}