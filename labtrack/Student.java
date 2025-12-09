package labtrack;

public class Student extends User {
    public Student(String username, String password){
        super(username, password, "STUDENT");
    }


    @Override
    public void showMenu() {
        System.out.println("\n===== STUDENT MENU =====");
        System.out.println("1. View Available Equipment");
        System.out.println("2. Borrow Equipment");
        System.out.println("3. Return Equipment");
        System.out.println("4. Mark Equipment as Damaged");
        System.out.println("5. View My Borrowed Equipment");
        System.out.println("0. Logout");
        System.out.print("Enter choice: ");
    }
}