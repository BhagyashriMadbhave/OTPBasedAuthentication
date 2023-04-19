package com.javamicroproject.bhagyashri.impl;

import com.javamicroproject.bhagyashri.dto.User;
import com.javamicroproject.bhagyashri.util.Utility;

import java.sql.SQLException;
import java.util.Scanner;


public class Login {
    public void login() {
        try (Scanner scanner = new Scanner(System.in)) {
            Utility utility = new Utility();
            System.out.println("Enter your email: ");
            String email = scanner.nextLine();
            System.out.println();
            if (utility.isValidEmail(email) && utility.isEmailPresent(email)) {
                if (utility.generateOTP(email)) {
                    System.out.println("OTP sent to your email. Please enter the OTP: ");
                    int enteredOTP = scanner.nextInt();
                    System.out.println();
                    if (utility.verifyOTP(email, enteredOTP)) {
                        User user = utility.getUserByEmail(email);
                        System.out.println("Login successful!");
                        System.out.println();
                        System.out.println("====================================================");
                        System.out.println("Name: " + user.getName());
                        System.out.println();
                        System.out.println("Number: " + user.getNumber());
                        System.out.println();
                        System.out.println("Email: " + user.getEmail());
                        System.out.println("====================================================");
                        System.out.println();
                    } else {
                        System.out.println("Invalid OTP or OTP has expired.");
                        System.out.println();
                    }
                }
            } else {
                System.out.println("Invalid email or User not found with email address. Please try again.");
                System.out.println();
                System.out.println("Your entered email is: " + email);
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
