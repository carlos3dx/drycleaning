import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BusinessHourCalculator {

    private List<WeekDay> weekDays;
    private SortedMap<String, WeekDay> specialDays;

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
        for (final String date : dates) {
            specialDays.put(date, new WeekDay());
        }
    }

    public Date calculateDeadline(final long duration, final String startTime) throws ParseException {
        final DateTimeFormatter formatter00 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        final DateTimeFormatter formatter0 = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm", Locale.ENGLISH);

        return calculateDeadline(duration, LocalDateTime.parse(startTime, startTime.length() == 15 ? formatter0 : formatter00));
    }

    public Date calculateDeadline(final long duration, final LocalDateTime startTime) {
        Date result = new Date();
        long timeRemaining = duration;
        int pointerDay = startTime.getDayOfWeek().getValue() - 1;
        LocalTime time = startTime.toLocalTime();
        LocalDate date = startTime.toLocalDate();
        WeekDay day = specialDays.getOrDefault(date.toString(), weekDays.get(pointerDay));

        while (timeRemaining > 0) {
            final long businessTime = day.calculateTimeUntilClose(time);
            if (timeRemaining > businessTime) {
                timeRemaining -= businessTime;
            } else {
                time = time.plusSeconds(timeRemaining);
                result = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());
                break;
            }
            pointerDay = (pointerDay + 1) % 7;
            date = date.plusDays(1);
            day = specialDays.getOrDefault(date.toString(), weekDays.get(pointerDay));
            time = day.getOpeningHour();
        }
        return result;
    }

    protected List<WeekDay> getWeekDays() {
        return weekDays;
    }

    protected SortedMap<String, WeekDay> getSpecialDays() {
        return specialDays;
    }
}
