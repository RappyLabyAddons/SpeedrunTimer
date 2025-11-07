package com.rappytv.speedruntimer.v1_8_9;

import com.rappytv.speedruntimer.sound.TimerSound;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;

@Singleton
@Implements(TimerSound.class)
public class VersionedTimerSound implements TimerSound {

    private final ResourceLocation sound = ResourceLocation.create("minecraft", "note.pling");

    @Override
    public ResourceLocation getNotificationSound() {
        return this.sound;
    }
}
