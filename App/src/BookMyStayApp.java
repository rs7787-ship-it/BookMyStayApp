/**
 * UseCase11ConcurrentBookingSimulation
 * 
 * Demonstrates thread-safe booking simulation using synchronized blocks.
 * Ensures consistent inventory updates and prevents double allocation.
 * 
 * Author: Rishi S
 * Version: 11.0
 */

import java.util.*;
import java.util.concurrent.*;

class Reservation {
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
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public synchronized void addReservation(Reservation r) {
        confirmedBookings.add(r);
    }

    public synchronized void displayBookings() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmedBookings) {
            System.out.println(r.getReservationId() + " - " + r.getRoomType() + " - " + r.getRoomId());
        }
    }
}

class BookingTask implements Runnable {
    private String reservationId;
    private String roomType;
    private RoomInventory inventory;
    private BookingHistory history;

    public BookingTask(String reservationId, String roomType, RoomInventory inventory, BookingHistory history) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.inventory = inventory;
        this.history = history;
    }

    @Override
    public void run() {
        // Critical section: allocate room safely
        synchronized (inventory) {
            if (inventory.allocateRoom(roomType)) {
                // Generate unique room ID
                String roomId = roomType.substring(0, 2).toUpperCase() + "-" + new Random().nextInt(900) + 100;
                Reservation r = new Reservation(reservationId, roomType, roomId);
                history.addReservation(r);
                System.out.println("Booking Confirmed: " + reservationId + " - " + roomType + " - " + roomId);
            } else {
                System.out.println("Booking Failed (No Availability): " + reservationId + " - " + roomType);
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Concurrent Booking Simulation v11.0");
        System.out.println("====================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate multiple guests booking concurrently
        ExecutorService executor = Executors.newFixedThreadPool(5);

        String[] reservationIds = {"RES-201", "RES-202", "RES-203", "RES-204", "RES-205", "RES-206"};
        String[] roomTypes = {"Single Room", "Double Room", "Single Room", "Double Room", "Single Room", "Double Room"};

        for (int i = 0; i < reservationIds.length; i++) {
            executor.execute(new BookingTask(reservationIds[i], roomTypes[i], inventory, history));
        }

        // Shutdown executor and wait for tasks to finish
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nAll booking attempts processed.\n");

        // Display final system state
        inventory.displayInventory();
        history.displayBookings();
    }
}
