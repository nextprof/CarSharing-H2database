package carsharing.menu.customer;

import carsharing.entity.Car;
import carsharing.entity.Customer;

import java.util.Map;
import java.util.Optional;

import static carsharing.menu.DefaultMenu.menuChoiceParser;

public class RentCar extends CustomerOption {

    public RentCar(Customer customer) {
        super(customer);
    }

    @Override
    public void action() {
        if(customer.getRentedCarId()!=0)
        {
            System.out.println("You've already rented a car!");
        }else {
            if (h2db.areCompaniesAvailable()) {

                System.out.println("Choose a company:");
                h2db.printAllCompanies();
                System.out.println("0. Back");

                int numberOfCompany = menuChoiceParser(100);
                if (numberOfCompany != 0) {
                    String companyName = h2db.getCompanyNameById(numberOfCompany);
                    if (h2db.areCarsAvailable(companyName)) {
                        System.out.println("Choose a car:");
                        Optional<Map<Integer, Car>> map = h2db.getCompanyAvailableToRentCars(companyName);
                        map.ifPresent(value -> {
                            System.out.format("%s's cars\n", companyName);
                            Car.printCars(value);
                        });
                        System.out.println("0. Back");
                        int choice = menuChoiceParser(100);
                        if (choice != 0) {
                            map.ifPresent(value -> {
                                Car car = value.get(choice);
                                h2db.setCustomerRentedCarId(customer, h2db.getCarId(car));
                                System.out.format("You rented '%s'\n", car.getName());
                            });
                        }
                    } else {
                        System.out.println("The car list is empty!");
                    }
                }
            } else {
                System.out.println("The company list is empty!");
            }
        }
    }

    public Customer getCustomer() {
        return customer;
    }

}
