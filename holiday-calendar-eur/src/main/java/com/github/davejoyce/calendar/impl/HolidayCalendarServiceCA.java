package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarService;
import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.observance.EasterMonday;
import com.github.davejoyce.calendar.observance.GoodFriday;
import com.github.davejoyce.calendar.observance.WesternEaster;
import com.github.davejoyce.calendar.observance.ca.LabourDay;
import com.github.davejoyce.calendar.observance.ca.Thanksgiving;
import com.github.davejoyce.calendar.observance.ca.VictoriaDay;

import java.time.Month;

import static com.github.davejoyce.calendar.HolidayCalendar.NO_ROLL;

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
                .rollable(false)
                .build();
        final Holiday labourDay = Holiday.builder()
                .name("Labour Day")
                .description("Celebration of workers in Canada")
                .type(Holiday.Type.FLOATING)
                .observance(new LabourDay())
                .rollable(false)
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
                .description("Commemoration to honour armed forces members who have died in the line of duty")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.NOVEMBER, 11)
                .rollable(false)
                .build();
        final Holiday christmas = Holiday.builder()
                .name("Christmas")
                .description("Christmas Day")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 25)
                .rollable(false)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 26)
                .rollable(false)
                .build();
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(NO_ROLL) // TODO Update this roll convention
                .holiday(newYearsDay)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(victoriaDay)
                .holiday(canadaDay)
                .holiday(labourDay)
                .holiday(thanksgiving)
                .holiday(remembranceDay)
                .holiday(christmas)
                .holiday(boxingDay)
                .build();
    }

}
