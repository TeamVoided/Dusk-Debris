package org.teamvoided.dusk_debris.net

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.DuskDebris.id

class StatueScreenPayload(val pos: BlockPos) : CustomPayload {
    constructor(buf: PacketByteBuf) : this(buf.readPos())

    override fun getId() = ID
    fun write(buf: PacketByteBuf) {
        buf.writePos(pos)
    }

    companion object {
        val CODEC: PacketCodec<PacketByteBuf, StatueScreenPayload> =
            CustomPayload.create<PacketByteBuf, StatueScreenPayload>(StatueScreenPayload::write, ::StatueScreenPayload)
        val ID = CustomPayload.Id<StatueScreenPayload>(id("statue_screen_payload"))
    }
}