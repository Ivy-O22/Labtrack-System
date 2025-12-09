package labtrack;

public class InventoryActions {
    private InventoryManager manager;

    public InventoryActions(InventoryManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Inventory Manager cannot be null");
        }
        this.manager = manager;
    }

    public void addOrUpdateEquipment(String name, String category, int qty) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }
        if (category == null || category.trim().isEmpty()) {
            System.out.println("Error: Category cannot be empty.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        try {
            for (TrackableEquipment eq : manager.getAllEquipment()) {
                if (eq.getName().equalsIgnoreCase(name)) {
                    eq.increaseStock(qty);
                    manager.saveToFile();
                    System.out.println("Updated existing equipment.");
                    return;
                }
            }

            TrackableEquipment newEq = new TrackableEquipment(name, category, qty);
            manager.addEquipment(newEq);
        } catch (Exception e) {
            System.out.println("Error adding/updating equipment: " + e.getMessage());
        }
    }

    public void borrowEquipment(String name, String user, int qty, String date) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }
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

            if (eq.borrowEquipment(user, qty, date)) {
                manager.saveToFile();
                System.out.println("Borrowed successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error borrowing equipment: " + e.getMessage());
        }
    }

    public void returnEquipment(String name, String user, int qty, String date) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }
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

            if (eq.returnEquipment(user, qty, date)) {
                manager.saveToFile();
                System.out.println("Returned successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error returning equipment: " + e.getMessage());
        }
    }

    public void markDamaged(String name, String user, int qty, String date) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Equipment name cannot be empty.");
            return;
        }
        if (user == null || user.trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty.");
            return;
        }
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
            System.out.println("Error marking equipment as damaged: " + e.getMessage());
        }
    }

    private TrackableEquipment findEquipment(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            for (TrackableEquipment eq : manager.getAllEquipment()) {
                if (eq.getName().equalsIgnoreCase(name)) {
                    return eq;
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching for equipment: " + e.getMessage());
        }
        return null;
    }
}