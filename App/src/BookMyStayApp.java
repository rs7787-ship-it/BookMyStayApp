/**
 * UseCase9ErrorHandlingValidation
 * 
 * Demonstrates validation and error handling using custom exceptions
 * to ensure system reliability and prevent invalid state changes.
 * 
 * Implements fail-fast design and graceful error handling.
 * 
 * @author Rishi S
 * @version 9.0
 */

import java.util.*;

/**
 * Custom Exception for invalid booking scenarios
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Reservation class
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * RoomInventory with validation safeguards
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Deluxe Room", 0);
    }

    public int getAvailability(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        return inventory.get(roomType);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        inventory.put(roomType, available - 1);
    }
}

/**
 * Validator class for booking input
 */
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        // Validate room type and availability
        inventory.getAvailability(reservation.getRoomType());
    }
}

/**
 * BookingService with validation and error handling
 */
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Check availability again before allocation
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking Successful!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

/**
 * Main class
 */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Error Handling & Validation v9.0");
        System.out.println("====================================");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases

        // Valid booking
        service.processBooking(new Reservation("Alice", "Single Room"));

        // Invalid room type
        service.processBooking(new Reservation("Bob", "Suite Room"));

        // No availability
        service.processBooking(new Reservation("Charlie", "Deluxe Room"));

        // Empty guest name
        service.processBooking(new Reservation("", "Double Room"));

        System.out.println("\nSystem continues running safely after errors.");
    }
}
