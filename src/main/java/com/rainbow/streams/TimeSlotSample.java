package com.rainbow.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/5.
 */
class TimeSlot {
    Random random = new Random();

    public void schedule() {

    }

    public boolean isAvailable() {
        return random.nextBoolean();
    }
}

public class TimeSlotSample {
    public static void main(String[] args) {
        List<TimeSlot> timeSlots = Arrays.asList(new TimeSlot(), new TimeSlot(),
                new TimeSlot(), new TimeSlot());

        TimeSlot firstAvailableTimeSlot = null;
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.isAvailable()) {
                timeSlot.schedule();
                firstAvailableTimeSlot = timeSlot;
                break;
            }
        }
        System.out.println("TimeSlot is " + firstAvailableTimeSlot);

        Optional<TimeSlot> timeSlot = timeSlots.stream()
                .filter(TimeSlot::isAvailable)
                .findFirst();
        if (timeSlot.isPresent()) {
            timeSlot.get().schedule();
        }

        System.out.println("TimeSlot is " + firstAvailableTimeSlot);
    }
}
