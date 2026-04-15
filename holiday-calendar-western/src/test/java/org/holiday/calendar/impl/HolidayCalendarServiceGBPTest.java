package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HolidayCalendarServiceGBPTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "GBP";

    public HolidayCalendarServiceGBPTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        // Names chosen to distinguish GBP from other calendars:
        // Early May BH and Spring BH are absent from EUR, US, CA, CH, DE, FR, AU.
        // Summer BH confirms UK-family membership.
        // Boxing Day is absent from EUR, US, CA.
        final Object[] earlyMayBankHoliday = {"Early May Bank Holiday"};
        final Object[] springBankHoliday   = {"Spring Bank Holiday"};
        final Object[] summerBankHoliday   = {"Summer Bank Holiday"};
        final Object[] boxingDay           = {"Boxing Day"};
        return Arrays.asList(earlyMayBankHoliday, springBankHoliday, summerBankHoliday, boxingDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // --- New Year's Day rolling (GBP-specific fix; UK calendar does NOT roll NYD) ---
        // 2021: Jan 1 = Friday  → no roll
        final Object[] nyd2021 = {2021, "New Year's Day", LocalDate.of(2021, Month.JANUARY,  1)};
        // 2022: Jan 1 = Saturday → +2 → Monday Jan 3
        final Object[] nyd2022 = {2022, "New Year's Day", LocalDate.of(2022, Month.JANUARY,  3)};
        // 2023: Jan 1 = Sunday  → +1 → Monday Jan 2
        final Object[] nyd2023 = {2023, "New Year's Day", LocalDate.of(2023, Month.JANUARY,  2)};
        // 2024: Jan 1 = Monday  → no roll
        final Object[] nyd2024 = {2024, "New Year's Day", LocalDate.of(2024, Month.JANUARY,  1)};

        // --- Christmas Day rolling ---
        // 2020: Dec 25 = Friday   → no roll
        final Object[] xmas2020 = {2020, "Christmas Day", LocalDate.of(2020, Month.DECEMBER, 25)};
        // 2021: Dec 25 = Saturday → +2 → Monday Dec 27
        final Object[] xmas2021 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 27)};
        // 2022: Dec 25 = Sunday   → +2 → Tuesday Dec 27
        final Object[] xmas2022 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 27)};
        // 2023: Dec 25 = Monday   → no roll
        final Object[] xmas2023 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};

        // --- Boxing Day rolling ---
        // 2020: Dec 26 = Saturday → +2 → Monday Dec 28
        final Object[] boxing2020 = {2020, "Boxing Day", LocalDate.of(2020, Month.DECEMBER, 28)};
        // 2021: Dec 26 = Sunday   → +2 → Tuesday Dec 28
        final Object[] boxing2021 = {2021, "Boxing Day", LocalDate.of(2021, Month.DECEMBER, 28)};
        // 2022: Dec 26 = Monday   → not on a weekend; lambda not invoked → no roll
        final Object[] boxing2022 = {2022, "Boxing Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        // 2023: Dec 26 = Tuesday  → no roll
        final Object[] boxing2023 = {2023, "Boxing Day", LocalDate.of(2023, Month.DECEMBER, 26)};
        // 2026: Dec 26 = Saturday → +2 → Monday Dec 28 (second "Boxing Day only" scenario)
        final Object[] boxing2026 = {2026, "Boxing Day", LocalDate.of(2026, Month.DECEMBER, 28)};

        // --- Good Friday (floating, NOT rollable — always a Friday) ---
        // 2021: Easter Sunday Apr 4  → Good Friday Apr 2
        final Object[] goodFriday2021 = {2021, "Good Friday", LocalDate.of(2021, Month.APRIL,  2)};
        // 2024: Easter Sunday Mar 31 → Good Friday Mar 29
        final Object[] goodFriday2024 = {2024, "Good Friday", LocalDate.of(2024, Month.MARCH, 29)};

        // --- Easter Monday (floating, NOT rollable — always a Monday) ---
        // 2021: Easter Sunday Apr 4  → Easter Monday Apr 5
        final Object[] easterMonday2021 = {2021, "Easter Monday", LocalDate.of(2021, Month.APRIL,  5)};
        // 2024: Easter Sunday Mar 31 → Easter Monday Apr 1
        final Object[] easterMonday2024 = {2024, "Easter Monday", LocalDate.of(2024, Month.APRIL,  1)};

        // --- Early May Bank Holiday (1st Monday in May, NOT rollable) ---
        // 1995: VE Day 50th anniversary override → May 8
        final Object[] earlyMay1995 = {1995, "Early May Bank Holiday", LocalDate.of(1995, Month.MAY,  8)};
        // 2020: VE Day 75th anniversary override → May 8
        final Object[] earlyMay2020 = {2020, "Early May Bank Holiday", LocalDate.of(2020, Month.MAY,  8)};
        // 2021: May 3  (May 1 = Saturday → 1st Monday = May 3)
        final Object[] earlyMay2021 = {2021, "Early May Bank Holiday", LocalDate.of(2021, Month.MAY,  3)};
        // 2023: May 1  (May 1 itself is a Monday — earliest possible date)
        final Object[] earlyMay2023 = {2023, "Early May Bank Holiday", LocalDate.of(2023, Month.MAY,  1)};
        // 2024: May 6  (May 1 = Wednesday → 1st Monday = May 6)
        final Object[] earlyMay2024 = {2024, "Early May Bank Holiday", LocalDate.of(2024, Month.MAY,  6)};

        // --- Spring Bank Holiday (last Monday in May, NOT rollable) ---
        // 2021: May 31
        final Object[] spring2021 = {2021, "Spring Bank Holiday", LocalDate.of(2021, Month.MAY,  31)};
        // 2022: June 2 — Platinum Jubilee override encoded in SpringBankHoliday observance
        final Object[] spring2022 = {2022, "Spring Bank Holiday", LocalDate.of(2022, Month.JUNE,  2)};
        // 2023: May 29
        final Object[] spring2023 = {2023, "Spring Bank Holiday", LocalDate.of(2023, Month.MAY,  29)};

        // --- Summer Bank Holiday (last Monday in August, NOT rollable) ---
        // 2022: Aug 29
        final Object[] summer2022 = {2022, "Summer Bank Holiday", LocalDate.of(2022, Month.AUGUST, 29)};
        // 2024: Aug 26
        final Object[] summer2024 = {2024, "Summer Bank Holiday", LocalDate.of(2024, Month.AUGUST, 26)};
        // 2026: Aug 31
        final Object[] summer2026 = {2026, "Summer Bank Holiday", LocalDate.of(2026, Month.AUGUST, 31)};

        return Arrays.asList(
                nyd2021, nyd2022, nyd2023, nyd2024,
                xmas2020, xmas2021, xmas2022, xmas2023,
                boxing2020, boxing2021, boxing2022, boxing2023, boxing2026,
                goodFriday2021, goodFriday2024,
                easterMonday2021, easterMonday2024,
                earlyMay1995, earlyMay2020, earlyMay2021, earlyMay2023, earlyMay2024,
                spring2021, spring2022, spring2023,
                summer2022, summer2024, summer2026
        ).listIterator();
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        assertEquals(calendar.getHolidays().size(), 8,
                "GBP calendar must define exactly 8 recurring CHAPS closure days");
    }

    @Test
    public void testNewYearsDayRoll_SaturdayCase() {
        // 2022: Jan 1 = Saturday → observed Monday Jan 3
        // This distinguishes GBP from UK, which does NOT correctly roll New Year's Day.
        final HolidayCalendar calendar = factory.create(CODE);
        final Optional<HolidayDate> found = calendar.calculate(2022).stream()
                .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent());
        assertEquals(found.get().getDate(), LocalDate.of(2022, Month.JANUARY, 3));
    }

    @Test
    public void testNewYearsDayRoll_SundayCase() {
        // 2023: Jan 1 = Sunday → observed Monday Jan 2
        final HolidayCalendar calendar = factory.create(CODE);
        final Optional<HolidayDate> found = calendar.calculate(2023).stream()
                .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent());
        assertEquals(found.get().getDate(), LocalDate.of(2023, Month.JANUARY, 2));
    }

    @Test
    public void testChristmasAndBoxingDayBothRoll_2021() {
        // 2021: Dec 25 = Saturday (+2 → Mon Dec 27), Dec 26 = Sunday (+2 → Tue Dec 28)
        final HolidayCalendar calendar = factory.create(CODE);
        final List<HolidayDate> holidays = calendar.calculate(2021);

        final Optional<HolidayDate> christmas = holidays.stream()
                .filter(hd -> "Christmas Day".equals(hd.getHoliday().getName()))
                .findFirst();
        final Optional<HolidayDate> boxing = holidays.stream()
                .filter(hd -> "Boxing Day".equals(hd.getHoliday().getName()))
                .findFirst();

        assertTrue(christmas.isPresent());
        assertTrue(boxing.isPresent());
        assertEquals(christmas.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 27));
        assertEquals(boxing.get().getDate(),    LocalDate.of(2021, Month.DECEMBER, 28));
    }

}