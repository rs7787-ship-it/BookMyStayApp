/**
 * UseCase4RoomSearch
 * 
 * Demonstrates read-only room search functionality using centralized inventory.
 * Ensures no modification to system state during search operations.
 * 
 * @author Rishi. S
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Room class represents room details (Domain Model)
 */
class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

/**
 * RoomInventory class (Read-only usage in this use case)
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Deluxe Room", 3);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

/**
 * SearchService handles read-only search logic
 */
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomDetails;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room domain data (separate from inventory)
        roomDetails = new HashMap<>();
        roomDetails.put("Single Room", new Room("Single Room", 2000));
        roomDetails.put("Double Room", new Room("Double Room", 3500));
        roomDetails.put("Deluxe Room", new Room("Deluxe Room", 5000));
    }

    /**
     * Displays only available rooms (read-only operation)
     */
    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:");

        for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {

            String roomType = entry.getKey();
            int availableCount = entry.getValue();

            // Validation: show only available rooms
            if (availableCount > 0) {
                Room room = roomDetails.get(roomType);

                System.out.println("--------------------------------");
                System.out.println("Room Type : " + room.getType());
                System.out.println("Price     : ₹" + room.getPrice());
                System.out.println("Available : " + availableCount);
            }
        }
    }
}

/**
 * Main class to execute the use case
 */
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Room Search v4.0");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed successfully (No state modified).");
    }
}
