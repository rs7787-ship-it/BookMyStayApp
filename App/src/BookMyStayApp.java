/**
 * UseCase8BookingHistoryReport
 * 
 * Demonstrates storing confirmed bookings and generating reports
 * using a List to maintain chronological order.
 * 
 * Focuses on historical tracking and read-only reporting.
 * 
 * @author Rishi S
 * @version 8.0
 */

import java.util.*;

/**
 * Reservation class represents a confirmed booking
 */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * BookingHistory stores confirmed reservations
 */
class BookingHistory {

    // List maintains insertion order (chronological)
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    /**
     * Add confirmed reservation to history
     */
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Stored reservation: " + reservation.getReservationId());
    }

    /**
     * Retrieve all reservations (read-only access)
     */
    public List<Reservation> getAllReservations() {
        return history;
    }
}

/**
 * BookingReportService generates reports
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    /**
     * Display all bookings
     */
    public void displayAllBookings() {
        System.out.println("\nBooking History:");

        for (Reservation r : history.getAllReservations()) {
            System.out.println("--------------------------------");
            System.out.println("Reservation ID: " + r.getReservationId());
            System.out.println("Guest Name    : " + r.getGuestName());
            System.out.println("Room Type     : " + r.getRoomType());
        }
    }

    /**
     * Generate summary report
     */
    public void generateSummaryReport() {

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            roomTypeCount.put(
                r.getRoomType(),
                roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nBooking Summary Report:");

        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " Bookings: " + entry.getValue());
        }
    }
}

/**
 * Main class
 */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking History & Reports v8.0");
        System.out.println("====================================");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES-101", "Alice", "Single Room"));
        history.addReservation(new Reservation("RES-102", "Bob", "Deluxe Room"));
        history.addReservation(new Reservation("RES-103", "Charlie", "Single Room"));
        history.addReservation(new Reservation("RES-104", "David", "Double Room"));

        // Initialize report service
        BookingReportService reportService = new BookingReportService(history);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummaryReport();

        System.out.println("\nReporting completed (No data modified).");
    }
}
