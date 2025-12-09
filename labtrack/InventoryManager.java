package labtrack;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class InventoryManager {
    private List<TrackableEquipment> equipmentList = new ArrayList<>();
    private Gson gson = new Gson();

    public void addEquipment(TrackableEquipment equipment) {
        try {
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
        try {

            return new ArrayList<>(equipmentList);
        } catch (Exception e) {
            System.out.println("Error retrieving equipment list: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void searchEquipment(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                System.out.println("Error: Search keyword cannot be empty.");
                return;
            }

            if (equipmentList.isEmpty()) {
                System.out.println("No equipment in inventory to search.");
                return;
            }

            boolean found = false;
            String trimmedKeyword = keyword.trim();

            for (TrackableEquipment eq : equipmentList) {
                if (eq == null) continue;

                String name = eq.getName();
                String id = eq.getEquipmentId();

                if ((name != null && name.equalsIgnoreCase(trimmedKeyword)) ||
                        (id != null && id.equalsIgnoreCase(trimmedKeyword))) {
                    eq.displayInfo();
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No equipment found matching: " + trimmedKeyword);
            }

        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void filterByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                System.out.println("Error: Status filter cannot be empty.");
                return;
            }

            if (equipmentList.isEmpty()) {
                System.out.println("No equipment in inventory to filter.");
                return;
            }

            boolean found = false;
            String trimmedStatus = status.trim();

            for (TrackableEquipment eq : equipmentList) {
                if (eq == null) continue;

                String equipmentStatus = eq.getStatus();

                if (equipmentStatus != null &&
                        equipmentStatus.equalsIgnoreCase(trimmedStatus)) {
                    eq.displayInfo();
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No equipment found with status: " + trimmedStatus);
            }

        } catch (Exception e) {
            System.out.println("Error filtering by status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void filterByCategory(String category) {
        try {
            if (category == null || category.trim().isEmpty()) {
                System.out.println("Error: Category filter cannot be empty.");
                return;
            }

            if (equipmentList.isEmpty()) {
                System.out.println("No equipment in inventory to filter.");
                return;
            }

            boolean found = false;
            String trimmedCategory = category.trim();

            for (TrackableEquipment eq : equipmentList) {
                if (eq == null) continue;

                String equipmentCategory = eq.getCategory();

                if (equipmentCategory != null &&
                        equipmentCategory.equalsIgnoreCase(trimmedCategory)) {
                    eq.displayInfo();
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No equipment found in category: " + trimmedCategory);
            }

        } catch (Exception e) {
            System.out.println("Error filtering by category: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void viewAllEquipment() {
        try {
            if (equipmentList == null || equipmentList.isEmpty()) {
                System.out.println("No equipment available in inventory.");
                return;
            }

            System.out.println("=== All Equipment ===");
            int validCount = 0;

            for (TrackableEquipment eq : equipmentList) {
                if (eq != null) {
                    eq.displayInfo();
                    validCount++;
                } else {
                    System.out.println("Warning: Null equipment entry detected.");
                }
            }

            System.out.println("Total: " + validCount + " equipment(s)");

        } catch (Exception e) {
            System.out.println("Error viewing equipment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        Writer writer = null;
        try {
            if (equipmentList == null) {
                System.out.println("Error: Equipment list is null, cannot save.");
                return;
            }

            File file = new File("equipment.json");
            if (file.exists()) {
                File backup = new File("equipment.json.backup");
                if (file.renameTo(backup)) {
                    System.out.println("Backup created.");
                }
            }

            writer = new FileWriter("equipment.json");
            gson.toJson(equipmentList, writer);
            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            System.out.println("Check if you have write permissions.");
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("Unexpected error during save: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing file writer: " + e.getMessage());
                }
            }
        }
    }

    public void loadFromFile() {
        Reader reader = null;
        try {
            File file = new File("equipment.json");

            if (!file.exists()) {
                System.out.println("No saved data found. Starting with empty inventory.");
                return;
            }

            if (!file.canRead()) {
                System.out.println("Error: Cannot read file. Check permissions.");
                return;
            }

            if (file.length() == 0) {
                System.out.println("Warning: Data file is empty. Starting fresh.");
                return;
            }

            reader = new FileReader(file);
            TrackableEquipment[] equipments = gson.fromJson(reader, TrackableEquipment[].class);

            if (equipments == null || equipments.length == 0) {
                System.out.println("No equipment data found in file.");
                equipmentList = new ArrayList<>();
            } else {
                equipmentList = new ArrayList<>(Arrays.asList(equipments));
                System.out.println("Successfully loaded " + equipments.length + " equipment(s).");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with empty inventory.");
            equipmentList = new ArrayList<>();

        } catch (com.google.gson.JsonSyntaxException e) {
            System.out.println("Error: Invalid JSON format in data file.");
            System.out.println("The file may be corrupted.");
            e.printStackTrace();
            equipmentList = new ArrayList<>();

        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
            e.printStackTrace();
            equipmentList = new ArrayList<>();

        } catch (Exception e) {
            System.out.println("Unexpected error loading data: " + e.getMessage());
            e.printStackTrace();
            equipmentList = new ArrayList<>();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing file reader: " + e.getMessage());
                }
            }
        }
    }
}