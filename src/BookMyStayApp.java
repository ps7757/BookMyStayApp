/**
 * Book My Stay App
 * Use Case 3: Centralized Room Inventory Management
 *
 * This program demonstrates how to manage room availability
 * using a centralized HashMap for consistency and scalability.
 *
 * @author YourName
 * @version 3.0
 */

import java.util.HashMap;
import java.util.Map;

// Inventory Class (Single Source of Truth)
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes availability
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled modification)
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

// Main Class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v3.0");
        System.out.println("=====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Example update
        System.out.println("\nUpdating availability...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated.");
    }
}