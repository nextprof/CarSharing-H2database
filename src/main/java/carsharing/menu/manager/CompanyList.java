package carsharing.menu.manager;

import carsharing.db.H2db;
import carsharing.menu.BackMenu;
import carsharing.menu.Menu;
import carsharing.menu.manager.company_list_options.CarList;
import carsharing.menu.manager.company_list_options.CreateCar;

import static carsharing.menu.DefaultMenu.menuChoiceParser;

public class CompanyList implements Menu {

    @Override
    public void show() {
        H2db h2db = H2db.getInstance("");
        if(h2db.areCompaniesAvailable()) {

            System.out.println("Choose a company:");
            h2db.printAllCompanies();
            System.out.println("0. Back");
            int numberOfCompany = menuChoiceParser(100);
            if(numberOfCompany!=0) {
                String companyName = h2db.getCompanyNameById(numberOfCompany);
                companyMenu(companyName);
            }
        }
        else{
            System.out.println("The company list is empty!");
        }
    }
    public void companyMenu(String companyName){
        int choice;
        do {
            System.out.println("'" + companyName + "' company:");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            choice = menuChoiceParser(100);
            Menu menu = getCompanyMenuOptions(choice,companyName);
            menu.show();

        }while(choice!=0);
    }


    public Menu getCompanyMenuOptions(int choice, String companyName) {
        switch (choice) {
            case 1:
                return new CarList(companyName);
            case 2:
                return new CreateCar(companyName);
            default:
                return new BackMenu();
        }
    }

}
