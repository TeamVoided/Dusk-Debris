package org.teamvoided.dusk_debris.util

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.DuskDebris.isDev

fun sendMessageIngame(message: String) {
    if (isDev()) Minecraft.getInstance().player?.displayClientMessage(Component.literal(message), true)
    else println("this message: $message; has been brought to you by: $MODID")
}