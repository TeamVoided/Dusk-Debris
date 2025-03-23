package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryOps
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities

class QuarterBlockPileBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(DnDBlockEntities.QUARTER_BLOCK_PILE, pos, state) {

    val blocks: DefaultedList<Block> = DefaultedList.ofSize(3, Blocks.AIR)

    fun place(block: Block): ItemInteractionResult {
        var success = false
        for (i in 0..<blocks.size) {
            val currentBlock = blocks[i]

            if (currentBlock == Blocks.AIR) {
                blocks[i] = block
                success = true
                world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_ALL)
                break
            }
        }
        return if (success) ItemInteractionResult.SUCCESS else ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    fun isEmpty(): Boolean {
        for (block in blocks) {
            if (block != null && !block.equals(Blocks.AIR)) {
                return false
            }
        }
        return true
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.of(this)
    }

    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider): NbtCompound {
        val nbt = NbtCompound()
        writeBlocks(nbt, lookupProvider)
        return nbt
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        writeBlocks(nbt, lookupProvider)
    }

    private fun writeBlocks(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        val list = NbtList()
        blocks.forEach { block ->
            val ops: RegistryOps<NbtElement> = lookupProvider.createSerializationContext(NbtOps.INSTANCE)
            list.add(Registries.BLOCK.codec.encodeStart(ops, block).getOrThrow())
        }
        nbt.put("blocks", list)
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.readNbtImpl(nbt, lookupProvider)

        val list = nbt.getList("blocks", NbtElement.STRING_TYPE.toInt())
        list.forEachIndexed { index, blockNbt ->
            blocks[index] = Registries.BLOCK.codec.parse(NbtOps.INSTANCE, blockNbt).resultOrPartial().orElse(Blocks.AIR)
        }
    }
}
