package com.rappytv.speedruntimer.sound;

import net.labymod.api.client.resources.ResourceLocation;

public class DefaultTimerSound implements ITimerSound {

    private final ResourceLocation sound = ResourceLocation.create("minecraft", "block.note_block.pling");

    @Override
    public ResourceLocation getNotificationSound() {
        return sound;
    }
}
