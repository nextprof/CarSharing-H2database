package carsharing.db;

import carsharing.entity.Car;
import carsharing.entity.Customer;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class H2db {

    private final String URL;
    public static H2db instance;

    public void testConnectionAndCreateCompanyTable() throws SQLException {
        System.out.println("Connecting to database... --> "+ URL);
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String sql =  "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT auto_increment PRIMARY KEY , " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL) ";

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        }
    }

    private void createCarTable() throws SQLException {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String sql =  "CREATE TABLE IF NOT EXISTS CAR " +
                    "(ID INT auto_increment PRIMARY KEY , " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL ," +
                    "COMPANY_ID INT NOT NULL," +
                    " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";

            stmt.executeUpdate(sql);
        }
    }

    private void createCustomerTable() throws SQLException {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String sql =  "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INT auto_increment PRIMARY KEY , " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL ," +
                    "RENTED_CAR_ID INT," +
                    " FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";

            stmt.executeUpdate(sql);
        }
    }

    private H2db(String path) throws SQLException {
        this.URL = "jdbc:h2:./src/db/"+ path;
        testConnectionAndCreateCompanyTable();
        createCarTable();
        createCustomerTable();
        startIndexIDwith1();
    }


    public static H2db getInstance(String path){
        try {
            if (instance == null) {
                instance = new H2db(path);
            }
        }catch (Exception e) {
            System.out.println("Error get instance DB");
            System.out.println(e.getMessage());
        }
        return instance;
    }

    public void saveCompany(String companyName) {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String sql = "INSERT INTO COMPANY(NAME) VALUES('"+ companyName + "')";
            stmt.executeUpdate(sql);
            System.out.println(companyName + " has been inserted to DB");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveCar(String carName,String companyName) {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            int companyId = getIdByCompanyName(companyName);

            String sql = "INSERT INTO CAR (NAME,COMPANY_ID) " +
                    "VALUES('"+ carName + "','"+companyId+"')";
            stmt.executeUpdate(sql);
            System.out.println(carName + " has been inserted to DB");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveCustomer(String customerName) {

        String sql = "INSERT INTO CUSTOMER (NAME) VALUES(?)";

        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setString(1, customerName);
            stmt.executeUpdate();
            System.out.println(customerName + " has been inserted to DB");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean areCustomersAvailable() {
        String sql = "SELECT * FROM CUSTOMER";
        return hasRecord(sql);
    }

    public int getCarId(Car car) {
        String selectSQL = String.format("SELECT * FROM CAR WHERE NAME = '%s'",car.getName());
        return getId(selectSQL);
    }

    private int getIdByCompanyName(String companyName) {
        String selectSQL = "SELECT * FROM COMPANY WHERE NAME = " + "'" + companyName + "'";
        return getId(selectSQL);
    }
    public int getId(String query) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            try (ResultSet cars = preparedStatement.executeQuery()) {
                if(cars.next()) {
                    return cars.getInt("ID");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public String getCompanyNameById(int companyID) {
        String selectSQL = "SELECT * FROM COMPANY WHERE ID = "+ companyID;
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet companies = preparedStatement.executeQuery()) {
                if(companies.next()) {
                    return companies.getString("NAME");
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public Optional<Customer> reloadCustomerByName(String name) {
        String selectSQL = String.format("SELECT * FROM CUSTOMER WHERE NAME = '%s'",name);
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet customer = preparedStatement.executeQuery()) {
                if(customer.next()) {
                    int id = customer.getInt("ID");
                    int carID = customer.getInt("RENTED_CAR_ID");
                    return Optional.of(new Customer(id,name,carID));
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    public void printAllCompanies() {
        String selectSQL = "SELECT * FROM COMPANY";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet companies = preparedStatement.executeQuery()) {
                while(companies.next()) {
                    int id = companies.getInt("ID");
                    String name = companies.getString("NAME");
                    System.out.println(id + ". " + name);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean areCompaniesAvailable() {
        String selectSQL = "SELECT * FROM COMPANY";
        return hasRecord(selectSQL);
    }

    public boolean areCarsAvailable(String companyName) {
        String selectSQL = "SELECT C.ID,C.NAME FROM CAR C JOIN COMPANY COM " +
                "WHERE COMPANY_ID=COM.ID and COM.NAME="
                + "'"+companyName + "'";

        return hasRecord(selectSQL);
    }

    public boolean hasRecord(String query) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            try (ResultSet set = preparedStatement.executeQuery()) {
                if(set.next()) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Optional<Map<Integer, Car>> getCompanyCars(String companyName) {
        String selectSQL = "SELECT C.ID,C.NAME,COMPANY_ID FROM CAR C JOIN COMPANY COM " +
                "WHERE COMPANY_ID=COM.ID and COM.NAME="
                + "'"+companyName + "'";

        int counter = 1;
        boolean flag = false;
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet cars = preparedStatement.executeQuery()) {
                Map<Integer,Car> map =  new HashMap<>();
                while(cars.next()) {
                    if(counter==1)
                        System.out.format("%s's cars\n",companyName);
                    String name = cars.getString("NAME");
                    int dbId = cars.getInt("ID");
                    int companyId = cars.getInt("COMPANY_ID");
                    map.put(counter++,new Car(dbId,name,companyId));
                    flag = true;
                }
                if(flag)
                    return Optional.of(map);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<Map<Integer, Car>> getCompanyAvailableToRentCars(String companyName) {
        String selectSQL = String.format("SELECT C.id, C.name, C.company_id FROM CAR C\n" +
                "    JOIN COMPANY ON COMPANY.NAME = '%s' and c.COMPANY_ID = COMPANY.ID\n" +
                "    LEFT JOIN CUSTOMER ON C.id = CUSTOMER.rented_car_id WHERE CUSTOMER.rented_car_id is null;",companyName);
        boolean flag = false;
        int counter = 1;
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet cars = preparedStatement.executeQuery()) {
                Map<Integer,Car> map =  new HashMap<>();
                while(cars.next()) {

                    String name = cars.getString("NAME");
                    int dbId = cars.getInt("ID");
                    int companyId = cars.getInt("COMPANY_ID");
                    map.put(counter++,new Car(dbId,name,companyId));
                    flag = true;
                }
                if(flag){
                    System.out.println();
                    return Optional.of(map);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public void startIndexIDwith1() {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String sql = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";

            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setCustomerRentedCarId(Customer customer,int id) {
        String selectSQL = String.format("UPDATE CUSTOMER SET RENTED_CAR_ID = %d WHERE NAME = '%s'"
                ,id,customer.getName());

        executeStatement(selectSQL);
    }

    public void clearCustomerRentedCarId(Customer customer) {
        String selectSQL = String.format("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE NAME = '%s'"
                ,customer.getName());
        executeStatement(selectSQL);
    }

    public void executeStatement(String query) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Optional<Map<Integer, Customer>> getAllCustomers() {
        String selectSQL = "SELECT * FROM CUSTOMER";

        int counter = 1;
        int id;
        boolean flag = false;
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet customers = preparedStatement.executeQuery()) {
                Map<Integer, Customer> map = new HashMap<>();
                while(customers.next()) {

                    id = customers.getInt("ID");
                    String name = customers.getString("NAME");
                    int rentedCarId = customers.getInt("RENTED_CAR_ID");
                    map.put(counter++,new Customer(id,name,rentedCarId));
                    flag=true;
                }
                if(flag)
                    return Optional.of(map);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Car> getCustomerCar(Customer customer) {
        String selectSQL = String.format("SELECT * FROM CAR WHERE ID = %d",customer.getRentedCarId());

        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL)) {
            try (ResultSet cars = preparedStatement.executeQuery()) {
                if(cars.next()) {
                    int id = cars.getInt("ID");
                    String name = cars.getString("NAME");
                    int companyId = cars.getInt("COMPANY_ID");
                    return Optional.of(new Car(id,name,companyId));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();

    }
}
