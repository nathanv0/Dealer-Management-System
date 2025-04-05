package Model;

public class Client extends User{
    public Client() {
        super();
    }

    @Override
    public void showList() {
        System.out.println("\n1. View Car");
        System.out.println("2. Rent Cars");
        System.out.println("3. Return Car");
        System.out.println("4. Show My Rent");
        System.out.println("5. Edit My Data");
        System.out.println("6. Quit\n");

    }
}
