package org.teamvoided.dusk_debris.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.CompoundContainer
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.ContainerOpenersCounter
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.ChestType
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.init.DuskBlockEntities

class DuskChestBlockEntity constructor(blockEntityType: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    RandomizableContainerBlockEntity(blockEntityType, pos, state) {
    private var inventory: NonNullList<ItemStack>
    private val stateManager: ContainerOpenersCounter
    var lidOpeningTicks = 0

    constructor(pos: BlockPos, state: BlockState) : this(DuskBlockEntities.STONE_CHEST, pos, state)

    init {
        this.inventory = NonNullList.withSize(containerSize, ItemStack.EMPTY)
        this.stateManager = object : ContainerOpenersCounter() {
            override fun onOpen(world: Level, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.CHEST_OPEN)
                setOpen(state, 2)
            }

            override fun onClose(world: Level, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.CHEST_CLOSE)
                setOpen(state, 1)
            }

            override fun openerCountChanged(
                world: Level,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {
                onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount)
            }

            override fun isOwnContainer(player: Player): Boolean {
                if (player.containerMenu !is ChestMenu) {
                    return false
                } else {
                    val inventory = (player.containerMenu as ChestMenu).container
                    return inventory == this@DuskChestBlockEntity || inventory is CompoundContainer && inventory.contains(
                        this@DuskChestBlockEntity
                    )
                }
            }
        }
    }

    override fun getContainerSize(): Int = 27

    override fun getDefaultName(): Component {
        val blockName = BuiltInRegistries.BLOCK.getKey(this.blockState.block).path //this.cachedState.block.name
        return Component.translatable("container.$blockName")
    }

    fun getDoubleContainerName(): Component {
        val blockName = BuiltInRegistries.BLOCK.getKey(this.blockState.block).path //this.cachedState.block.name
        return Component.translatable("container.$blockName" + "_double")
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.loadAdditional(nbt, lookupProvider)
        this.inventory = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.inventory, lookupProvider)
        }
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.saveAdditional(nbt, lookupProvider)
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory, lookupProvider)
        }
    }

    override fun startOpen(player: Player) {
        if (!this.remove && !player.isSpectator) {
            stateManager.incrementOpeners(player, this.getLevel(), this.blockPos, this.blockState)
        }
    }

    override fun stopOpen(player: Player) {
        if (!this.remove && !player.isSpectator) {
            stateManager.decrementOpeners(player, this.getLevel(), this.blockPos, this.blockState)
        }
    }

    private fun setOpen(state: BlockState, open: Int) {
//        this.cachedState = state.with(DuskProperties.CHEST_PHASE, ChestPhase.fromInt(open))
        level!!.setBlock(blockPos, state.setValue(DuskProperties.CHEST_PHASE, ChestPhase.fromInt(open)), 3)
//        if (open == 0) lidOpeningTicks = 0
    }

    override fun triggerEvent(type: Int, data: Int): Boolean {
        when (type) {
            CLOSED_COUNT_EVENT -> {
                setOpen(blockState, 0)
                return true
            }

            CLOSING_COUNT_EVENT -> {
                setOpen(blockState, 1)
                return true
            }

            OPEN_COUNT_EVENT -> {
                setOpen(blockState, 2)
                return true
            }
        }
        return super.triggerEvent(type, data)
    }

    override fun getItems(): NonNullList<ItemStack> = this.inventory

    override fun setItems(stacks: NonNullList<ItemStack>) {
        this.inventory = stacks
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return ChestMenu.threeRows(syncId, playerInventory, this)
    }

    fun onScheduledTick() {
        if (!this.remove) {
            stateManager.recheckOpeners(this.getLevel(), blockPos, this.blockState)
        }
    }

    fun onInvOpenOrClose(
        world: Level,
        pos: BlockPos,
        state: BlockState,
        oldViewerCount: Int,
        newViewerCount: Int
    ) {
        if (oldViewerCount != newViewerCount) {
            val block = state.block
            world.blockEvent(
                pos,
                block,
                if (newViewerCount > 0) OPEN_COUNT_EVENT else CLOSING_COUNT_EVENT,
                newViewerCount
            )
        }
    }

    companion object {
        private const val CLOSED_COUNT_EVENT = 0
        private const val CLOSING_COUNT_EVENT = 1
        private const val OPEN_COUNT_EVENT = 2
        const val MAX_OPENING_TICKS = 20
        fun tick(world: Level, pos: BlockPos, state: BlockState, blockEntity: DuskChestBlockEntity) {
            val phase = state.getValue(DuskProperties.CHEST_PHASE)
            if (phase.ordinal != 0) {
                if (phase.ordinal == 2) {
                    if (blockEntity.lidOpeningTicks < MAX_OPENING_TICKS)
                        blockEntity.lidOpeningTicks++
                } else if (blockEntity.lidOpeningTicks > 0) {
                    blockEntity.lidOpeningTicks--
                } else {
                    world.blockEvent(pos, state.block, CLOSED_COUNT_EVENT, 0)
                }
            }
        }


        fun DuskChestBlockEntity.shouldRenderLid(): Boolean {
            return this.blockState.getValue(DuskProperties.CHEST_PHASE) != ChestPhase.CLOSED || lidOpeningTicks > -5
        }

        fun playSound(world: Level, pos: BlockPos, state: BlockState, soundEvent: SoundEvent?) {
            val chestType = state.getValue(ChestBlock.TYPE)
            if (chestType != ChestType.RIGHT) {
                var posX = pos.x.toDouble() + 0.5
                val posY = pos.y.toDouble() + 0.5
                var posZ = pos.z.toDouble() + 0.5
                if (chestType == ChestType.LEFT) {
                    val direction = ChestBlock.getConnectedDirection(state)
                    posX += direction.stepX.toDouble() * 0.5
                    posZ += direction.stepZ.toDouble() * 0.5
                }

                world.playSound(
                    null,
                    posX, posY, posZ,
                    soundEvent,
                    SoundSource.BLOCKS,
                    0.5f,
                    world.random.nextFloat() * 0.1f + 0.9f
                )
            }
        }

        fun getPlayersLookingInChestCount(world: BlockGetter, pos: BlockPos?): Int {
            val blockState = world.getBlockState(pos)
            if (blockState.hasBlockEntity()) {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is DuskChestBlockEntity) {
                    return blockEntity.stateManager.openerCount
                }
            }

            return 0
        }

        fun copyInventory(from: DuskChestBlockEntity, to: DuskChestBlockEntity) {
            val defaultedList = from.items
            from.items = to.items
            to.items = defaultedList
        }
    }
}
