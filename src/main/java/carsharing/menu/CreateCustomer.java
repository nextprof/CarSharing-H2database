package carsharing.menu;

import carsharing.db.H2db;
import carsharing.menu.Menu;

import java.util.Scanner;

public class CreateCustomer implements Menu {

    @Override
    public void show() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        H2db h2db = H2db.getInstance("");
        h2db.saveCustomer(customerName);
    }
}
