/**
 * UseCase3InventorySetup
 * 
 * This class demonstrates centralized room inventory management
 * using a HashMap as a single source of truth.
 * 
 * @author Rishi. S
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory class handles all inventory-related operations.
 */
class RoomInventory {

    // HashMap to store room type and available count
    private Map<String, Integer> inventory;

    /**
     * Constructor initializes room availability
     */
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room types with availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Deluxe Room", 3);
    }

    /**
     * Get availability of a specific room type
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Update availability of a specific room type
     */
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type does not exist!");
        }
    }

    /**
     * Display complete inventory
     */
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * Main class to run the use case
 */
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Inventory Management v3.1");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Retrieve availability
        System.out.println("\nChecking availability for Deluxe Room:");
        System.out.println("Available: " + inventory.getAvailability("Deluxe Room"));

        // Update availability
        System.out.println("\nUpdating Deluxe Room availability...");
        inventory.updateAvailability("Deluxe Room", 2);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication execution completed.");
    }
}
