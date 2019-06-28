package com.easyhelp.application.utils;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.misc.SsnData;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.time.*;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class MiscUtils {

    private static int OPEN_HOUR = 4; // UTC TIME for 7 AM
    private static int CLOSE_HOUR = 10; // UTC TIME for 13 AM
    private static int SCHEDULE_PERIOD = 7;

    private static int getDigitFromString(String s, int position) {
        return Integer.parseInt(String.valueOf(s.charAt(position)));
    }

    public static void validateSsn(String ssn) throws SsnInvalidException {
        if (System.getenv("ENV").equals("LOCAL")) {
            if (ssn.length() < 7)
                throw new SsnInvalidException("Incorrect length.");

            int year = getDigitFromString(ssn, 1) * 10 + getDigitFromString(ssn, 2);
            int month = getDigitFromString(ssn, 3) * 10 + getDigitFromString(ssn, 4);
            int day = getDigitFromString(ssn, 5) * 10 + getDigitFromString(ssn, 6);
            if (month > 12 || day > 31)
                throw new SsnInvalidException("Incorrect date of birth in ssn.");
        } else {
            String testKey = "279146358279";

            if (ssn.length() != 13)
                throw new SsnInvalidException("Incorrect length.");

            int firstDigit = getDigitFromString(ssn, 0);
            if (firstDigit < 0 || firstDigit > 6)
                throw new SsnInvalidException("Incorrect first digit.");

            int year = getDigitFromString(ssn, 1) * 10 + getDigitFromString(ssn, 2);
            int month = getDigitFromString(ssn, 3) * 10 + getDigitFromString(ssn, 4);
            int day = getDigitFromString(ssn, 5) * 10 + getDigitFromString(ssn, 6);
            if (month > 12 || day > 31)
                throw new SsnInvalidException("Incorrect date of birth in ssn.");

            int sum = 0;
            for (int i = 0; i < testKey.length(); i++) {
                sum += getDigitFromString(ssn, i) * getDigitFromString(testKey, i);
            }

            sum %= 11;
            int control = sum > 10 ? 1 : sum;
            if (control != getDigitFromString(ssn, 12))
                throw new SsnInvalidException("Invalid ssn.");
        }
    }

    public static SsnData getDataFromSsn(String ssn) {
        SsnData ssnData = new SsnData();

        int firstDigit = getDigitFromString(ssn, 0);
        ssnData.setIsMale(firstDigit % 2 == 1);

        int year = getDigitFromString(ssn, 1) * 10 + getDigitFromString(ssn, 2);
        int month = getDigitFromString(ssn, 3) * 10 + getDigitFromString(ssn, 4);
        int day = getDigitFromString(ssn, 5) * 10 + getDigitFromString(ssn, 6);

        if (firstDigit == 1 || firstDigit == 2)
            year = 1900 + year;
        else if (firstDigit == 3 || firstDigit == 4)
            year = 1800 + year;
        else if (firstDigit == 5 || firstDigit == 6)
            year = 2000 + year;

        LocalDate bd = LocalDate.of(year, month, day);
        ssnData.setDateOfBirth(bd);

        return ssnData;
    }

    public static List<AvailableDate> getAllHoursForWeek() {
        List<AvailableDate> dates = new ArrayList<>();

        for (int day = 0; day < SCHEDULE_PERIOD; day++) {
            ZonedDateTime localDate = ZonedDateTime.now();
            localDate = localDate.plusDays(day);


            if (localDate.getDayOfWeek() != DayOfWeek.SATURDAY && localDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                List<ZonedDateTime> hours = new ArrayList<>();

                ZonedDateTime current = ZonedDateTime.of(localDate.toLocalDate(), LocalTime.of(OPEN_HOUR, 0), ZoneId.of("UTC"));

                while (current.getHour() < CLOSE_HOUR) {
                    hours.add(current);
                    current = current.plusMinutes(20);
                }

                dates.add(new AvailableDate(localDate, hours));
            } else
                dates.add(new AvailableDate(localDate, Collections.emptyList()));
        }

        return dates;
    }

    public static Double computeDistance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0D;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return dist;
        }
    }

}
