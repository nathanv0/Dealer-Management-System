package Controller;

import Model.Operation;
import Model.Database;
import Model.User;

import java.util.Scanner;

public class AddNewAdmin implements Operation {

    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter First Name:");
        String firstName = s.next();
        System.out.println("Enter Last Name:");
        String lastName = s.next();
        System.out.println("Enter Email:");
        String email = s.next();
        System.out.println("Enter Phone Number:");
        String phoneNumber = s.next();
        System.out.println("Enter Password:");
        String password = s.next();
        System.out.println("Confirm Password:");
        String confirmPassword = s.next();

        // Loop until the password and confirmation match
        while (!password.equals(confirmPassword)) {
            System.out.println("Passwords don't match. Please try again.");
            System.out.println("Enter Password:");
            password = s.next();
            System.out.println("Confirm Password:");
            confirmPassword = s.next();
        }
        int accType = 1;

    }

}
