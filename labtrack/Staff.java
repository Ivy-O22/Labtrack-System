package labtrack;

public class Staff extends User {

    public Staff(String username, String password) {
        super(username, password, "STAFF");
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== STAFF MENU =====");
        System.out.println("1. Add Equipment");
        System.out.println("2. Borrow Equipment");
        System.out.println("3. Return Equipment");
        System.out.println("4. Mark Equipment as Damaged");
        System.out.println("5. View All Equipment (Full Details)");
        System.out.println("6. Search Equipment");
        System.out.println("7. Filter by Category");
        System.out.println("8. Filter by Status");
        System.out.println("9. View Borrow History");
        System.out.println("0. Logout");
        System.out.print("Enter choice: ");
    }
}