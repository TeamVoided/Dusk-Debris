package org.teamvoided.dusk_debris.net

import net.minecraft.entity.EntityType
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.minecraft.registry.Registries
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.DuskDebris.id

class StatueUpdatePayload(val pos: BlockPos, val type: EntityType<*>?) : CustomPayload {
    constructor(buf: PacketByteBuf) : this(buf.readPos(), Registries.ENTITY_TYPE.get(buf.readIdentifier()))

    override fun getId() = ID
    fun write(buf: PacketByteBuf) {
        buf.writePos(pos)
        buf.writeIdentifier(type?.builtInRegistryHolder?.key?.get()?.value ?: id("empty"))
    }

    companion object {
        val CODEC: PacketCodec<PacketByteBuf, StatueUpdatePayload> =
            CustomPayload.create<PacketByteBuf, StatueUpdatePayload>(StatueUpdatePayload::write, ::StatueUpdatePayload)
        val ID = CustomPayload.Id<StatueUpdatePayload>(id("statue_update_payload"))
    }
}