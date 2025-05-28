package org.teamvoided.dusk_debris.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.init.DuskBlockEntities.STATUE
import kotlin.jvm.optionals.getOrNull

class StatueBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(STATUE, pos, state) {
    var entityType: EntityType<*> = EntityType.BREEZE
    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.writeNbt(nbt, lookupProvider)
        val type = Registries.ENTITY_TYPE.codec.encodeStart(NbtOps.INSTANCE, entityType).result().getOrNull()
        nbt.put("type", type)
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.readNbtImpl(nbt, lookupProvider)
        this.entityType = Registries.ENTITY_TYPE.codec.decode(NbtOps.INSTANCE, nbt.get("type")).result().getOrNull()?.first
                ?: EntityType.BREEZE
    }

    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider): NbtCompound {
        return this.toComponentlessNbt(lookupProvider)
    }

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket? {
        return BlockEntityUpdateS2CPacket.of(this)
    }

}