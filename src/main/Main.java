package main;


import bank.ATM;
import bank.BankSystem;

public class Main {
    public static void main(String[] args) {
        BankSystem bankSystem = new BankSystem();
        ATM atm = new ATM(bankSystem);
        atm.start();
    }
}
