package carsharing.menu.customer;

import carsharing.db.H2db;
import carsharing.entity.Customer;

public abstract class CustomerOption {

    protected Customer customer;

    public CustomerOption(Customer customer) {

        this.customer = customer;
    }

    public void reloadCustomerAndExecute() {
        H2db h2db = H2db.getInstance("");
        h2db.reloadCustomerByName(customer.getName())
                .ifPresentOrElse(value -> this.customer=value
                        ,() -> System.out.format("Cannot reload customer - %s",this.customer.getName()));
        action();
    }

    public abstract void action();
}
