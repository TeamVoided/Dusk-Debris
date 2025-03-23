package org.teamvoided.dusk_debris.structure.pool

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.nbt.NbtList
import net.minecraft.structure.Structure

class SeperateForNow() {

    fun create() {
    }

//    fun createBlockLists(){
//        val nbtList = NbtList()

//        val nbtCompound = NbtCompound()
//        nbtCompound.put(
//            "pos",
//            createNbtIntList(
//                structureBlockInfo.pos.getX(),
//                structureBlockInfo.pos.getY(),
//                structureBlockInfo.pos.getZ()
//            )
//        )
//        val k: Int = palette.getId(structureBlockInfo.state)
//        nbtCompound.putInt("state", k)
//        if (structureBlockInfo.nbt != null) {
//            nbtCompound.put("nbt", structureBlockInfo.nbt)
//        }

//        nbtList.add(nbtCompound)
//    }

//    private fun createNbtIntList(vararg ints: Int): NbtList {
//        val nbtList = NbtList()
//        val var3 = ints
//        val var4 = ints.size

//        for (var5 in 0 until var4) {
//            val i = var3[var5]
//            nbtList.add(NbtInt.of(i))
//        }

//        return nbtList
//    }

    fun pallete() {
        val nbtCompound = NbtCompound()

        val list = listOf<Structure.Palette>(
//            Blocks.DIAMOND_BLOCK.defaultState,
//            Blocks.GOLD_BLOCK.defaultState,
//            Blocks.IRON_BLOCK.defaultState,
//            Blocks.OXIDIZED_COPPER.defaultState
        )

        val nbtList2: NbtList
//        if (list.size == 1) {
//            nbtList2 = NbtList()
//            var18 = palette.iterator()
//
//            while (var18.hasNext()) {
//                val blockState = var18.next() as BlockState
//                nbtList2.add(NbtHelper.fromBlockState(blockState))
//            }
//
//            nbtCompound.put("palette", nbtList2)
//        } else {
        nbtList2 = NbtList()

        list.forEach { pallete ->
            val nbtList3 = NbtList()
            pallete.forEach { state ->
                nbtList3.add(NbtHelper.fromBlockState(state))
            }

            nbtList2.add(nbtList3)
        }

        nbtCompound.put("palettes", nbtList2)
//        }
    }

}