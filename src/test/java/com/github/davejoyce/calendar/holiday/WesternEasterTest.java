package com.github.davejoyce.calendar.holiday;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class WesternEasterTest {

    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data")
    public void testApply(int yearToCalculate, LocalDate expected) {
        LocalDate actual = westernEaster.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1583, LocalDate.of(1583, Month.APRIL, 10) });
        data.add(new Object[]{ 1776, LocalDate.of(1776, Month.APRIL,  7) });
        data.add(new Object[]{ 1918, LocalDate.of(1918, Month.MARCH, 31) });
        data.add(new Object[]{ 2000, LocalDate.of(2000, Month.APRIL, 23) });
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.APRIL,  4) });
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.APRIL, 17) });

        return data.iterator();
    }

}