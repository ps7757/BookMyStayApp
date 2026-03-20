/**
 * Book My Stay App
 * Use Case 2: Basic Room Types & Static Availability
 *
 * This program demonstrates abstraction, inheritance, and polymorphism
 * by modeling different types of hotel rooms and displaying their availability.
 *
 * @author YourName
 * @version 2.0
 */

// Abstract class
abstract class Room {
    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method
    public abstract String getRoomType();

    // Common method
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 50);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 90);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 150);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}

// Main class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v2.0");
        System.out.println("=====================================");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("\n--- Room Details & Availability ---\n");

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println("-----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println("-----------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
        System.out.println("-----------------------------------");

        System.out.println("Application terminated.");
    }
}