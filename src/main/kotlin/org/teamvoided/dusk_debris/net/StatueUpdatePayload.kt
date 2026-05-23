package org.teamvoided.dusk_debris.net

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.EntityType
import org.teamvoided.dusk_debris.DuskDebris.id

class StatueUpdatePayload(val pos: BlockPos, val type: EntityType<*>?) : CustomPacketPayload {
    constructor(buf: FriendlyByteBuf) : this(buf.readBlockPos(), BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation()))

    override fun type() = ID
    fun write(buf: FriendlyByteBuf) {
        buf.writeBlockPos(pos)
        buf.writeResourceLocation(type?.builtInRegistryHolder()?.unwrapKey()?.get()?.location() ?: id("empty"))
    }

    companion object {
        val CODEC: StreamCodec<FriendlyByteBuf, StatueUpdatePayload> =
            CustomPacketPayload.codec<FriendlyByteBuf, StatueUpdatePayload>(StatueUpdatePayload::write, ::StatueUpdatePayload)
        val ID = CustomPacketPayload.Type<StatueUpdatePayload>(id("statue_update_payload"))
    }
}