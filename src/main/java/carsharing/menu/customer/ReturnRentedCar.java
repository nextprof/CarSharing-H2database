package carsharing.menu.customer;

import carsharing.db.H2db;
import carsharing.entity.Customer;

public class ReturnRentedCar extends CustomerOption {

    public ReturnRentedCar(Customer customer) {
        super(customer);
    }

    @Override
    public void action() {
        H2db h2db = H2db.getInstance("");
        if(customer.getRentedCarId()==0)
        {
            System.out.println("You didn't rent a car!");
        }
        else {
            h2db.clearCustomerRentedCarId(customer);
            System.out.println("You've returned a rented car!");
        }
    }

}
