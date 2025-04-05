package Model;

public class Admin extends User{
    public Admin() {
        super();
    }

    @Override
    public void showList() {
        System.out.println("\n1. Add New Car");
        System.out.println("2. View Cars");
        System.out.println("3. Update Car");
        System.out.println("4. Delete Car");
        System.out.println("5. Add New Admin");
        System.out.println("6. Show Rents");
        System.out.println("7. Quit\n");
    }
}
