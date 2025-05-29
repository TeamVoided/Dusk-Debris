package org.teamvoided.dusk_debris.util

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.net.StatueScreenPayload


fun PlayerEntity.openStatuesScreen(statue: StatueBlockEntity) {
    if (this is ServerPlayerEntity) {
        this.networkHandler.send(BlockEntityUpdateS2CPacket.create(statue, BlockEntity::toComponentlessNbt))
        ServerPlayNetworking.send(this, StatueScreenPayload(statue.pos))
    }
}