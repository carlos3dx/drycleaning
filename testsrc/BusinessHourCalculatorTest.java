import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class BusinessHourCalculatorTest {
    private BusinessHourCalculator businessHourCalculator;

    private DateFormat dateFormat;
    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss yyyy";
    private static final String OPENING_HOUR = "09:15";
    private static final String CLOSING_HOUR = "16:22";

    @BeforeEach
    void setUp() {

        businessHourCalculator = new BusinessHourCalculator(OPENING_HOUR, CLOSING_HOUR);
        dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
    }


    @Test
    public void test_constructor() {


        for (final WeekDay day : businessHourCalculator.getWeekDays()) {
            Assertions.assertEquals(OPENING_HOUR, day.getOpeningHour().toString());
            Assertions.assertEquals(CLOSING_HOUR, day.getClosingHour().toString());
        }
    }

    @Test
    public void test_closing_days_of_week() {
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY, DayOfWeek.MONDAY);
        final List<WeekDay> days = businessHourCalculator.getWeekDays();
        Assertions.assertFalse(days.get(0).isOpen());
        Assertions.assertTrue(days.get(1).isOpen());
        Assertions.assertFalse(days.get(2).isOpen());
        Assertions.assertTrue(days.get(3).isOpen());
        Assertions.assertTrue(days.get(4).isOpen());
        Assertions.assertTrue(days.get(5).isOpen());
        Assertions.assertFalse(days.get(6).isOpen());
    }


    @Test
    public void test_Example_1() throws ParseException {
        setForGivenExamples();
        final Date result = businessHourCalculator.calculateDeadline(2 * 60 * 60, "2010-06-07 09:10");
        final Date expected = dateFormat.parse("Mon Jun 07 11:10:00 2010");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_Example_2() throws ParseException {
        setForGivenExamples();
        final Date result = businessHourCalculator.calculateDeadline(15 * 60, "2010-06-08 14:48");
        final Date expected = dateFormat.parse("Thu Jun 10 09:03:00 2010");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_Example_3() throws ParseException {
        setForGivenExamples();
        final Date result = businessHourCalculator.calculateDeadline(7 * 60 * 60, "2010-12-24 6:45");
        final Date expected = dateFormat.parse("Mon Dec 27 11:00:00 2010");
        Assertions.assertEquals(expected, result);
    }

    private void setForGivenExamples() {
        businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "8:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");
    }
}