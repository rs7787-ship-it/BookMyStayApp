/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates saving and restoring booking and inventory state using serialization.
 *
 * Author: Rishi S
 * Version: 12.0
 */

import java.io.*;
import java.util.*;

// Reservation class implements Serializable for persistence
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    @Override
    public String toString() {
        return reservationId + " - " + roomType + " - " + roomId;
    }
}

// Inventory class implements Serializable for persistence
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// BookingHistory class implements Serializable for persistence
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        confirmedBookings.add(r);
    }

    public void displayBookings() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }

    public List<Reservation> getConfirmedBookings() {
        return confirmedBookings;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "hotelData.ser";

    public static void saveData(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nData saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Object[] loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\nData loaded successfully from " + FILE_NAME);
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Data Persistence & Recovery v12.0");
        System.out.println("====================================");

        // Load previous state if available
        Object[] loadedData = PersistenceService.loadData();
        RoomInventory inventory = loadedData != null ? (RoomInventory) loadedData[0] : new RoomInventory();
        BookingHistory history = loadedData != null ? (BookingHistory) loadedData[1] : new BookingHistory();

        // Simulate booking new reservations
        String[][] newBookings = {
            {"RES-301", "Single Room"},
            {"RES-302", "Double Room"},
            {"RES-303", "Single Room"}
        };

        for (String[] booking : newBookings) {
            String resId = booking[0];
            String roomType = booking[1];
            if (inventory.allocateRoom(roomType)) {
                String roomId = roomType.substring(0,2).toUpperCase() + "-" + new Random().nextInt(900) + 100;
                Reservation r = new Reservation(resId, roomType, roomId);
                history.addReservation(r);
                System.out.println("Booking Confirmed: " + r);
            } else {
                System.out.println("Booking Failed (No Availability): " + resId + " - " + roomType);
            }
        }

        // Display current system state
        inventory.displayInventory();
        history.displayBookings();

        // Save data for next run
        PersistenceService.saveData(inventory, history);
    }
}
