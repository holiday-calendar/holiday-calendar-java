package com.github.davejoyce.calendar;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class FixedHolidayTest {

    @Test(dataProvider = "data")
    public void testDateForYear(String name, Month month, int day, int yearToCalculate, LocalDate expected) {
        FixedHoliday holiday = new FixedHoliday(name, "", month, day);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "New Years Day 2021", Month.JANUARY, 1, 2021, LocalDate.of(2021, Month.JANUARY, 1)});
        data.add(new Object[]{ "Fourth of July 1976", Month.JULY, 4, 1976, LocalDate.of(1976, Month.JULY, 4)});
        return data.iterator();
    }

}