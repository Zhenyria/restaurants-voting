package ru.zhenyria.restaurants.util;

import java.time.LocalTime;

public class Util {
    private static final LocalTime END_VOTE_TIME = LocalTime.of(11, 0);

    private Util() {
    }

    public static boolean isCanReVote() {
        return LocalTime.now().isBefore(END_VOTE_TIME);
    }
}
