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
        this.name = name;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.damagedQuantity = 0;
        updateStatus();
    }
// THIS IS A COMMIT TESTING

    // Simple getters
    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public int getDamagedQuantity() { return damagedQuantity; }

    public void increaseStock(int amount) {
        totalQuantity += amount;
        availableQuantity += amount;
        updateStatus();
    }

    public void reduceAvailable(int amount) {
        availableQuantity -= amount;
        updateStatus();
    }

    public void increaseAvailable(int amount) {
        availableQuantity += amount;
        updateStatus();
    }

    public void addDamaged(int amount) {
        damagedQuantity += amount;
        updateStatus();
    }

    private void updateStatus() {
        if (availableQuantity == totalQuantity) {
            status = "AVAILABLE";
        } else if (availableQuantity > 0) {
            status = "PARTIALLY AVAILABLE";
        } else if (damagedQuantity > 0) {
            status = "DAMAGED";
        } else {
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