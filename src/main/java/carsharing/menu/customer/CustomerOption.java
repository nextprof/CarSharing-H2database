package carsharing.menu.customer;

import carsharing.db.H2db;
import carsharing.entity.Customer;

public abstract class CustomerOption {

    protected Customer customer;
    protected H2db h2db;

    public CustomerOption(Customer customer) {
        this.customer = customer;
        this.h2db = H2db.getInstance("");
    }

    public void reloadCustomerAndExecute() {
        h2db.reloadCustomerByName(customer.getName())
                .ifPresentOrElse(value -> this.customer=value
                        ,() -> System.out.format("Cannot reload customer - %s",this.customer.getName()));
        action();
    }

    public abstract void action();
}
