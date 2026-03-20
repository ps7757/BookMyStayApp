/**
 * Book My Stay App
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Demonstrates safe booking confirmation using FIFO queue processing,
 * unique room allocation, and synchronized inventory updates.
 *
 * Prevents double-booking using Set and ensures consistency.
 *
 * @author YourName
 * @version 6.0
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

// -------------------- Booking Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Inventory Service --------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> allocationMap = new HashMap<>();

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process booking requests
    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("\n--- Processing Booking Requests ---\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String roomType = r.getRoomType();

            System.out.println("Processing request for " + r.getGuestName());

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Store globally
                allocatedRoomIds.add(roomId);

                // Map to room type
                allocationMap.putIfAbsent(roomType, new HashSet<>());
                allocationMap.get(roomType).add(roomId);

                // Update inventory immediately
                inventory.decrement(roomType);

                // Confirm booking
                System.out.println("Booking CONFIRMED for " + r.getGuestName());
                System.out.println("Room Type: " + roomType + " | Room ID: " + roomId);
            } else {
                System.out.println("Booking FAILED for " + r.getGuestName() + " (No availability)");
            }

            System.out.println("-----------------------------------");
        }
    }

    // Display allocated rooms
    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (String type : allocationMap.keySet()) {
            System.out.println(type + " -> " + allocationMap.get(type));
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("=====================================");

        // Initialize components
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        bookingService.processBookings(queue, inventory);

        // Show final allocations
        bookingService.displayAllocations();

        // Show updated inventory
        inventory.displayInventory();

        System.out.println("\nAll bookings processed.");
    }
}