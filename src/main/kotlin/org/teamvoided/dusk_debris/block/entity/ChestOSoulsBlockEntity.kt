package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.ChestLidAnimator
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.client.block.ChestAnimationProgress
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities
import org.teamvoided.dusks_and_dungeons.init.DnDBlocks

class ChestOSoulsBlockEntity(pos: BlockPos?, state: BlockState?) :
    LootableContainerBlockEntity(DnDBlockEntities.CHEST_O_SOULS, pos, state), ChestAnimationProgress {

    private var openTicks = 0
    private val lidAnimator = ChestLidAnimator()
    private var inventory = DefaultedList.ofSize(1, ItemStack.EMPTY)
    private val stateManager = object : ViewerCountManager() {
        override fun onContainerOpen(world: World?, pos: BlockPos?, state: BlockState?) {}

        override fun onContainerClose(world: World?, pos: BlockPos?, state: BlockState?) {}

        override fun onViewerCountUpdate(
            world: World,
            pos: BlockPos,
            state: BlockState,
            oldViewerCount: Int,
            newViewerCount: Int
        ) {
            world.addSyncedBlockEvent(pos, DnDBlocks.CHEST_O_SOULS, 1, newViewerCount)
        }

        override fun isPlayerViewing(player: PlayerEntity?): Boolean = isOpen()
    }

    override fun size(): Int = inventory.size

    override fun getContainerName(): Text = Text.translatable("container.dusks_and_dungeons.chest_o_souls")

    override fun getInventory(): DefaultedList<ItemStack> = inventory

    override fun setInventory(newInventory: DefaultedList<ItemStack>) {
        inventory = newInventory
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory?): ScreenHandler? = null
    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.of(this)
    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider?): NbtCompound {
        val nbt = NbtCompound()
        nbt.putInt("openTicks", openTicks)
        return nbt
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.writeNbt(nbt, lookupProvider)
        nbt.putInt("openTicks", openTicks)
        Inventories.writeNbt(nbt, inventory, lookupProvider)
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider?) {
        super.readNbtImpl(nbt, lookupProvider)
        openTicks = nbt.getInt("openTicks")
        Inventories.readNbt(nbt, inventory, lookupProvider)
    }

    override fun getAnimationProgress(tickDelta: Float): Float = lidAnimator.getProgress(tickDelta)

    fun isOpen(): Boolean = openTicks > 0

    fun open(player: PlayerEntity) {
        if (!removed) {
            openTicks = 100
            setOpen(player)
        }
    }

    fun setOpen(player: PlayerEntity) {
        if (!player.isSpectator) {
            stateManager.openContainer(player, world, pos, cachedState)
            world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_ALL)
        }
    }

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            lidAnimator.setOpen(data > 0)
            return true
        }
        return super.onSyncedBlockEvent(type, data)
    }

    fun onScheduledTick() {
        if (!removed) {
            stateManager.updateViewerCount(world, pos, cachedState)
        }
    }

    companion object {
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: ChestOSoulsBlockEntity) {
            if (world.isClient) {
                blockEntity.lidAnimator.step()

                if (blockEntity.isOpen()) {
                    val random = world.getRandom()
                    world.addParticle(
                        ParticleTypes.SOUL,
                        pos.x + 0.5 + (MathHelper.nextDouble(
                            random,
                            0.0,
                            0.4375
                        ) * if (random.nextBoolean()) 1 else -1),
                        pos.y + 0.625,
                        pos.z + 0.5 + (MathHelper.nextDouble(
                            random,
                            0.0,
                            0.4375
                        ) * if (random.nextBoolean()) 1 else -1),
                        MathHelper.nextDouble(random, -0.03, 0.03),
                        MathHelper.nextDouble(random, 0.01, 0.2),
                        MathHelper.nextDouble(random, -0.03, 0.03)
                    )
                }

                return
            }

            if (blockEntity.isOpen()) {
                blockEntity.openTicks--

                if (!blockEntity.isOpen()) {
                    blockEntity.stateManager.closeContainer(null, world, pos, state)
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL)
                }
            }
        }
    }
}
