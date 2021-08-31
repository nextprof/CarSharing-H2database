package carsharing;

public class Main {

    public static void main(String[] args)  {
        new CarSharing(args.length==2 ? args[1] : "standardDbName");
    }
}
