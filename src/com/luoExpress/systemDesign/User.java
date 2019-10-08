package com.luoExpress.systemDesign;

public abstract class User {
    private String name;
    private double balance;

    public User(){}


    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public abstract void redBag();

    public void showBalance(){
        System.out.println(getBalance());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
