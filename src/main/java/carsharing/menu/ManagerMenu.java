package carsharing.menu;

import carsharing.menu.manager.CompanyList;
import carsharing.menu.manager.CreateCompany;

import static carsharing.menu.DefaultMenu.menuChoiceParser;

public class ManagerMenu implements Menu{
    @Override
    public void show() {
        int choice;
        do {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            choice = menuChoiceParser( 100);
            Menu menu = getManagerMenuOptions(choice);
            menu.show();
        }while(choice!=0);
    }

    public Menu getManagerMenuOptions(int choice) {
        switch (choice) {
            case 1:
                return new CompanyList();
            case 2:
                return new CreateCompany();
            default:
                return new BackMenu();
        }
    }
}
