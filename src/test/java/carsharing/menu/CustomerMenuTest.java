package carsharing.menu;

import carsharing.entity.Customer;
import carsharing.menu.customer.CustomerOption;
import carsharing.menu.customer.RentCar;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMenuTest {

    @Test
    void getCustomerMenuOptionsTest() {

        Customer customer = new Customer(1,"test",1);
        CustomerMenu customerMenu = new CustomerMenu();
        CustomerOption customerOption = customerMenu.getCustomerMenuOptions(1,customer);

        assertThat(customerOption.getClass()).isEqualTo(RentCar.class);
    }



}