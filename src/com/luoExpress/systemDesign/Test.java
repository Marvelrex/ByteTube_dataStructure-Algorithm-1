package com.luoExpress.systemDesign;

public class Test {
    public static void main(String[] args) {
        User sender = new Sender("dal", 1000, 2, 1);
        User reciever1 = new Reciever("david", 100, ((Sender)sender).getAmount());
        User reciever2 = new Reciever("tony", 100, ((Sender)sender).getAmount());
        sender.redBag();
        reciever1.redBag();
        reciever2.redBag();
        sender.showBalance();
        reciever1.showBalance();
        reciever2.showBalance();
    }
}
