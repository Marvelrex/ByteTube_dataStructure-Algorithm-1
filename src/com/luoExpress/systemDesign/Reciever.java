package com.luoExpress.systemDesign;

public class Reciever extends User {
    private double money;

    public Reciever(){}

    public Reciever(String name, double balance, double money) {
        super(name, balance);
        this.money = money;
    }

    @Override
    public void redBag() {
        recieve(money);
    }

    private void recieve(double money) {
        this.setBalance(this.getBalance()+money);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
