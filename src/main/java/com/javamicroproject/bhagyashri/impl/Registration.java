package com.javamicroproject.bhagyashri.impl;

import com.javamicroproject.bhagyashri.dto.User;
import com.javamicroproject.bhagyashri.util.Utility;

import java.sql.SQLException;
import java.util.Scanner;

public class Registration {
    public Boolean register() throws SQLException {
        Utility utility = new Utility();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println();
        if(!utility.isValidName(name)){
            System.out.println("Invalid name entered. Please try again.");
            System.out.println();
            return Boolean.FALSE;
        }

        System.out.println("Enter your number: ");
        String number = scanner.nextLine();
        System.out.println();
        if(!utility.isValidNumber(number)){
            System.out.println("Invalid number entered. Please try again.");
            System.out.println();
            return Boolean.FALSE;
        }

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println();
        if(utility.isValidEmail(email)){
            if(utility.isEmailPresent(email)){
                System.out.println("Entered email is already present in DB.");
                System.out.println();
                return Boolean.FALSE;
            }
        }else{
            System.out.println("Invalid email entered. Please try again.");
            System.out.println();
            return Boolean.FALSE;
        }

        User user = new User();
        user.setName(name);
        user.setNumber(number);
        user.setEmail(email);
        if(utility.insertUser(user)){
            System.out.println("Registration successful!");
            System.out.println();
            return Boolean.TRUE;
        }else{
            System.out.println("Internal Server Error. Please try again");
            System.out.println();
            return Boolean.FALSE;
        }

    }
}
