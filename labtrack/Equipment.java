package labtrack;

import java.util.UUID;

public abstract class Equipment {
    private String equipmentId = UUID.randomUUID().toString();
    private String name;
    private String category;
    private String status;
    protected int totalQuantity;
    protected int availableQuantity;
    protected int damagedQuantity;

    public Equipment(String name, String category, int totalQuantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be null or empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment category cannot be null or empty");
        }
        if (totalQuantity < 0) {
            throw new IllegalArgumentException("Total quantity cannot be negative");
        }

        this.name = name.trim();
        this.category = category.trim();
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.damagedQuantity = 0;
        updateStatus();
    }

    // Simple getters
    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public int getAvailableQuantity() { return availableQuantity; }

    public void increaseStock(int amount) {
        if (amount <= 0) {
            System.err.println("Error: Amount must be positive");
            return;
        }
        totalQuantity += amount;
        availableQuantity += amount;
        updateStatus();
    }

    public void reduceAvailable(int amount) {
        if (amount <= 0) {
            System.err.println("Error: Amount must be positive");
            return;
        }
        if (amount > availableQuantity) {
            System.err.println("Error: Insufficient available quantity. Requested: " + amount + ", Available: " + availableQuantity);
            return;
        }
        availableQuantity -= amount;
        updateStatus();
    }

    public void increaseAvailable(int amount) {
        if (amount <= 0) {
            System.err.println("Error: Amount must be positive");
            return;
        }
        if (availableQuantity + amount > totalQuantity) {
            System.err.println("Error: Cannot increase available quantity beyond total");
            return;
        }
        availableQuantity += amount;
        updateStatus();
    }

    public void addDamaged(int amount) {
        if (amount <= 0) {
            System.err.println("Error: Amount must be positive");
            return;
        }
        if (damagedQuantity + amount > totalQuantity) {
            System.err.println("Error: Cannot have more damaged items than total quantity");
            return;
        }
        damagedQuantity += amount;
        updateStatus();
    }

    private void updateStatus() {
        if (damagedQuantity > 0) {
            status = "DAMAGED";
        }
        else if (availableQuantity == totalQuantity) {
            status = "AVAILABLE";
        }
        else if (availableQuantity > 0) {
            status = "PARTIALLY AVAILABLE";
        }
        else {
            status = "IN USE";
        }
    }

    public abstract String getEquipmentType();

    public abstract void displayUsageInfo();

    public void displayInfo() {
        System.out.println("- - - - - -");
        System.out.println("ID       : " + equipmentId);
        System.out.println("Name     : " + name);
        System.out.println("Category : " + category);
        System.out.println("Total    : " + totalQuantity);
        System.out.println("Available: " + availableQuantity);
        System.out.println("Damaged  : " + damagedQuantity);
        System.out.println("Status   : " + status);
        System.out.println("- - - - - -");
    }
}