/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

/**
 *
 * @author eobregon
 */
public class CommonDateFunctions {

    public static java.util.Date getDateFromString(String string) {
        // Creating an instant object
        java.util.Date date = null;

        try {
            // Parsing the string to Date
            Instant timestamp = Instant.parse(string);
            date = java.util.Date.from(timestamp);
            System.out.println("date " + java.util.Date.from(timestamp));

        } catch (Exception e) {
            System.out.println("logic.CommonDateFunctions.getDateFromString() error " + e.getMessage());
        }

        // Returning the converted timestamp
        return date;
    }

    public static int getWeekOfTheYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance(AppStaticValues.localeCrCurrency);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        return weekOfYear;
    }

    public static java.util.Date getLastWeekStartDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate lastWeekStart = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        try {
            return dateFormat.parse(lastWeekStart.toString());
        } catch (Exception e) {
            return null;
        }

    }

    public static java.util.Date getLastWeekEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate lastWeekEnd = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        try {
            return dateFormat.parse(lastWeekEnd.toString());
        } catch (Exception e) {
            return null;
        }

    }
}
