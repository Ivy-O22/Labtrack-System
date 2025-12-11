package labtrack;

public class InventoryActions {
    private InventoryManager manager;

    public InventoryActions(InventoryManager manager) {
        // Check if manager is null - we can't operate without a valid manager
        if (manager == null) {
            throw new IllegalArgumentException("Inventory Manager cannot be null");
        }
        this.manager = manager;
    }

    public void addOrUpdateEquipment(String name, String category, int qty) {
        // Validate equipment name is not null or empty
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }

        // Validate category is not null or empty
        if (category == null || category.trim().isEmpty()) {
            System.out.println("Error: Category cannot be empty.");
            return;
        }

        // Validate quantity is positive - can't add zero or negative items
        if (qty <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        try {
            // Search through existing equipment to see if this item already exists
            for (TrackableEquipment eq : manager.getAllEquipment()) {
                if (eq.getName().equalsIgnoreCase(name)) {
                    eq.increaseStock(qty);
                    manager.saveToFile();
                    System.out.println("Updated existing equipment.");
                    return;
                }
            }

            // Equipment doesn't exist yet - create new entry
            TrackableEquipment newEq = new TrackableEquipment(name, category, qty);
            manager.addEquipment(newEq);
        } catch (Exception e) {
            // Catch any unexpected errors (file I/O issues, etc.)
            System.out.println("Error adding/updating equipment: " + e.getMessage());
        }
    }

    public void borrowEquipment(String name, String user, int qty, String date) {
        // Validate equipment name
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }

        // Validate username - we need to know who borrowed it
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }

        // Validate quantity is positive
        if (qty <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        // Validate date is provided - important for tracking
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Date cannot be empty.");
            return;
        }

        try {
            TrackableEquipment eq = findEquipment(name);
            if (eq == null) {
                System.out.println("Equipment not found.");
                return;
            }

            if (eq.borrowEquipment(user, qty, date)) {
                manager.saveToFile();
                System.out.println("Borrowed successfully!");
            }
        } catch (Exception e) {
            // Catch any unexpected errors during the borrow process
            System.out.println("Error borrowing equipment: " + e.getMessage());
        }
    }

    public void returnEquipment(String name, String user, int qty, String date) {
        // Validate equipment name
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }

        // Validate username - need to match with borrow record
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }

        // Validate quantity is positive
        if (qty <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        // Validate return date is provided
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Date cannot be empty.");
            return;
        }

        try {
            TrackableEquipment eq = findEquipment(name);
            if (eq == null) {
                System.out.println("Equipment not found.");
                return;
            }

            if (eq.returnEquipment(user, qty, date)) {
                manager.saveToFile();
                System.out.println("Returned successfully!");
            }
        } catch (Exception e) {
            // Catch any unexpected errors during the return process
            System.out.println("Error returning equipment: " + e.getMessage());
        }
    }

    public void markDamaged(String name, String user, int qty, String date) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }

        // Validate username - need to know who reported/caused the damage
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }

        // Validate quantity is positive
        if (qty <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Date cannot be empty.");
            return;
        }

        try {
            TrackableEquipment eq = findEquipment(name);
            if (eq == null) {
                System.out.println("Equipment not found.");
                return;
            }

            if (eq.markDamaged(user, qty, date)) {
                manager.saveToFile();
                System.out.println("Marked as damaged.");
            }
        } catch (Exception e) {
            // Catch any unexpected errors during the damage marking process
            System.out.println("Error marking equipment as damaged: " + e.getMessage());
        }
    }

    private TrackableEquipment findEquipment(String name) {
        // Validate name before searching
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            for (TrackableEquipment eq : manager.getAllEquipment()) {
                // equalsIgnoreCase allows "Microscope" to match "microscope"
                if (eq.getName().equalsIgnoreCase(name)) {
                    return eq;
                }
            }
        } catch (Exception e) {
            // Catch any errors during the search (e.g., if getAllEquipment() fails)
            System.out.println("Error searching for equipment: " + e.getMessage());
        }
        return null;
    }
}