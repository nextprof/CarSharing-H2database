package carsharing.menu.manager.company_list_options;

import carsharing.db.H2db;
import carsharing.menu.Menu;

import java.util.Scanner;

public class CreateCar implements Menu {

    String companyName;

    public CreateCar(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void show() {
        System.out.println("Enter the car name:");
        Scanner scn = new Scanner(System.in);
        String carName = scn.nextLine();
        H2db h2db = H2db.getInstance("");
        h2db.saveCar(carName, companyName);
    }

}
