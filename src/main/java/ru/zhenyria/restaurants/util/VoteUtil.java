package ru.zhenyria.restaurants.util;

import java.time.*;

public class VoteUtil {
    public static final int DEADLINE_HOURS = 11;

    private static final LocalTime endVoteTime = LocalTime.of(DEADLINE_HOURS, 0);
    private static Clock clock = Clock.systemDefaultZone();

    private VoteUtil() {
    }

    public static boolean isCanReVote() {
        return LocalTime.now(clock).isBefore(endVoteTime);
    }

    public static void prepareEndVoteTimeForPassTests() {
        setFixedClockOfHours(DEADLINE_HOURS - 1);
    }

    public static void prepareEndVoteTimeForFailTests() {
        setFixedClockOfHours(DEADLINE_HOURS);
    }

    private static void setFixedClockOfHours(int hours) {
        ZoneId zone = clock.getZone();
        clock = Clock.fixed(Instant.from(
                ZonedDateTime.of(
                        LocalDateTime.of(2020, 12, 1, hours, 0), zone)), zone);
    }
}
