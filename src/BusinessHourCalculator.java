import java.time.DayOfWeek;
import java.util.*;

public class BusinessHourCalculator {

    List<WeekDay> weekDays;
    SortedMap<String, WeekDay> specialDays;

    BusinessHourCalculator(final String defaultOpeningTime, final String defaultClosingTime) {
        weekDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDays.add(new WeekDay(true, defaultOpeningTime, defaultClosingTime));
        }
        specialDays = new TreeMap<>();
    }

    public void setOpeningHours(final DayOfWeek dayOfWeek, final String openingTime, final String closingTime) {
        weekDays.get(dayOfWeek.getValue() - 1).updateOpeningHours(openingTime, closingTime);
    }

    public void setOpeningHours(final String date, final String openingTime, final String closingTime) {
        specialDays.put(date, new WeekDay(true, openingTime, closingTime));
    }

    public void setClosed(final DayOfWeek... daysOfWeek) {
        for (final DayOfWeek day : daysOfWeek) {
            weekDays.get(day.getValue() - 1).setOpen(false);
        }
    }

    public void setClosed(final String... dates) {
        for (final String date : dates){
            specialDays.put(date, new WeekDay());
        }
    }

    public Date calculateDeadline(final long duration, final String startTime) {
        return null;
    }

}
