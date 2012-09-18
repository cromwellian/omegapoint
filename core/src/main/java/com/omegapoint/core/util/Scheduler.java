package com.omegapoint.core.util;

import playn.core.PlayN;
import playn.core.util.Callback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class Scheduler {
    private List<ScheduledEntry> timers = new ArrayList<ScheduledEntry>();

    public void update(float delta) {
        Iterator<ScheduledEntry> it = timers.iterator();
        while (it.hasNext()) {
            ScheduledEntry entry = it.next();
            if (entry.runIfReady()) {
                it.remove();
            }
        }
    }

    private class ScheduledEntry {
        private double when;
        private Runnable callback;
        private boolean repeat;
        private final double delay;
        private int countTimes;

        public ScheduledEntry(double delay, int countTimes, Runnable callback, boolean repeat) {
            this.delay = delay;
            this.countTimes = countTimes;
            this.when = PlayN.currentTime() + delay;
            this.callback = callback;
            this.repeat = repeat;
        }

        public boolean isReady() {
            return when <= PlayN.currentTime();
        }

        public void cancel() {
            timers.remove(this);
        }

        public boolean runIfReady() {
            boolean remove = false;
            if (isReady()) {
                try {
                  callback.run();
                } finally {
                    if (!repeat || countTimes == 0) {
                      remove = true;
                    } else {
                        if (countTimes > 0) {
                            countTimes--;
                        }
                        this.when = PlayN.currentTime() + this.delay;
                    }
                }
            }
            return remove;
        }
    }

    public void schedule(double delay, Runnable callback) {
        timers.add(new ScheduledEntry(delay, -1, callback, false));
    }

    public void scheduleRepeating(double period, Runnable callback) {
        timers.add(new ScheduledEntry(period, -1, callback, true));
    }

    public void scheduleRepeatingWithCount(double period, int countTimes, Runnable callback) {
        timers.add(new ScheduledEntry(period, countTimes, callback, true));
    }

    public void scheduleRepeatingUntil(double period, double maxPeriod, Runnable callback) {
        timers.add(new ScheduledEntry(period, (int) Math.ceil(maxPeriod/period), callback, true));
    }

}
