package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;
import com.omegapoint.core.achievements.Achievement;

/**
 * Event fired when an achievement reached.
 */
public class AchievementAchievedEvent extends Event<AchievementAchievedHandler> {
    public static Type<AchievementAchievedHandler> TYPE = new Type<AchievementAchievedHandler>();
    private Achievement achievement;

    public AchievementAchievedEvent(Achievement achievement) {
        this.achievement = achievement;
    }

    @Override
    public Type<AchievementAchievedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AchievementAchievedHandler handler) {
        handler.onAchievement(this);
    }

    public Achievement getAchievement() {
        return achievement;
    }
}
