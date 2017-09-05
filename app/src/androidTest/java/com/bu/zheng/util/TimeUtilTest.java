package com.bu.zheng.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by BuZheng on 2017/7/18.
 */

public class TimeUtilTest {

    @Test
    public void test1() {
        Calendar calendar = Calendar.getInstance();

        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);

        int add = 7;

        if (day_of_week > 2) {
            add = 7 - (day_of_week - 2);

        } else if (day_of_week < 2) {
            add = 2 - day_of_week;
        }

        calendar.add(Calendar.DAY_OF_YEAR, add);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        System.out.println(sdf.format(calendar.getTime()));

    }

    @Test
    public void test2() {
        HashSet hashSet = new HashSet();


    }

    class A {
        @Override
        public boolean equals(Object obj) {
            return true;
        }
    }

    class B {
        @Override
        public int hashCode() {
            return 1;
        }
    }

    class C {
        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
