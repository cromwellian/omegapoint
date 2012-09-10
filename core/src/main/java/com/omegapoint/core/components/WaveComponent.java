package com.omegapoint.core.components;

import com.artemis.Component;
import com.artemis.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WaveComponent extends Component {

    public static class SpawnSchedule {
        double relativeDelay;
        Entity toBeSpawned;

        public SpawnSchedule(Entity toBeSpawned, double relativeDelay) {
            this.toBeSpawned = toBeSpawned;
            this.relativeDelay = relativeDelay;
        }
    }

    public List<SpawnSchedule> spawnScheduleList = new ArrayList<SpawnSchedule>();
    public void schedule(Entity toBeSpawned, double relativeDelay) {
        spawnScheduleList.add(new SpawnSchedule(toBeSpawned, relativeDelay));
    }
}

