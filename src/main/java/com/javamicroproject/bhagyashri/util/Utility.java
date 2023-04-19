package com.javamicroproject.bhagyashri.util;

import com.javamicroproject.bhagyashri.dbconnection.DatabaseConnection;
import com.javamicroproject.bhagyashri.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

public class Utility {

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    public boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean isValidName(String name) {
        return name.matches("[A-Za-z\\s]+");
    }

    public boolean isValidNumber(String number) {
        return number.matches("\\d{10}");
    }

    public boolean insertUser(User user) throws SQLException {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (name, number, email) VALUES (?, ?, ?)")) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getNumber());
            statement.setString(3, user.getEmail());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean generateOTP(String email) {
        Utility utility = new Utility();
        User user = new User();
        user.setOtp((int)(Math.random() * 9000) + 1000);
        user.setEmail(email);
        user.setOtpTime(new Date().getTime());
        Boolean userUpdateResult = utility.updateUser(user);
        Boolean sendOtpResult = sendOTP(email, user.getOtp());
        if (!userUpdateResult && !sendOtpResult) {
            return Boolean.FALSE;
        } else if (!userUpdateResult) {
            return Boolean.FALSE;
        } else if (!sendOtpResult) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean sendOTP(String email, int otp) {
        String from = "javamicroproject@gmail.com";
        String password = "ckmsxzesggxgnxvx";
        String to = email;
        String subject = "OTP for login";
        String body = "Your OTP is " + otp;
        Properties properties = new Properties();
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.port", "465");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.starttls.enable", "true");
        properties.put("mail.smtps.ssl.enable", "true");
        properties.put("mail.smtps.ssl.trust", "*");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");

        // Set the SSL/TLS protocol and cipher suite
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Somthing Went Wrong!!! Please try again");
            e.printStackTrace();
            System.out.println("Error : "+e.getMessage());
            System.out.println("Cause : "+e.getCause());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean verifyOTP(String email, int otp) {
        User user = getUserByEmail(email);
        if (user != null && user.getOtp() == otp) {
            Instant instant = Instant.ofEpochMilli(user.getOtpTime());
            LocalDateTime otpTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime otpTimePlusFiveMin = otpTime.plusMinutes(5);
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isBefore(otpTimePlusFiveMin)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public User getUserByEmail(String email) {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setName(rs.getString("name"));
                user.setNumber(rs.getString("number"));
                user.setEmail(rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setOtp(rs.getInt("otp"));
                user.setOtpTime(rs.getLong("otpTime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(conn, stmt, rs);
        }
        return user;
    }

    public Boolean updateUser(User user) {
        boolean success = Boolean.FALSE;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement("UPDATE users SET otp = ?, otpTime = ? WHERE email = ?");
            stmt.setInt(1, user.getOtp());
            stmt.setLong(2, user.getOtpTime());
            stmt.setString(3, user.getEmail());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                success = Boolean.TRUE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(conn, stmt, null);
        }
        return success;
    }

    public boolean isEmailPresent(String email) {
       return getUserCount(email) != 0;
    }

    private int getUserCount(String email) {
        int count = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(conn, stmt, rs);
        }
        return count;
    }
}
