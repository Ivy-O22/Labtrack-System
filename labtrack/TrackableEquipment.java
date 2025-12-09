package labtrack;

import java.util.*;

public class TrackableEquipment extends Equipment implements EquipmentOperations {

    private Map<String, Integer> borrowers = new HashMap<>();
    private List<String> history = new ArrayList<>();

    public TrackableEquipment(String name, String category, int totalQuantity) {
        super(name, category, totalQuantity);
    }

    @Override
    public String getEquipmentType() {
        return "Trackable";
    }

    @Override
    public void displayUsageInfo() {
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
    }

    @Override
    public boolean borrowEquipment(String user, int quantity, String date) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return false;
        }

        if (quantity > getAvailableQuantity()) {
            System.out.println("Not enough available. Only " + getAvailableQuantity() + " left.");
            return false;
        }

        reduceAvailable(quantity);
        borrowers.put(user, borrowers.getOrDefault(user, 0) + quantity);
        history.add("BORROWED " + quantity + " by " + user + " on " + date);
        return true;
    }

    @Override
    public boolean returnEquipment(String user, int quantity, String date) {
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

        increaseAvailable(quantity);
        borrowers.put(user, borrowers.get(user) - quantity);
        if (borrowers.get(user) == 0) borrowers.remove(user);

        history.add("RETURNED " + quantity + " by " + user + " on " + date);
        return true;
    }

    @Override
    public boolean markDamaged(String user, int quantity, String date) {
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
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        displayUsageInfo();
    }

    public List<String> getUsageHistory() {
        return history;
    }

    public Map<String, Integer> getBorrowers() {
        return new HashMap<>(borrowers);
    }
}