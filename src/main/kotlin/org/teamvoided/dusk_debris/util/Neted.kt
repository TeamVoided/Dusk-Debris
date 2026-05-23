package org.teamvoided.dusk_debris.util

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.net.StatueScreenPayload


fun Player.openStatuesScreen(statue: StatueBlockEntity) {
    if (this is ServerPlayer) {
        this.connection.send(ClientboundBlockEntityDataPacket.create(statue, BlockEntity::saveCustomOnly))
        ServerPlayNetworking.send(this, StatueScreenPayload(statue.blockPos))
    }
}