package labtrack;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class InventoryManager {
    private List<TrackableEquipment> equipmentList = new ArrayList<>();
    private Gson gson = new Gson();

    public void addEquipment(TrackableEquipment equipment) {
        try {
            // Validate input
            if (equipment == null) {
                System.out.println("Error: Cannot add null equipment.");
                return;
            }

            for (TrackableEquipment eq : equipmentList) {
                if (eq.getEquipmentId() != null &&
                        eq.getEquipmentId().equalsIgnoreCase(equipment.getEquipmentId())) {
                    System.out.println("Error: Equipment with ID '" +
                            equipment.getEquipmentId() + "' already exists.");
                    return;
                }
            }

            equipmentList.add(equipment);
            saveToFile();
            System.out.println("Equipment added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding equipment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<TrackableEquipment> getAllEquipment() {
        return equipmentList;
    }

    public void searchEquipment(String keyword) {
        boolean found = false;
        for (TrackableEquipment eq : equipmentList) {
            if (eq.getName().equalsIgnoreCase(keyword) ||
                    eq.getEquipmentId().equalsIgnoreCase(keyword)) {
                eq.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("Not found.");
    }

    public void filterByStatus(String status) {
        boolean found = false;
        for (TrackableEquipment eq : equipmentList) {
            if (eq.getStatus().equalsIgnoreCase(status)) {
                eq.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No equipment with that status.");
    }

    public void filterByCategory(String category) {
        boolean found = false;
        for (TrackableEquipment eq : equipmentList) {
            if (eq.getCategory().equalsIgnoreCase(category)) {
                eq.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No equipment in that category.");
    }

    public void viewAllEquipment() {
        if (equipmentList.isEmpty()) {
            System.out.println("No equipment available.");
        } else {
            for (TrackableEquipment eq : equipmentList) {
                eq.displayInfo();
            }
        }
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter("equipment.json")) {
            gson.toJson(equipmentList, writer);
            System.out.println("Saved!");
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (Reader reader = new FileReader("equipment.json")) {
            TrackableEquipment[] equipments = gson.fromJson(reader, TrackableEquipment[].class);
            if (equipments != null) {
                equipmentList = new ArrayList<>(Arrays.asList(equipments));
                System.out.println("Loaded " + equipments.length + " equipment(s).");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }
}