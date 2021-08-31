package carsharing;

import carsharing.db.H2db;
import carsharing.menu.DefaultMenu;

public class CarSharing {

    public CarSharing(String pathToDatabase) {
        H2db.getInstance(pathToDatabase);
        new DefaultMenu();
    }
}
