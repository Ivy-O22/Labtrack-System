package labtrack;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager manager = new InventoryManager();
    private static InventoryActions actions = new InventoryActions(manager);
    private static User currentUser = null;

    private static Map<String, User> users = new HashMap<>();

    static {
        users.put("staff1", new Staff("staff1", "staff123"));
        users.put("student1", new Student("student1", "pass123"));
    }

    public static void main(String[] args) {
        manager.loadFromFile();

        System.out.println("===== LabTrack Inventory System =====");
        System.out.println("Please login to continue\n");

        while (currentUser == null) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            login(username, password);
            System.out.println();
        }

        int choice = -1;
        do {
            currentUser.showMenu(); // Polymorphic call!

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            if (currentUser instanceof Staff) {
                handleStaffChoice(choice);
            } else if (currentUser instanceof Student) {
                handleStudentChoice(choice);
            }

        } while (choice != 0);

        logout();
    }

    private static void login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + username + " (" + user.getRole() + ")");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getUsername() + "!");
            currentUser = null;
        }
    }

    // ========== STAFF HANDLERS ==========
    private static void handleStaffChoice(int choice) {
        switch (choice) {
            case 1 -> addEquipmentMenu();
            case 2 -> borrowEquipmentMenu();
            case 3 -> returnEquipmentMenu();
            case 4 -> markDamagedMenu();
            case 5 -> manager.viewAllEquipment();
            case 6 -> searchEquipmentMenu();
            case 7 -> filterByCategoryMenu();
            case 8 -> filterByStatusMenu();
            case 9 -> viewBorrowHistoryMenu();
            case 0 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void addEquipmentMenu() {
        System.out.print("Enter equipment name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        actions.addOrUpdateEquipment(name, category, qty);
    }

    private static void searchEquipmentMenu() {
        System.out.print("Enter equipment name or ID: ");
        String keyword = scanner.nextLine();
        manager.searchEquipment(keyword);
    }

    private static void filterByCategoryMenu() {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        manager.filterByCategory(category);
    }

    private static void filterByStatusMenu() {
        System.out.print("Enter status: ");
        String status = scanner.nextLine();
        manager.filterByStatus(status);
    }

    private static void viewBorrowHistoryMenu() {
        System.out.print("Enter equipment name: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (TrackableEquipment eq : manager.getAllEquipment()) {
            if (eq.getName().equalsIgnoreCase(name)) {
                eq.displayInfo();
                found = true;
                break;
            }
        }
        if (!found) System.out.println("Equipment not found.");
    }

    // ========== STUDENT HANDLERS ==========
    private static void handleStudentChoice(int choice) {
        switch (choice) {
            case 1 -> viewAvailableEquipment();
            case 2 -> borrowEquipmentMenu();
            case 3 -> returnEquipmentMenu();
            case 4 -> markDamagedMenu();
            case 5 -> viewMyBorrowedEquipment();
            case 0 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void viewAvailableEquipment() {
        System.out.println("\n===== Available Equipment =====");
        boolean hasEquipment = false;
        for (TrackableEquipment eq : manager.getAllEquipment()) {
            if (eq.getAvailableQuantity() > 0) {
                System.out.println("- - - - -");
                System.out.println("Name      : " + eq.getName());
                System.out.println("Category  : " + eq.getCategory());
                System.out.println("Available : " + eq.getAvailableQuantity());
                System.out.println("Status    : " + eq.getStatus());
                hasEquipment = true;
            }
        }
        if (!hasEquipment) {
            System.out.println("No equipment available.");
        }
        System.out.println("- - - - -");
    }

    private static void viewMyBorrowedEquipment() {
        String username = currentUser.getUsername();
        System.out.println("\n===== My Borrowed Equipment =====");
        boolean hasBorrowed = false;

        for (TrackableEquipment eq : manager.getAllEquipment()) {
            if (eq.getBorrowers().containsKey(username)) {
                System.out.println("- - - - -");
                System.out.println("Equipment : " + eq.getName());
                System.out.println("Category  : " + eq.getCategory());
                System.out.println("Borrowed  : " + eq.getBorrowers().get(username));
                hasBorrowed = true;
            }
        }

        if (!hasBorrowed) {
            System.out.println("You have no borrowed equipment.");
        }
        System.out.println("- - - - -");
    }

    // ========== SHARED HANDLERS ==========
    private static void borrowEquipmentMenu() {
        System.out.print("Enter equipment name: ");
        String name = scanner.nextLine();
        String user = currentUser.getUsername();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        actions.borrowEquipment(name, user, qty, date);
    }

    private static void returnEquipmentMenu() {
        System.out.print("Enter equipment name: ");
        String name = scanner.nextLine();
        String user = currentUser.getUsername();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        actions.returnEquipment(name, user, qty, date);
    }

    private static void markDamagedMenu() {
        System.out.print("Enter equipment name: ");
        String name = scanner.nextLine();
        String user = currentUser.getUsername();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        actions.markDamaged(name, user, qty, date);
    }
}