package com.javamicroproject.bhagyashri.impl;

import java.sql.SQLException;
import java.util.Scanner;

public class DisplayOptions {
    public DisplayOptions() throws SQLException {
        Registration registration = new Registration();
        Login userLogin = new Login();
        System.out.println("Please select one option: ");
        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        System.out.println();
        switch (choice) {
            case 1:
                registration.register();
                new DisplayOptions();
                break;
            case 2:
                userLogin.login();
                new DisplayOptions();
                break;
            case 3:
                System.out.println("Thank you!!!");
                System.out.println();
                System.out.println("Exiting program...");
                System.out.println();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                System.out.println();
                new DisplayOptions();
                break;
        }
        scanner.close();
    }
}
