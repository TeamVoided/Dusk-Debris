package org.teamvoided.dusk_debris.block.temp.entity

import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.DoubleInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.StoneChestBlockEntity
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.init.DuskBlockEntities

class DuskChestBlockEntity constructor(blockEntityType: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    LootableContainerBlockEntity(blockEntityType, pos, state) {
    private var inventory: DefaultedList<ItemStack>
    private val stateManager: ViewerCountManager
    var lidOpeningTicks = 0

    constructor(pos: BlockPos, state: BlockState) : this(DuskBlockEntities.STONE_CHEST, pos, state)

    init {
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        this.stateManager = object : ViewerCountManager() {
            override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN)
                setOpen(state, 2)
            }

            override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE)
                setOpen(state, 1)
            }

            override fun onViewerCountUpdate(
                world: World,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {
                onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount)
            }

            override fun isPlayerViewing(player: PlayerEntity): Boolean {
                if (player.currentScreenHandler !is GenericContainerScreenHandler) {
                    return false
                } else {
                    val inventory = (player.currentScreenHandler as GenericContainerScreenHandler).inventory
                    return inventory == this@DuskChestBlockEntity || inventory is DoubleInventory && inventory.isPart(
                        this@DuskChestBlockEntity
                    )
                }
            }
        }
    }

    override fun size(): Int = 27

    override fun getContainerName(): Text = Text.translatable("container.chest")

    fun getDoubleContainerName(): Text = Text.translatable("container.chestDouble")

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.readNbtImpl(nbt, lookupProvider)
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
        if (!this.readLootTableNbt(nbt)) {
            Inventories.readNbt(nbt, this.inventory, lookupProvider)
        }
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        if (!this.writeLootTableNbt(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, lookupProvider)
        }
    }

    override fun onOpen(player: PlayerEntity) {
        if (!this.removed && !player.isSpectator) {
            stateManager.openContainer(player, this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    override fun onClose(player: PlayerEntity) {
        if (!this.removed && !player.isSpectator) {
            stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    private fun setOpen(state: BlockState, open: Int) {
        world!!.setBlockState(this.getPos(), state.with(DuskProperties.CHEST_PHASE, ChestPhase.fromInt(open)), 3)
        if (open == 0) lidOpeningTicks = 0
    }

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        when (type) {
            CLOSED_COUNT_EVENT -> {
                setOpen(cachedState, 0)
                return true
            }

            CLOSING_COUNT_EVENT -> {
                setOpen(cachedState, 1)
                return true
            }

            OPEN_COUNT_EVENT -> {
                setOpen(cachedState, 2)
                return true
            }
        }
        return super.onSyncedBlockEvent(type, data)
    }

    override fun getInventory(): DefaultedList<ItemStack> = this.inventory

    override fun setInventory(stacks: DefaultedList<ItemStack>) {
        this.inventory = stacks
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
    }

    fun onScheduledTick() {
        if (!this.removed) {
            stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    fun onInvOpenOrClose(
        world: World,
        pos: BlockPos,
        state: BlockState,
        oldViewerCount: Int,
        newViewerCount: Int
    ) {
        val block = state.block
        world.addSyncedBlockEvent(
            pos,
            block,
            if (newViewerCount > 0) OPEN_COUNT_EVENT
            else CLOSING_COUNT_EVENT,
            newViewerCount
        )
    }

    companion object {
        private const val CLOSED_COUNT_EVENT = 0
        private const val CLOSING_COUNT_EVENT = 1
        private const val OPEN_COUNT_EVENT = 2
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: DuskChestBlockEntity) {
            val phase = state.get(DuskProperties.CHEST_PHASE)
            if (phase.ordinal == 2) {
                if (blockEntity.lidOpeningTicks < StoneChestBlockEntity.MAX_OPENING_TICKS)
                    blockEntity.lidOpeningTicks++
            } else if (blockEntity.lidOpeningTicks > 0) {
                blockEntity.lidOpeningTicks--
            } else if (phase.ordinal != 0) {
                world.addSyncedBlockEvent(pos, state.block, CLOSED_COUNT_EVENT, 0)
            }
        }

        fun playSound(world: World, pos: BlockPos, state: BlockState, soundEvent: SoundEvent?) {
            val chestType = state.get(ChestBlock.CHEST_TYPE)
            if (chestType != ChestType.LEFT) {
                var posX = pos.x.toDouble() + 0.5
                val posY = pos.y.toDouble() + 0.5
                var posZ = pos.z.toDouble() + 0.5
                if (chestType == ChestType.RIGHT) {
                    val direction = ChestBlock.getFacing(state)
                    posX += direction.offsetX.toDouble() * 0.5
                    posZ += direction.offsetZ.toDouble() * 0.5
                }

                world.playSound(
                    null,
                    posX, posY, posZ,
                    soundEvent,
                    SoundCategory.BLOCKS,
                    0.5f,
                    world.random.nextFloat() * 0.1f + 0.9f
                )
            }
        }

        fun getPlayersLookingInChestCount(world: BlockView, pos: BlockPos?): Int {
            val blockState = world.getBlockState(pos)
            if (blockState.hasBlockEntity()) {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is DuskChestBlockEntity) {
                    return blockEntity.stateManager.viewerCount
                }
            }

            return 0
        }

        fun copyInventory(from: DuskChestBlockEntity, to: DuskChestBlockEntity) {
            val defaultedList = from.getInventory()
            from.setInventory(to.getInventory())
            to.setInventory(defaultedList)
        }
    }
}
