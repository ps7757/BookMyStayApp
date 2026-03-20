/**
 * Book My Stay App
 * Use Case 12: Data Persistence & System Recovery
 *
 * Demonstrates saving and restoring system state using file-based
 * persistence (serialization/deserialization).
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// -------------------- Data Model --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<String> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<String> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// -------------------- Persistence Service --------------------
class PersistenceService {

    private static final String FILE_NAME = "bookmystay_data.ser";

    // Save state to file
    public void saveState(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState loadState() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting fresh.");
            return null;
        }
    }
}

// -------------------- Inventory + Booking --------------------
class BookingSystem {

    Map<String, Integer> inventory;
    List<String> bookingHistory;

    public BookingSystem(SystemState state) {
        if (state != null) {
            this.inventory = state.inventory;
            this.bookingHistory = state.bookingHistory;
        } else {
            inventory = new HashMap<>();
            bookingHistory = new ArrayList<>();

            inventory.put("Single Room", 2);
            inventory.put("Double Room", 2);
            inventory.put("Suite Room", 1);
        }
    }

    public void bookRoom(String guestName, String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);

            String record = guestName + " booked " + roomType;
            bookingHistory.add(record);

            System.out.println("Booking SUCCESS: " + record);
        } else {
            System.out.println("Booking FAILED: No availability for " + roomType);
        }
    }

    public SystemState getState() {
        return new SystemState(inventory, bookingHistory);
    }

    public void displayState() {
        System.out.println("\n--- Inventory ---");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }

        System.out.println("\n--- Booking History ---");
        for (String record : bookingHistory) {
            System.out.println(record);
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v12.0");
        System.out.println("=====================================");

        PersistenceService persistenceService = new PersistenceService();

        // Load previous state (if exists)
        SystemState loadedState = persistenceService.loadState();

        // Initialize system
        BookingSystem system = new BookingSystem(loadedState);

        System.out.println("\n--- Current System State ---");
        system.displayState();

        // Simulate some bookings
        System.out.println("\n--- New Bookings ---");
        system.bookRoom("Alice", "Single Room");
        system.bookRoom("Bob", "Double Room");

        // Display updated state
        system.displayState();

        // Save state before shutdown
        persistenceService.saveState(system.getState());

        System.out.println("\nSystem shutdown completed safely with persistence.");
    }
}