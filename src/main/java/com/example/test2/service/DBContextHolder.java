package com.example.test2.service;


public class DBContextHolder {
    private static final ThreadLocal<Integer> contextHolder = ThreadLocal.withInitial(()->0);

    public static void setCurrentDb(Integer dbIndex) {
        contextHolder.set(dbIndex);
    }

    public static Integer getCurrentDb() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
