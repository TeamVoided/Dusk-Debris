package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.util.Mth
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.ChestLidController
import net.minecraft.world.level.block.entity.ContainerOpenersCounter
import net.minecraft.world.level.block.entity.LidBlockEntity
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.init.DuskBlocks

class ChestOSoulsBlockEntity(pos: BlockPos?, state: BlockState?) :
    RandomizableContainerBlockEntity(DuskBlockEntities.CHEST_O_SOULS, pos, state), LidBlockEntity {

    private var openTicks = 0
    private val lidAnimator = ChestLidController()
    private var inventory = NonNullList.withSize(1, ItemStack.EMPTY)
    private val stateManager = object : ContainerOpenersCounter() {
        override fun onOpen(world: Level?, pos: BlockPos?, state: BlockState?) {}

        override fun onClose(world: Level?, pos: BlockPos?, state: BlockState?) {}

        override fun openerCountChanged(
            world: Level,
            pos: BlockPos,
            state: BlockState,
            oldViewerCount: Int,
            newViewerCount: Int
        ) {
            world.blockEvent(pos, DuskBlocks.CHEST_O_SOULS, 1, newViewerCount)
        }

        override fun isOwnContainer(player: Player?): Boolean = isOpen()
    }

    override fun getContainerSize(): Int = inventory.size

    override fun getDefaultName(): Component = Component.translatable("container.dusks_and_dungeons.chest_o_souls")

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun setItems(newInventory: NonNullList<ItemStack>) {
        inventory = newInventory
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory?): AbstractContainerMenu? = null
    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)
    override fun getUpdateTag(lookupProvider: HolderLookup.Provider?): CompoundTag {
        val nbt = CompoundTag()
        nbt.putInt("openTicks", openTicks)
        return nbt
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.saveAdditional(nbt, lookupProvider)
        nbt.putInt("openTicks", openTicks)
        ContainerHelper.saveAllItems(nbt, inventory, lookupProvider)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider?) {
        super.loadAdditional(nbt, lookupProvider)
        openTicks = nbt.getInt("openTicks")
        ContainerHelper.loadAllItems(nbt, inventory, lookupProvider)
    }

    override fun getOpenNess(tickDelta: Float): Float = lidAnimator.getOpenness(tickDelta)

    fun isOpen(): Boolean = openTicks > 0

    fun open(player: Player) {
        if (!remove) {
            openTicks = 100
            setOpen(player)
        }
    }

    fun setOpen(player: Player) {
        if (!player.isSpectator) {
            stateManager.incrementOpeners(player, level, worldPosition, blockState)
            level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL)
        }
    }

    override fun triggerEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            lidAnimator.shouldBeOpen(data > 0)
            return true
        }
        return super.triggerEvent(type, data)
    }

    fun onScheduledTick() {
        if (!remove) {
            stateManager.recheckOpeners(level, worldPosition, blockState)
        }
    }

    companion object {
        fun tick(world: Level, pos: BlockPos, state: BlockState, blockEntity: ChestOSoulsBlockEntity) {
            if (world.isClientSide) {
                blockEntity.lidAnimator.tickLid()

                if (blockEntity.isOpen()) {
                    val random = world.getRandom()
                    world.addParticle(
                        ParticleTypes.SOUL,
                        pos.x + 0.5 + (Mth.nextDouble(
                            random,
                            0.0,
                            0.4375
                        ) * if (random.nextBoolean()) 1 else -1),
                        pos.y + 0.625,
                        pos.z + 0.5 + (Mth.nextDouble(
                            random,
                            0.0,
                            0.4375
                        ) * if (random.nextBoolean()) 1 else -1),
                        Mth.nextDouble(random, -0.03, 0.03),
                        Mth.nextDouble(random, 0.01, 0.2),
                        Mth.nextDouble(random, -0.03, 0.03)
                    )
                }

                return
            }

            if (blockEntity.isOpen()) {
                blockEntity.openTicks--

                if (!blockEntity.isOpen()) {
                    blockEntity.stateManager.decrementOpeners(null, world, pos, state)
                    world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL)
                }
            }
        }
    }
}
