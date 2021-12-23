package com.github.davejoyce.calendar;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

@Slf4j
public class FixedHolidayTest {

    @Test
    public void testConstructor_StringMonthAndDay_3Args() {
        FixedHoliday holiday = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        assertEquals(holiday.getMonth(), Month.JULY);
        assertEquals(holiday.getDayOfMonth(), 4);
        assertTrue(holiday.isRollable());
    }

    @Test(dataProvider = "data")
    public void testDateForYear(String name, Month month, int day, int yearToCalculate, LocalDate expected) {
        FixedHoliday holiday = new FixedHoliday(name, "", month, day);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), expected);

        log.debug("{} {}: {}", name, yearToCalculate, actual.get());
    }

    @Test
    public void testEquals() {
        FixedHoliday holiday1 = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        FixedHoliday holiday2 = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        FixedHoliday holiday3 = new FixedHoliday("Fourth of July", "US Independence Day", "--07-05");
        FixedHoliday holiday4 = new FixedHoliday("Fourth of July", "Independence Day", "--07-04");
        Object notAHoliday = new Object();

        assertEquals(holiday1, holiday1);
        assertEquals(holiday2, holiday1);
        assertNotEquals(holiday1, null);
        assertNotEquals(notAHoliday, holiday1);
        assertNotEquals(holiday3, holiday1);
        assertNotEquals(holiday4, holiday1);
    }

    @Test
    public void testHashCode() {
        FixedHoliday holiday1 = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        FixedHoliday holiday2 = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        assertEquals(holiday2.hashCode(), holiday1.hashCode());
    }

    @Test
    public void testToString() {
        FixedHoliday holiday = new FixedHoliday("Fourth of July", "US Independence Day", "--07-04");
        String expected = "Holiday[name='Fourth of July', description='US Independence Day', month=JULY, dayOfMonth=4]";
        assertEquals(holiday.toString(), expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "New Years Day", Month.JANUARY, 1, 2021, LocalDate.of(2021, Month.JANUARY, 1)});
        data.add(new Object[]{ "Fourth of July", Month.JULY, 4, 1976, LocalDate.of(1976, Month.JULY, 4)});
        return data.iterator();
    }

}