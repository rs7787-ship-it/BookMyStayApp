/**
 * UseCase6RoomAllocationService
 * 
 * Demonstrates booking confirmation and room allocation while
 * ensuring uniqueness and preventing double-booking.
 * 
 * Uses Queue (FIFO), Set (uniqueness), and HashMap (grouped tracking).
 * 
 * @author Rishi S
 * @version 6.0
 */

import java.util.*;

/**
 * Reservation class represents a booking request
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
 * RoomInventory handles availability
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Deluxe Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * BookingRequestQueue manages incoming requests
 */
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

/**
 * BookingService handles allocation logic
 */
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs per room type
    private Map<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness
    private Set<String> allAllocatedRoomIds;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        this.allAllocatedRoomIds = new HashSet<>();
    }

    /**
     * Process booking requests
     */
    public void processBookings(BookingRequestQueue queue) {

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing request for: " + request.getGuestName());

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (extra safety)
                if (!allAllocatedRoomIds.contains(roomId)) {

                    // Store allocation
                    allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                    allAllocatedRoomIds.add(roomId);

                    // Update inventory immediately
                    inventory.decrementRoom(roomType);

                    // Confirm booking
                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest: " + request.getGuestName());
                    System.out.println("Room Type: " + roomType);
                    System.out.println("Room ID: " + roomId);

                } else {
                    System.out.println("Error: Duplicate Room ID detected!");
                }

            } else {
                System.out.println("Booking Failed: No rooms available for " + roomType);
            }
        }
    }

    /**
     * Generate unique room ID
     */
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    /**
     * Display allocated rooms
     */
    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/**
 * Main class
 */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Room Allocation v6.0");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Deluxe Room"));

        // Process bookings
        BookingService service = new BookingService(inventory);
        service.processBookings(queue);

        // Show results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll booking requests processed.");
    }
}
