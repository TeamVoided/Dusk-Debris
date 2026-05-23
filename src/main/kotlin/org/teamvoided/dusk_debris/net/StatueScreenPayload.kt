package org.teamvoided.dusk_debris.net

import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.teamvoided.dusk_debris.DuskDebris.id

class StatueScreenPayload(val pos: BlockPos) : CustomPacketPayload {
    constructor(buf: FriendlyByteBuf) : this(buf.readBlockPos())

    override fun type() = ID
    fun write(buf: FriendlyByteBuf) {
        buf.writeBlockPos(pos)
    }

    companion object {
        val CODEC: StreamCodec<FriendlyByteBuf, StatueScreenPayload> =
            CustomPacketPayload.codec<FriendlyByteBuf, StatueScreenPayload>(StatueScreenPayload::write, ::StatueScreenPayload)
        val ID = CustomPacketPayload.Type<StatueScreenPayload>(id("statue_screen_payload"))
    }
}