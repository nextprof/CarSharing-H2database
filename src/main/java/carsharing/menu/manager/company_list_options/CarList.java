package carsharing.menu.manager.company_list_options;

import carsharing.db.H2db;
import carsharing.entity.Car;
import carsharing.menu.Menu;

import java.util.Map;
import java.util.Optional;

public class CarList implements Menu {

    private final String companyName;

    public CarList(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void show() {
        H2db h2db = H2db.getInstance("");

        Optional<Map<Integer, Car>> map = h2db.getCompanyCars(companyName);
        map.ifPresentOrElse(Car::printCars,() -> System.out.println("The car list is empty!"));

    }
}
