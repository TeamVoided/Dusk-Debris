package org.teamvoided.dusk_debris.net

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.screen.StatueScreen

object DuskNetClient {
    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(StatueScreenPayload.ID, ::openStatueScreen)
    }

    fun openStatueScreen(payload: StatueScreenPayload, ctx: ClientPlayNetworking.Context) {
        val client = ctx.client() ?: return
        val world = ctx.player().level() ?: return
        val statue = world.getBlockEntity(payload.pos) ?: return
        if (statue !is StatueBlockEntity) return

        client.setScreen(StatueScreen(statue))
    }
}
