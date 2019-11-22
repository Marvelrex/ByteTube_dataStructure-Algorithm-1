package com.luoExpress.demo;

/**
 * 0 1 1 2 3 5 8 13 21...
 */
public class Fib {
    //recursion
    public static int fib1(int n){
        //1.basic case--->
        //2.recursion body
        if (n <= 1) {
            return n;
        }

        return  fib1(n-1)+fib1(n-2);
    }

    //unrecursion
    public static int fib2(int n){//5n+1  4n+4 5n-1 5n-2 4n 4n+1
        int first = 0;//1
        int second = 1;//1
        for (int i = 0; i <n-1 ; i++) {//1+(n-1)*(1+1+1)+(n-1)*2+1= 5n-1 = O(n)
            int sum = first+second;
            first = second;
            second = sum;
        }
        return second;
    }

    public static void main(String[] args) {
        System.out.println(fib1(4));
        System.out.println(fib2(4));
    }
}
