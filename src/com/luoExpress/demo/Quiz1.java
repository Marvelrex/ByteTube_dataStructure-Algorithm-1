package com.luoExpress.demo;

public class Quiz1 {
    public static int fib(int n) {//O(2^n)
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static int fib2(int n) {//5n-1 O(N)
        if (n <= 1) {
            return n;//1
        }
        int first = 0;//1
        int second = 1;//1
        for (int i = 0; i < n - 1; i++) {//1+3*(n-1)+2(n-1)
            int sum = first + second;
            first = second;
            second = sum;
        }
        return second;//1

    }
}
