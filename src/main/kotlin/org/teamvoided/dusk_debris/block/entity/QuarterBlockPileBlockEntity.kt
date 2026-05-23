package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.RegistryOps
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.init.DuskBlockEntities

class QuarterBlockPileBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(DuskBlockEntities.QUARTER_BLOCK_PILE, pos, state) {

    val blocks: NonNullList<Block> = NonNullList.withSize(3, Blocks.AIR)

    fun place(block: Block): ItemInteractionResult {
        var success = false
        for (i in 0..<blocks.size) {
            val currentBlock = blocks[i]

            if (currentBlock == Blocks.AIR) {
                blocks[i] = block
                success = true
                level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL)
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

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
        val nbt = CompoundTag()
        writeBlocks(nbt, lookupProvider)
        return nbt
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.saveAdditional(nbt, lookupProvider)
        writeBlocks(nbt, lookupProvider)
    }

    private fun writeBlocks(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        val list = ListTag()
        blocks.forEach { block ->
            val ops: RegistryOps<Tag> = lookupProvider.createSerializationContext(NbtOps.INSTANCE)
            list.add(BuiltInRegistries.BLOCK.byNameCodec().encodeStart(ops, block).getOrThrow())
        }
        nbt.put("blocks", list)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.loadAdditional(nbt, lookupProvider)

        val list = nbt.getList("blocks", Tag.TAG_STRING.toInt())
        list.forEachIndexed { index, blockNbt ->
            blocks[index] = BuiltInRegistries.BLOCK.byNameCodec()
                .parse(NbtOps.INSTANCE, blockNbt).resultOrPartial().orElse(Blocks.AIR)
        }
    }
}
