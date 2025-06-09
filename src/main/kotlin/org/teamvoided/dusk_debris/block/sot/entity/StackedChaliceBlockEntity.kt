package org.teamvoided.dusk_debris.block.sot.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.HolderLookup
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.init.DuskBlockEntities.STACKED_CHALICE

class StackedChaliceBlockEntity(pos: BlockPos, state: BlockState?) : BlockEntity(STACKED_CHALICE, pos, state) {
    val chalices: DefaultedList<ItemStack> = DefaultedList.ofSize(4, ItemStack.EMPTY)
    fun isEmpty(): Boolean {
        for (item in chalices) {
            if (!item.isEmpty) return false
        }
        return true
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.readNbtImpl(nbt, lookupProvider)
        println("Read: $nbt")

        if (nbt.contains(KEY)) {
            val list = nbt.getList(KEY, NbtElement.STRING_TYPE.toInt())
            list.forEachIndexed { index, nbt ->
                chalices[index] =
                    ItemStack.field_49266.parse(NbtOps.INSTANCE, nbt).resultOrPartial().orElse(ItemStack.EMPTY)
            }
        }
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        val list = NbtList()
        for (stack in chalices) {
            val ops = lookupProvider.createSerializationContext(NbtOps.INSTANCE)
            list.add(ItemStack.field_49266.encodeStart(ops, stack).getOrThrow())
        }
        nbt.put(KEY, list)

        println("Write: $nbt")
    }


    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.of(this)
    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider): NbtCompound {
        println("Sync")
        return this.toComponentlessNbt(lookupProvider)
    }

    companion object {
        const val KEY = "chalices"

    }
}
