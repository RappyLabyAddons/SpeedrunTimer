package com.rappytv.speedruntimer.sound;

import net.labymod.api.client.resources.ResourceLocation;

public class DefaultTimerSound implements TimerSound {

    private final ResourceLocation sound = ResourceLocation.create("minecraft", "block.note_block.pling");

    @Override
    public ResourceLocation getNotificationSound() {
        return this.sound;
    }
}
