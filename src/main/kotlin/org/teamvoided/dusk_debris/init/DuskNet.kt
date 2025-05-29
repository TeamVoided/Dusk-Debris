package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.net.StatueScreenPayload
import org.teamvoided.dusk_debris.net.StatueUpdatePayload

object DuskNet {
    fun init() {
        PayloadTypeRegistry.playS2C().register(StatueScreenPayload.ID, StatueScreenPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(StatueUpdatePayload.ID, StatueUpdatePayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(StatueUpdatePayload.ID, ::updateStatue)
    }

    private fun updateStatue(payload: StatueUpdatePayload, ctx: ServerPlayNetworking.Context) {
        val type = payload.type ?: return
        val world = ctx.player().world ?: return
        val statue = world.getBlockEntity(payload.pos) ?: return
        if (statue !is StatueBlockEntity) return
        println(type)
        statue.entityType = type
        ctx.player().networkHandler.send(BlockEntityUpdateS2CPacket.create(statue, BlockEntity::toComponentlessNbt))

    }
}