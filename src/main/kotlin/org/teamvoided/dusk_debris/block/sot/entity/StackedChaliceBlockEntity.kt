package org.teamvoided.dusk_debris.block.sot.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.init.DuskBlockEntities.STACKED_CHALICE

class StackedChaliceBlockEntity(pos: BlockPos, state: BlockState?) : BlockEntity(STACKED_CHALICE, pos, state) {
    val chalices: NonNullList<ItemStack> = NonNullList.withSize(4, ItemStack.EMPTY)
    fun isEmpty(): Boolean {
        for (item in chalices) {
            if (!item.isEmpty) return false
        }
        return true
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.loadAdditional(nbt, lookupProvider)
        println("Read: $nbt")

        if (nbt.contains(KEY)) {
            val list = nbt.getList(KEY, Tag.TAG_COMPOUND.toInt())
            list.forEachIndexed { index, nbt ->
                chalices[index] =
                    ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial().orElse(ItemStack.EMPTY)!!
            }
        }
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.saveAdditional(nbt, lookupProvider)
        val list = ListTag()
        for (stack in chalices) {
            val ops = lookupProvider.createSerializationContext(NbtOps.INSTANCE)
            list.add(ItemStack.OPTIONAL_CODEC.encodeStart(ops, stack).getOrThrow())
        }
        nbt.put(KEY, list)

        println("Write: $nbt")
    }


    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)
    override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
        println("Sync")
        return this.saveCustomOnly(lookupProvider)
    }

    companion object {
        const val KEY = "chalices"

    }
}
