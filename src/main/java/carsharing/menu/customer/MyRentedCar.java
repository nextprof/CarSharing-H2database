package carsharing.menu.customer;

import carsharing.entity.Car;
import carsharing.entity.Customer;

import java.util.Optional;

public class MyRentedCar extends CustomerOption {

    public MyRentedCar(Customer customer) {
        super(customer);
    }

    @Override
    public void action() {
        Optional<Car> car = h2db.getCustomerCar(customer);
        car.ifPresentOrElse(value -> {
            System.out.println("Your rented car:");
            System.out.println(value.getName());
            System.out.println("Company:");
            String companyName = h2db.getCompanyNameById(value.getCompanyId());
            System.out.println(companyName);
            System.out.println();
        }, () -> System.out.println("You didn't rent a car!") );
    }

}
