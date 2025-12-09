package labtrack;

public interface EquipmentOperations {
    boolean borrowEquipment(String user, int quantity, String date);
    boolean returnEquipment(String user, int quantity, String date);
    boolean markDamaged(String user, int quantity, String date);
    void displayInfo();
}