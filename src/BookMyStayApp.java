/**
 * Book My Stay App
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Demonstrates safe cancellation using Stack (LIFO) to rollback room IDs,
 * restore inventory, and maintain consistent system state.
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean cancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return cancelled; }

    public void cancel() {
        this.cancelled = true;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Cancelled: " + cancelled);
    }
}

// -------------------- Inventory --------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    private Map<String, Reservation> reservations = new HashMap<>();
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track released room IDs (rollback)
    private Stack<String> releasedRoomStack = new Stack<>();

    // Add booking
    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
        allocatedRoomIds.add(r.getRoomId());
    }

    // Cancel booking
    public void cancelReservation(String reservationId, RoomInventory inventory) {

        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation not found.");
            return;
        }

        Reservation r = reservations.get(reservationId);

        if (r.isCancelled()) {
            System.out.println("Cancellation FAILED: Already cancelled.");
            return;
        }

        // Mark as cancelled
        r.cancel();

        // Release room ID
        String roomId = r.getRoomId();
        releasedRoomStack.push(roomId);
        allocatedRoomIds.remove(roomId);

        // Restore inventory
        inventory.increment(r.getRoomType());

        System.out.println("Cancellation SUCCESS for Reservation ID: " + reservationId);
        System.out.println("Room ID released: " + roomId);
    }

    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation r : reservations.values()) {
            r.display();
        }
    }

    public void displayReleasedRooms() {
        System.out.println("\n--- Released Room IDs (LIFO Order) ---");
        for (String id : releasedRoomStack) {
            System.out.println(id);
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v10.0");
        System.out.println("=====================================");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();

        // Create sample reservations (simulating confirmed bookings)
        Reservation r1 = new Reservation("RES1", "Alice", "Single Room", "SI-1001");
        Reservation r2 = new Reservation("RES2", "Bob", "Double Room", "DO-2001");

        bookingService.addReservation(r1);
        bookingService.addReservation(r2);

        System.out.println("\nBefore Cancellation:");
        bookingService.displayReservations();
        inventory.displayInventory();

        // Perform cancellations
        System.out.println("\n--- Cancellation Operations ---\n");

        bookingService.cancelReservation("RES1", inventory);
        bookingService.cancelReservation("RES1", inventory); // duplicate cancel attempt
        bookingService.cancelReservation("RES3", inventory); // non-existent

        System.out.println("\nAfter Cancellation:");
        bookingService.displayReservations();
        inventory.displayInventory();

        bookingService.displayReleasedRooms();

        System.out.println("\nSystem state restored consistently.");
    }
}