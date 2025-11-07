package com.rappytv.speedruntimer.sound;

import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface TimerSound {

    ResourceLocation getNotificationSound();
}
