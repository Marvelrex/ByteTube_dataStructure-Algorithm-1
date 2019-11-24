package com.luoExpress.demo;

public class GetMax {
    public static int getMax(int[] arr,int left,int right){
        if (left == right) {
            return arr[left];
        }

        int mid = left+((right-left)>>1);
        int leftMax = getMax(arr,left,mid);
        int rightMax = getMax(arr,mid+1,right);
        return Math.max(leftMax,rightMax);

    }
}
