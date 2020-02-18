import java.time.Duration;
import java.time.LocalTime;

public class WeekDay {
    private boolean open;
    private LocalTime openingHour;
    private LocalTime closingHour;

    private static final String TIME_FORMAT = "kk:mm";

    public WeekDay() {
        this(false, "00:00", "23:59");
    }

    public WeekDay(boolean open, String openingHour, String closingHour) {
        setOpen(open);
        setOpeningHour(openingHour);
        setClosingHour(closingHour);
    }

    public void updateOpeningHours(String openingHour, String closingHour) {
        setOpen(true);
        setOpeningHour(openingHour);
        setClosingHour(closingHour);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private LocalTime parseLocalTime(final String time) {
        return LocalTime.parse(time.length() > 4 ? time : "0" + time);
    }

    public long timeOpen(){
        return calculateTimeUntilClose(getOpeningHour());
    }

    public long calculateTimeUntilClose(final String time) {
        return calculateTimeUntilClose(parseLocalTime(time));
    }

    public long calculateTimeUntilClose(final LocalTime time) {
        final long result;
        if (isOpen() && time.isBefore(closingHour)) {

            final Duration duration = Duration.between(time.isAfter(openingHour)?time:openingHour, closingHour);
            result = duration.getSeconds();
        } else {
            result = 0;
        }
        return result;
    }

    private void setOpeningHour(final String time) {
        this.openingHour = parseLocalTime(time);
    }

    public LocalTime getOpeningHour() {
        return openingHour;
    }

    private void setClosingHour(final String time) {
        this.closingHour = parseLocalTime(time);
    }

    public LocalTime getClosingHour() {
        return closingHour;
    }
}
