package carsharing.menu;

import carsharing.db.H2db;
import carsharing.entity.Customer;
import carsharing.menu.customer.*;

import java.util.Map;
import java.util.Optional;

import static carsharing.menu.DefaultMenu.menuChoiceParser;

public class CustomerMenu implements Menu {
    @Override
    public void show() {
        H2db h2db = H2db.getInstance("");
        if (h2db.areCustomersAvailable()) {
            System.out.println("Choose a customer:");
            Optional<Map<Integer, Customer>> customers = h2db.getAllCustomers();
            customers.ifPresent(Customer::printCustomers);
            System.out.println("0. Back");
            int numberOfCustomer = menuChoiceParser(100);
            if(numberOfCustomer!=0) {
                customers.ifPresent(v -> {
                    Customer customer = v.get(numberOfCustomer);
                    customerMenu(customer);
                });
            }
        }
        else {
            System.out.println("The customer list is empty!");
        }
    }

    public void customerMenu(Customer customer){
        int choice;
        do {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            choice = menuChoiceParser(100);
            CustomerOption menu = getCustomerMenuOptions(choice,customer);
            menu.reloadCustomerAndExecute();

        }while(choice!=0);
    }

    public CustomerOption getCustomerMenuOptions(int choice, Customer customer) {
        switch (choice) {
            case 1:
                return new RentCar(customer);
            case 2:
                return new ReturnRentedCar(customer);
            case 3:
                return new MyRentedCar(customer);
            default:
                return new BackMenuCustomer(customer);
        }
    }
}
