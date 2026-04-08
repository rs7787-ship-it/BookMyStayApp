/**
 * UseCase7AddOnServiceSelection
 * 
 * Demonstrates how add-on services can be attached to a reservation
 * without modifying core booking or inventory logic.
 * 
 * Uses Map<String, List<Service>> to model one-to-many relationships.
 * 
 * @author Rishi S
 * @version 7.0
 */

import java.util.*;

/**
 * Service class represents an add-on offering
 */
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

/**
 * AddOnServiceManager handles mapping between reservations and services
 */
class AddOnServiceManager {

    // Map reservationId -> List of services
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    /**
     * Add a service to a reservation
     */
    public void addService(String reservationId, Service service) {
        reservationServices
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);

        System.out.println("Added service: " + service.getName() +
                           " to Reservation ID: " + reservationId);
    }

    /**
     * Display services for a reservation
     */
    public void displayServices(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("\nNo add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nAdd-On Services for Reservation ID: " + reservationId);

        for (Service s : services) {
            System.out.println("Service: " + s.getName() + " | Cost: ₹" + s.getCost());
        }
    }

    /**
     * Calculate total cost of add-on services
     */
    public double calculateTotalCost(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

/**
 * Main class to demonstrate use case
 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Add-On Services v7.0");
        System.out.println("====================================");

        // Simulated reservation ID (from Use Case 6)
        String reservationId = "RES-101";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects add-on services
        manager.addService(reservationId, new Service("Breakfast", 500));
        manager.addService(reservationId, new Service("Airport Pickup", 1200));
        manager.addService(reservationId, new Service("Extra Bed", 800));

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate total add-on cost
        double totalCost = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}
