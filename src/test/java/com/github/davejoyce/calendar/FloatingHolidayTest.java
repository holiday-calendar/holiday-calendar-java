package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;
import com.github.davejoyce.calendar.holiday.EasterMonday;
import com.github.davejoyce.calendar.holiday.GoodFriday;
import com.github.davejoyce.calendar.holiday.WesternEaster;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

@Slf4j
public class FloatingHolidayTest {

    @Test(dataProvider = "data")
    public void testDateForYear(String name, Observance observance, int yearToCalculate, LocalDate expected) {
        FloatingHoliday holiday = new FloatingHoliday(name, "", observance);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), expected);

        log.debug("{} {}: {}", name, yearToCalculate, actual.get());
    }

    @Test
    public void testGetObservance() {

    }

    @DataProvider
    public Iterator<Object[]> data() {
        final EasterObservance easter = new WesternEaster();
        final Observance goodFriday = new GoodFriday(easter);
        final Observance easterMonday = new EasterMonday(easter);
        final Observance thanksGiving = (year) -> Year.of(year)
                                                      .atMonth(Month.NOVEMBER)
                                                      .atDay(1)
                                                      .with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));

        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Easter", easter, 1918, LocalDate.of(1918, Month.MARCH, 31)});
        data.add(new Object[]{ "Good Friday", goodFriday, 1918, LocalDate.of(1918, Month.MARCH, 29)});
        data.add(new Object[]{ "Easter Monday", easterMonday, 1918, LocalDate.of(1918, Month.APRIL, 1)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 1918, LocalDate.of(1918, Month.NOVEMBER, 28)});
        data.add(new Object[]{ "Easter", easter, 2021, LocalDate.of(2021, Month.APRIL,  4)});
        data.add(new Object[]{ "Good Friday", goodFriday, 2021, LocalDate.of(2021, Month.APRIL, 2)});
        data.add(new Object[]{ "Easter Monday", easterMonday, 2021, LocalDate.of(2021, Month.APRIL, 5)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 2021, LocalDate.of(2021, Month.NOVEMBER, 25)});
        return data.iterator();
    }

}