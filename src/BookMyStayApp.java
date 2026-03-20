/**
 * Book My Stay App
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates input validation using custom exceptions and
 * fail-fast design to ensure system reliability.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// -------------------- Custom Exception --------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- Validator --------------------
class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES = new HashSet<>(
            Arrays.asList("Single Room", "Double Room", "Suite Room")
    );

    // Validate booking request
    public void validate(String guestName, String roomType, int availableRooms)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (availableRooms <= 0) {
            throw new InvalidBookingException("No availability for selected room type.");
        }
    }
}

// -------------------- Inventory --------------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 0); // intentionally 0 for validation demo
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
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

    private BookingValidator validator = new BookingValidator();

    public void bookRoom(String guestName, String roomType, RoomInventory inventory) {

        try {
            int available = inventory.getAvailability(roomType);

            // Validate input and system state
            validator.validate(guestName, roomType, available);

            // Proceed with booking if valid
            inventory.decrement(roomType);

            System.out.println("Booking SUCCESS for " + guestName +
                    " | Room Type: " + roomType);

        } catch (InvalidBookingException e) {
            System.out.println("Booking FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v9.0");
        System.out.println("=====================================");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();

        inventory.displayInventory();

        System.out.println("\n--- Booking Attempts ---\n");

        // Valid booking
        bookingService.bookRoom("Alice", "Single Room", inventory);

        // Invalid room type
        bookingService.bookRoom("Bob", "Deluxe Room", inventory);

        // No availability
        bookingService.bookRoom("Charlie", "Double Room", inventory);

        // Empty guest name
        bookingService.bookRoom("", "Suite Room", inventory);

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();

        System.out.println("\nSystem continued safely after errors.");
    }
}