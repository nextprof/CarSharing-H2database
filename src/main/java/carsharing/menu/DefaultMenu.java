package carsharing.menu;

import java.util.Scanner;

public class DefaultMenu {

    public DefaultMenu() {
        startMenu();
    }

    public void startMenu() {
        while(true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            int choice = menuChoiceParser(100);

            StartMenuFactory menuFactory = new StartMenuFactory();
            Menu menu = menuFactory.getMenuOrCreateCustomer(choice);
            menu.show();

        }
    }

    public static int menuChoiceParser(int defaultVal) {
        try {
            Scanner scanner = new Scanner(System.in);
            String value = scanner.next();
            return Integer.parseInt(value);
        } catch (NumberFormatException e)
        {
            return defaultVal;
        }
    }
}