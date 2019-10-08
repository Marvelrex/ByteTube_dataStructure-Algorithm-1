package com.luoExpress.systemDesign;

public class Sender extends User {
    private int number;
    private double amount;

    public Sender(){}


    public Sender(String name, double balance, int number, double amount) {
        super(name, balance);
        this.number = number;
        this.amount = amount;
    }



    @Override
    public void redBag() {
        send(number,amount);
    }

    private void send(int number, double amount) {
        if (this.getBalance() >= number*amount) {
            this.setBalance(this.getBalance()-number*amount);
        }

    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
