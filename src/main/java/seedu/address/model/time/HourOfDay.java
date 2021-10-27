package seedu.address.model.time;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the 24 hours in a single day, with 0 being the first hour (0000) and 23 being the last hour (2300).
 */
public class HourOfDay {
    public static final String MESSAGE_HOUR_OF_DAY_MUST_BE_INT = "The hour provided must be an integer value.";
    public static final String MESSAGE_INVALID_RANGE = "The hour provided must be an integer value "
            + "within the range 0 to 23 inclusive.";

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private final int hour;

    /**
     * Constructs an instance of HourOfDay
     *
     * @param hour valid hour between 0 and 23 inclusive.
     */
    public HourOfDay(int hour) {
        checkArgument(validateHourOfDay(hour), MESSAGE_INVALID_RANGE);
        this.hour = hour;
    }

    public static boolean validateHourOfDay(int hourOfDay) {
        return hourOfDay >= MIN_HOUR && hourOfDay <= MAX_HOUR;
    }

    public int getHour() {
        return hour;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof HourOfDay)) {
            return false;
        } else {
            return ((HourOfDay) other).hour == this.hour;
        }
    }
}
