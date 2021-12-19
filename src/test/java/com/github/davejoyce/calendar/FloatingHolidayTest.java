package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;
import com.github.davejoyce.calendar.holiday.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test(dataProvider = "boundaryCheckData")
    public void testDateForYear_OutsideBoundary(String name, Observance observance, int yearToCalculate) {
        FloatingHoliday holiday = new FloatingHoliday(name, "", observance);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertFalse(actual.isPresent());
    }

    @Test
    public void testGetObservance() {
        final Observance thanksGiving = (year) -> Year.of(year)
                                                      .atMonth(Month.NOVEMBER)
                                                      .atDay(1)
                                                      .with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        FloatingHoliday holiday = new FloatingHoliday("Thanksgiving", "Thanksgiving (US)", thanksGiving);
        assertEquals(holiday.getObservance(), thanksGiving);
    }

    @Test
    public void testEquals() {
        final Observance observance = new GoodFriday(new WesternEaster());
        final Observance justNewYear = (year) -> LocalDate.of(year, Month.JANUARY, 1);
        FloatingHoliday holiday1 = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", observance);
        FloatingHoliday holiday2 = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", observance);
        FloatingHoliday holiday3 = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", justNewYear);
        FloatingHoliday holiday4 = new FloatingHoliday("Good Friday", "Friday before Easter Sunday", observance);
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
        final Observance observance = new GoodFriday(new WesternEaster());
        FloatingHoliday holiday1 = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", observance);
        FloatingHoliday holiday2 = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", observance);
        assertEquals(holiday2.hashCode(), holiday1.hashCode());
    }

    @Test
    public void testToString() {
        final Observance observance = new GoodFriday(new WesternEaster());
        FloatingHoliday holiday = new FloatingHoliday("Good Friday", "Day of Crucifixion of Jesus Christ", observance);
        String expected = "Holiday\\[name='Good Friday', description='Day of Crucifixion of Jesus Christ', observance=.+]";
        Pattern pattern = Pattern.compile(expected);
        Matcher matcher = pattern.matcher(holiday.toString());
        assertTrue(matcher.matches());
        log.debug("FloatingHoliday (string): {}", holiday);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        final EasterObservance easter = new WesternEaster();
        final Observance goodFriday = new GoodFriday(easter);
        final Observance easterMonday = new EasterMonday(easter);
        final Observance whitSunday = new WhitSunday(easter);
        final Observance thanksGiving = (year) -> Year.of(year)
                                                      .atMonth(Month.NOVEMBER)
                                                      .atDay(1)
                                                      .with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));

        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Easter", easter, 1918, LocalDate.of(1918, Month.MARCH, 31)});
        data.add(new Object[]{ "Good Friday", goodFriday, 1918, LocalDate.of(1918, Month.MARCH, 29)});
        data.add(new Object[]{ "Easter Monday", easterMonday, 1918, LocalDate.of(1918, Month.APRIL, 1)});
        data.add(new Object[]{ "Whit Sunday", whitSunday, 1918, LocalDate.of(1918, Month.MAY, 19)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 1918, LocalDate.of(1918, Month.NOVEMBER, 28)});
        data.add(new Object[]{ "Easter", easter, 2021, LocalDate.of(2021, Month.APRIL,  4)});
        data.add(new Object[]{ "Good Friday", goodFriday, 2021, LocalDate.of(2021, Month.APRIL, 2)});
        data.add(new Object[]{ "Easter Monday", easterMonday, 2021, LocalDate.of(2021, Month.APRIL, 5)});
        data.add(new Object[]{ "Whit Sunday", whitSunday, 2021, LocalDate.of(2021, Month.MAY, 23)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 2021, LocalDate.of(2021, Month.NOVEMBER, 25)});
        return data.iterator();
    }

    @DataProvider
    public Iterator<Object[]> boundaryCheckData() {
        final EasterObservance easter = new OrthodoxEaster();
        final Observance goodFriday = new GoodFriday(easter);
        final Observance easterMonday = new EasterMonday(easter);
        final Observance whitSunday = new WhitSunday(easter);

        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Easter", easter, 529 });
        data.add(new Object[]{ "Good Friday", goodFriday, 529 });
        data.add(new Object[]{ "Easter Monday", easterMonday, 529 });
        data.add(new Object[]{ "Whit Sunday", whitSunday, 529 });

        return data.iterator();
    }

}