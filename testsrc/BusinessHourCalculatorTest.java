import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.Locale;

class BusinessHourCalculatorTest {
    private  BusinessHourCalculator businessHourCalculator;

    private DateFormat dateFormat;

    @BeforeEach
    void setUp() {
        businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "8:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");

        dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss yyyy", Locale.ENGLISH);
    }

    @Test
    public void test_Example_1() throws ParseException {
        final Date result = businessHourCalculator.calculateDeadline(2*60*60, "2010-06-07 09:10");
        final Date expected = dateFormat.parse("Mon Jun 07 11:10:00 2010");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_Example_2() throws ParseException {
        final Date result = businessHourCalculator.calculateDeadline(15*60, "2010-06-08 14:48");
        final Date expected = dateFormat.parse("Thu Jun 10 09:03:00 2010");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_Example_3() throws ParseException {
        final Date result = businessHourCalculator.calculateDeadline(7*60*60, "2010-12-24 6:45");
        final Date expected = dateFormat.parse("Mon Dec 27 11:00:00 2010");
        Assertions.assertEquals(expected, result);
    }
}