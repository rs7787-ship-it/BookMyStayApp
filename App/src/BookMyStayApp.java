/**
 * UseCase5BookingRequestQueue
 * 
 * Demonstrates booking request intake using a Queue (FIFO)
 * to ensure fair handling of multiple booking requests.
 * 
 * No inventory updates or room allocation is performed here.
 * 
 * @author Rishi. S
 * @version 5.0
 */

import java.util.LinkedList;
import java.util.Queue;

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
 * BookingRequestQueue handles incoming booking requests
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Add booking request to queue
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    /**
     * Display all queued requests (without processing)
     */
    public void displayQueue() {
        System.out.println("\nCurrent Booking Requests (FIFO Order):");

        for (Reservation r : requestQueue) {
            System.out.println("Guest: " + r.getGuestName() +
                               " | Room Type: " + r.getRoomType());
        }
    }
}

/**
 * Main class to execute the use case
 */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking Request Queue v5.0");
        System.out.println("====================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Deluxe Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double Room"));

        // Display queued requests (FIFO order preserved)
        bookingQueue.displayQueue();

        System.out.println("\nAll requests are queued. No allocation done yet.");
    }
}
