package com.easyhelp.application.utils;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.util.*;

public class MiscUtils {

    private static int OPEN_HOUR = 7;
    private static int CLOSE_HOUR = 13;
    private static int SCHEDULE_PERIOD = 7;

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

            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

                List<Date> hours = new ArrayList<>();
                calendar.set(Calendar.HOUR_OF_DAY, OPEN_HOUR);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                hours.add(calendar.getTime());
                for (int hour = OPEN_HOUR; hour < CLOSE_HOUR; hour++) {
                    for (int j = 0; j < 3; j++) {
                        calendar.add(Calendar.MINUTE, 20);
                        hours.add(calendar.getTime());
                    }
                }
                hours.remove(hours.size() - 1);

                dates.add(new AvailableDate(calendar.getTime(), hours));
            } else
                dates.add(new AvailableDate(calendar.getTime(), Collections.emptyList()));
        }

        return dates;
    }
}
