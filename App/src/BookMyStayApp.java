/**
 * UseCase10BookingCancellation
 * 
 * Demonstrates booking cancellation with safe rollback using Stack (LIFO),
 * ensuring inventory consistency and controlled state reversal.
 * 
 * @author Rishi S
 * @version 10.0
 */

import java.util.*;

/**
 * Reservation class
 */
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

/**
 * RoomInventory class
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * BookingHistory with cancellation tracking
 */
class BookingHistory {

    private Map<String, Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String reservationId) {
        return confirmedBookings.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        confirmedBookings.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return confirmedBookings.containsKey(reservationId);
    }
}

/**
 * CancellationService handles rollback logic
 */
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    /**
     * Cancel booking and perform rollback
     */
    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate reservation existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found!");
            return;
        }

        Reservation reservation = history.getReservation(reservationId);

        // Step 2: Record room ID for rollback
        rollbackStack.push(reservation.getRoomId());

        // Step 3: Restore inventory
        inventory.incrementRoom(reservation.getRoomType());

        // Step 4: Remove booking from history
        history.removeReservation(reservationId);

        // Step 5: Confirm cancellation
        System.out.println("Cancellation Successful!");
        System.out.println("Released Room ID: " + reservation.getRoomId());
    }

    /**
     * Display rollback stack
     */
    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recently Released Rooms): " + rollbackStack);
    }
}

/**
 * Main class
 */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking Cancellation v10.0");
        System.out.println("====================================");

        // Initialize inventory and history
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES-101", "Single Room", "SI-111"));
        history.addReservation(new Reservation("RES-102", "Double Room", "DO-222"));

        // Initialize cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Perform cancellation
        service.cancelBooking("RES-101");

        // Attempt invalid cancellation
        service.cancelBooking("RES-999");

        // Display rollback state
        service.displayRollbackStack();

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nSystem state restored safely after cancellation.");
    }
}
