package com.rappytv.speedruntimer.v1_12_2;

import com.rappytv.speedruntimer.sound.ITimerSound;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.Implements;

@Implements(ITimerSound.class)
public class TimerSoundImpl implements ITimerSound {

    private final ResourceLocation sound = ResourceLocation.create("minecraft", "block.note.pling");

    @Override
    public ResourceLocation getNotificationSound() {
        return sound;
    }
}
