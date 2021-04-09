package com.hellofresh.task2.di;

public class JMMTest {
    public static synchronized void sync(){

    }

    public static void notsync(){
        Object obj = new Object();
        synchronized (obj) {

        }
    }
}
