package com.example.test2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DBContextHolderTest {
    private DBContextHolder dBContextHolder;

    @Test
    public void getCurrentDb_Ok() {
        DBContextHolder.setCurrentDb(1);
        Assertions.assertEquals(1, DBContextHolder.getCurrentDb());
    }

    @Test
    public void clear_Ok() {
        DBContextHolder.clear();
        Assertions.assertEquals(0, DBContextHolder.getCurrentDb());
    }
}
