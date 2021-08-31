package carsharing.menu;

public class StartMenuFactory {

    public Menu getMenuOrCreateCustomer(int choice) {
        switch(choice) {
            case 1: return new ManagerMenu();
            case 2: return new CustomerMenu();
            case 3: return new CreateCustomer();
            case 0: System.exit(0);
            default: return new BackMenu();
        }
    }
}
