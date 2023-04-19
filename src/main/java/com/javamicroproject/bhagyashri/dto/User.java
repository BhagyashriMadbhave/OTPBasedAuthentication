package com.javamicroproject.bhagyashri.dto;

public class User {
    private int id;
    private String name;
    private String number;
    private String email;
    private int otp;
    private Long otpTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public long getOtpTime() {
        return otpTime;
    }

    public void setOtpTime(long otpTime) {
        this.otpTime = otpTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
