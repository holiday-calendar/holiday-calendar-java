package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.testng.Assert.*;

public class HolidayDateTest {

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "holiday is marked non-null .+")
    public void testConstructor_NullHoliday() {
        LocalDate date = LocalDate.now();
        new HolidayDate(null, date);
        fail("Expected NullPointerException");
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "date is marked non-null .+")
    public void testConstructor_NullDate() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        new HolidayDate(christmas, null);
        fail("Expected NullPointerException");
    }

    @Test
    public void testGetHoliday() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDay = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDay);
        assertEquals(christmas2021.getHoliday(), christmas);
    }

    @Test
    public void testGetDate() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDay = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDay);
        assertEquals(christmas2021.getDate(), christmasDay);
    }

    @Test
    public void testEquals() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas1 = new HolidayDate(christmas, christmasDate);
        HolidayDate christmas2 = new HolidayDate(christmas, christmasDate);

        Holiday boxingDay = new FixedHoliday("Boxing Day", "Day after Christmas", Month.DECEMBER, 26);
        LocalDate boxingDayDate = LocalDate.of(2021, Month.DECEMBER, 26);
        HolidayDate boxingDay2021 = new HolidayDate(boxingDay, boxingDayDate);

        Object notaHolidayDate = new Object();

        assertEquals(christmas1, christmas1);
        assertEquals(christmas2, christmas1);
        assertNotEquals(boxingDay2021, christmas1);
        assertNotEquals(notaHolidayDate, christmas1);
    }

    @Test
    public void testHashCode() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas1 = new HolidayDate(christmas, christmasDate);
        HolidayDate christmas2 = new HolidayDate(christmas, christmasDate);

        assertEquals(christmas2.hashCode(), christmas1.hashCode());
    }

    @Test
    public void testToString() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDate);

        String expected = "HolidayDate(holiday=Holiday[name='Christmas', description='Celebration of birth of Christ', month=DECEMBER, dayOfMonth=25], date=2021-12-25)";
        assertEquals(christmas2021.toString(), expected);
    }
}