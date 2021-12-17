package com.github.davejoyce.calendar.holiday;

import com.github.davejoyce.calendar.holiday.OrthodoxEaster;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;


public class OrthodoxEasterTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();

    @Test(dataProvider = "data")
    public void testApply(int yearToCalculate, LocalDate expected) {
        LocalDate actual = orthodoxEaster.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1583, LocalDate.of(1583, Month.APRIL, 10) }); // Same as Western
        data.add(new Object[]{ 1776, LocalDate.of(1776, Month.APRIL, 14) });
        data.add(new Object[]{ 1918, LocalDate.of(1918, Month.MAY,    5) });
        data.add(new Object[]{ 1919, LocalDate.of(1919, Month.APRIL, 20) }); // Same as Western
        data.add(new Object[]{ 2000, LocalDate.of(2000, Month.APRIL, 30) });
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.MAY,    2) });
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.APRIL, 24) });

        return data.iterator();
    }

}