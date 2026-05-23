package org.teamvoided.dusk_debris.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.init.DuskBlockEntities.STATUE
import kotlin.jvm.optionals.getOrNull

class StatueBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(STATUE, pos, state) {
    var entityType: EntityType<*> = EntityType.BREEZE

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.saveAdditional(nbt, lookupProvider)
        val type = BuiltInRegistries.ENTITY_TYPE.byNameCodec().encodeStart(NbtOps.INSTANCE, entityType).result().getOrNull()
        nbt.put("type", type)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.loadAdditional(nbt, lookupProvider)
        this.entityType =
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().decode(NbtOps.INSTANCE, nbt.get("type")).result().getOrNull()?.first
                ?: EntityType.BREEZE
    }

    override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
        return this.saveCustomOnly(lookupProvider)
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

}