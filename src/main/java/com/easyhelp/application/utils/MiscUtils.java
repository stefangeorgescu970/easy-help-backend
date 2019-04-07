package com.easyhelp.application.utils;

import com.easyhelp.application.model.dto.booking.AvailableDate;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.util.*;

public class MiscUtils {

    private static int OPEN_HOUR = 6;
    private static int CLOSE_HOUR = 13;
    private static int SCHEDULE_PERIOD = 6;

    private static Boolean validateSsn(String ssn) {
        return true;
    }

    public static Date getDateFromSsn(String ssn) throws SsnInvalidException {
        if (validateSsn(ssn)) {
            return new Date();
        } else {
            throw new SsnInvalidException("Provided SSN is not valid.");
        }
    }

    public static List<AvailableDate> getAllHoursForWeek(Date date) {
        List<AvailableDate> dates = new ArrayList<>();

        for (int day = 0; day < SCHEDULE_PERIOD; day++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, day);
            List<Date> hours = new ArrayList<>();
            calendar.add(Calendar.HOUR_OF_DAY, OPEN_HOUR);

            for (int hour = OPEN_HOUR; hour < CLOSE_HOUR; hour++) {
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                hours.add(calendar.getTime());
            }
            dates.add(new AvailableDate(calendar.getTime(), hours));
        }

        return dates;
    }
}
