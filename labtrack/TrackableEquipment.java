package labtrack;

import java.util.*;

public class TrackableEquipment extends Equipment implements Trackable {

    private Map<String, Integer> borrowers = new HashMap<>();
    private List<String> history = new ArrayList<>();

    public TrackableEquipment(String name, String category, int totalQuantity) {
        super(name, category, totalQuantity);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be null or empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (totalQuantity < 0) {
            throw new IllegalArgumentException("Total quantity cannot be negative");
        }
    }

    @Override
    public String getEquipmentType() {
        return "Trackable";
    }

    @Override
    public void displayUsageInfo() {
        try {
            System.out.println("Equipment Type: " + getEquipmentType());
            System.out.println("Borrowers:");
            if (borrowers.isEmpty()) {
                System.out.println(" - None");
            } else {
                borrowers.forEach((user, qty) ->
                        System.out.println(" - " + user + ": " + qty));
            }

            System.out.println("History:");
            if (history.isEmpty()) {
                System.out.println(" - No history");
            } else {
                history.forEach(h -> System.out.println(" - " + h));
            }
        } catch (Exception e) {
            System.err.println("Error displaying usage info: " + e.getMessage());
        }
    }

    @Override
    public boolean borrowEquipment(String user, int quantity, String date) {
        try {
            // Validate inputs
            if (user == null || user.trim().isEmpty()) {
                System.out.println("Error: User name cannot be null or empty.");
                return false;
            }

            if (date == null || date.trim().isEmpty()) {
                System.out.println("Error: Date cannot be null or empty.");
                return false;
            }

            // Validate date format (YYYY-MM-DD only)
            if (!isValidDateFormat(date)) {
                System.out.println("Error: Invalid date format. Expected format: YYYY-MM-DD.");
                return false;
            }

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return false;
            }

            if (quantity > getAvailableQuantity()) {
                System.out.println("Not enough available. Only " + getAvailableQuantity() + " left.");
                return false;
            }

            // Perform the borrow operation
            reduceAvailable(quantity);
            borrowers.put(user, borrowers.getOrDefault(user, 0) + quantity);
            history.add("BORROWED " + quantity + " by " + user + " on " + date);
            return true;

        } catch (Exception e) {
            System.err.println("Error during borrow operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnEquipment(String user, int quantity, String date) {
        try {
            // Validate inputs
            if (user == null || user.trim().isEmpty()) {
                System.out.println("Error: User name cannot be null or empty.");
                return false;
            }

            if (date == null || date.trim().isEmpty()) {
                System.out.println("Error: Date cannot be null or empty.");
                return false;
            }

            // Validate date format
            if (!isValidDateFormat(date)) {
                System.out.println("Error: Invalid date format. Expected format: YYYY-MM-DD.");
                return false;
            }

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return false;
            }

            if (!borrowers.containsKey(user)) {
                System.out.println(user + " has no borrowed equipment.");
                return false;
            }

            if (borrowers.get(user) < quantity) {
                System.out.println(user + " is returning more than borrowed. Borrowed: " + borrowers.get(user));
                return false;
            }

            // Perform the return operation
            increaseAvailable(quantity);
            borrowers.put(user, borrowers.get(user) - quantity);
            if (borrowers.get(user) == 0) borrowers.remove(user);

            history.add("RETURNED " + quantity + " by " + user + " on " + date);
            return true;

        } catch (Exception e) {
            System.err.println("Error during return operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean markDamaged(String user, int quantity, String date) {
        try {
            // Validate inputs
            if (user == null || user.trim().isEmpty()) {
                System.out.println("Error: User name cannot be null or empty.");
                return false;
            }

            if (date == null || date.trim().isEmpty()) {
                System.out.println("Error: Date cannot be null or empty.");
                return false;
            }

            // Validate date format
            if (!isValidDateFormat(date)) {
                System.out.println("Error: Invalid date format. Expected format: YYYY-MM-DD.");
                return false;
            }

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return false;
            }

            if (!borrowers.containsKey(user)) {
                System.out.println(user + " has no borrowed equipment.");
                return false;
            }

            if (borrowers.get(user) < quantity) {
                System.out.println("Damage quantity exceeds borrowed amount. Borrowed: " + borrowers.get(user));
                return false;
            }

            addDamaged(quantity);
            borrowers.put(user, borrowers.get(user) - quantity);
            if (borrowers.get(user) == 0) borrowers.remove(user);

            history.add("DAMAGED " + quantity + " by " + user + " on " + date);
            return true;

        } catch (Exception e) {
            System.err.println("Error during damage marking operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void displayInfo() {
        try {
            super.displayInfo();
            displayUsageInfo();
        } catch (Exception e) {
            System.err.println("Error displaying equipment info: " + e.getMessage());
        }
    }

    public List<String> getUsageHistory() {
        try {
            return new ArrayList<>(history);
        } catch (Exception e) {
            System.err.println("Error retrieving usage history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, Integer> getBorrowers() {
        try {
            return new HashMap<>(borrowers);
        } catch (Exception e) {
            System.err.println("Error retrieving borrowers: " + e.getMessage());
            return new HashMap<>();
        }
    }


    // Validates if the date string is in a valid format
    //Accepts YYYY-MM-DD format only

    private boolean isValidDateFormat(String date) {
        // Check for YYYY-MM-DD format
        if (date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return isValidDateValues(date);
        }
        return false;
    }

    //Validates the actual date values (month, day, year ranges)
    private boolean isValidDateValues(String date) {
        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // Basic validation
            if (year < 1900 || year > 2100) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}