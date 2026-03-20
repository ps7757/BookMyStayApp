/**
 * Book My Stay App
 * Use Case 8: Booking History & Reporting
 *
 * Demonstrates how confirmed bookings are stored and reported
 * using a List to maintain chronological order.
 *
 * Reporting is read-only and does not modify stored data.
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// -------------------- Booking History --------------------
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings (read-only)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }

    // Display all bookings
    public void displayHistory() {
        System.out.println("\n--- Booking History ---\n");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }
    }
}

// -------------------- Reporting Service --------------------
class BookingReportService {

    // Generate summary report
    public void generateSummary(BookingHistory history) {

        System.out.println("\n--- Booking Summary Report ---\n");

        List<Reservation> reservations = history.getAllReservations();

        Map<String, Integer> countByRoomType = new HashMap<>();

        for (Reservation r : reservations) {
            countByRoomType.put(
                    r.getRoomType(),
                    countByRoomType.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        // Print summary
        for (String type : countByRoomType.keySet()) {
            System.out.println(type + " : " + countByRoomType.get(type) + " bookings");
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// -------------------- Main Class --------------------
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v8.0");
        System.out.println("=====================================");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES1", "Alice", "Single Room", "SI-1234"));
        history.addReservation(new Reservation("RES2", "Bob", "Double Room", "DO-5678"));
        history.addReservation(new Reservation("RES3", "Charlie", "Single Room", "SI-4321"));

        // Display full history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateSummary(history);

        System.out.println("\nReporting completed. No data modified.");
    }
}