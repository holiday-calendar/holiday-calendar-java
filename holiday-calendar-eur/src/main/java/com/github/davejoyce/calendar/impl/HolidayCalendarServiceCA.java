package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarService;
import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.observance.EasterMonday;
import com.github.davejoyce.calendar.observance.GoodFriday;
import com.github.davejoyce.calendar.observance.WesternEaster;
import com.github.davejoyce.calendar.observance.ca.FamilyDay;
import com.github.davejoyce.calendar.observance.ca.LabourDay;
import com.github.davejoyce.calendar.observance.ca.Thanksgiving;
import com.github.davejoyce.calendar.observance.ca.VictoriaDay;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static com.github.davejoyce.calendar.HolidayCalendar.STANDARD_WEEKEND;

public class HolidayCalendarServiceCA implements HolidayCalendarService {

    private static final String CODE = "CA";
    private static final String NAME = "Canada National Holidays";

    @Override
    public boolean isProvided(String code) {
        return CODE.equalsIgnoreCase(code);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
        final Holiday familyDay = Holiday.builder()
                .name("Family Day")
                .description("Day to spend time with the family")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new FamilyDay())
                .build();
        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Friday before Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(easter))
                .build();
        final Holiday easterMonday = Holiday.builder()
                .name("Easter Monday")
                .description("Monday after Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EasterMonday(easter))
                .build();
        final Holiday victoriaDay = Holiday.builder()
                .name("Victoria Day")
                .description("Official celebration of birthday of Canada's Sovereign")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new VictoriaDay())
                .build();
        final Holiday canadaDay = Holiday.builder()
                .name("Canada Day")
                .description("Anniversary of Canadian Confederation")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.JULY, 1)
                .rollable(true)
                .build();
        final Holiday labourDay = Holiday.builder()
                .name("Labour Day")
                .description("Celebration of workers in Canada")
                .type(Holiday.Type.FLOATING)
                .observance(new LabourDay())
                .rollable(false)
                .build();
        final Holiday nationalDayForTruthAndReconciliation = Holiday.builder()
                .name("National Day For Truth and Reconciliation")
                .description("Recognition of the legacy of the Canadian Indian residential school system")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.SEPTEMBER, 30)
                .rollable(true)
                .build();
        final Holiday thanksgiving = Holiday.builder()
                .name("Thanksgiving Day")
                .description("National day for giving thanks")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new Thanksgiving())
                .build();
        final Holiday remembranceDay = Holiday.builder()
                .name("Remembrance Day")
                .description("Commemoration of armed forces members who have died in the line of duty")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.NOVEMBER, 11)
                .rollable(false)
                .build();
        final Holiday christmas = Holiday.builder()
                .name("Christmas")
                .description("Christmas Day")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 25)
                .rollable(true)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 26)
                .rollable(true)
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(dateToRoll -> {
                    final Optional<LocalDate> christmasDate = christmas.dateForYear(dateToRoll.getYear());
                    final Optional<LocalDate> boxingDayDate = boxingDay.dateForYear(dateToRoll.getYear());
                    final boolean isChristmas = christmasDate.isPresent() && dateToRoll.equals(christmasDate.get());
                    final boolean isBoxingDay = boxingDayDate.isPresent() && dateToRoll.equals(boxingDayDate.get());
                    switch (dateToRoll.getDayOfWeek()) {
                        case SATURDAY:
                        case SUNDAY:
                            return (isChristmas || isBoxingDay) ? dateToRoll.plusDays(2L) : dateToRoll;
                        default:
                            return dateToRoll;
                    }
                })
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(familyDay)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(victoriaDay)
                .holiday(canadaDay)
                .holiday(labourDay)
                .holiday(nationalDayForTruthAndReconciliation)
                .holiday(thanksgiving)
                .holiday(remembranceDay)
                .holiday(christmas)
                .holiday(boxingDay)
                .build();
    }

}
