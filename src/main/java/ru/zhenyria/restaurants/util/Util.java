package ru.zhenyria.restaurants.util;

import java.time.LocalTime;

public class Util {
    private static LocalTime endVoteTime = LocalTime.of(11, 0);

    private Util() {
    }

    public static boolean isCanReVote() {
        return LocalTime.now().isBefore(endVoteTime);
    }

    public static void prepareEndVoteTimeForPassTests() {
        if (LocalTime.now().getHour() >= endVoteTime.getHour()) {
            endVoteTime = LocalTime.of(23, 59);
        }
    }

    public static void prepareEndVoteTimeForFailTests() {
        if (LocalTime.now().getHour() < endVoteTime.getHour()) {
            endVoteTime = LocalTime.of(0, 1);
        }
    }
}
