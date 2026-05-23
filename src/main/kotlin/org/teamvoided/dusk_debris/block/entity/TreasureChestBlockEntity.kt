package org.teamvoided.dusk_debris.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
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
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.ChestLidController
import net.minecraft.world.level.block.entity.ContainerOpenersCounter
import net.minecraft.world.level.block.entity.LidBlockEntity
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.init.DuskBlockEntities.TREASURE_CHEST

class TreasureChestBlockEntity : RandomizableContainerBlockEntity, LidBlockEntity {
    constructor(pos: BlockPos, state: BlockState) : super(TREASURE_CHEST, pos, state)

    private var inventory: NonNullList<ItemStack>
    private val stateManager: ContainerOpenersCounter
    private val lidAnimator: ChestLidController

    init {
        this.inventory = NonNullList.withSize(CHEST_SIZE, ItemStack.EMPTY)
        this.stateManager = object : ContainerOpenersCounter() {
            override fun onOpen(world: Level, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.CHEST_OPEN)
            }

            override fun onClose(world: Level, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.CHEST_CLOSE)
            }

            override fun openerCountChanged(
                world: Level,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {
                this@TreasureChestBlockEntity.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount)
            }

            override fun isOwnContainer(player: Player): Boolean {
                if (player.containerMenu !is ChestMenu) {
                    return false
                } else {
                    val inventory = (player.containerMenu as ChestMenu).container
                    return inventory === this@TreasureChestBlockEntity || inventory is CompoundContainer && inventory.contains(
                        this@TreasureChestBlockEntity
                    )
                }
            }
        }
        this.lidAnimator = ChestLidController()
    }
    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.saveAdditional(nbt, lookupProvider)
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory, lookupProvider)
        }
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.loadAdditional(nbt, lookupProvider)
        this.inventory = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.inventory, lookupProvider)
        }
    }


    protected open fun onInvOpenOrClose(
        world: Level,
        pos: BlockPos,
        state: BlockState,
        oldViewerCount: Int,
        newViewerCount: Int
    ) {
        val block = state.block
        world.blockEvent(pos, block, 1, newViewerCount)
    }


    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getContainerSize(): Int = CHEST_SIZE

    override fun getDefaultName(): Component = Component.translatable("container.treasure_chest")

    override fun getItems(): NonNullList<ItemStack> = this.inventory

    override fun setItems(defaultedList: NonNullList<ItemStack>) {
        this.inventory = defaultedList
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return ChestMenu.threeRows(syncId, playerInventory, this)
    }

    override fun getOpenNess(tickDelta: Float): Float {
        return this.lidAnimator.getOpenness(tickDelta);
    }
    fun tick() {
        if (!this.remove) {
            stateManager.recheckOpeners(this.getLevel(), blockPos, this.blockState)
        }
    }
    companion object {
        val CHEST_SIZE = 27
        fun playSound(world: Level, pos: BlockPos, state: BlockState, soundEvent: SoundEvent?) {
            var d = pos.x.toDouble() + 0.5
            val e = pos.y.toDouble() + 0.5
            var f = pos.z.toDouble() + 0.5
            world.playSound(
                null as Player,
                d,
                e,
                f,
                soundEvent,
                SoundSource.BLOCKS,
                0.5f,
                world.random.nextFloat() * 0.1f + 0.9f
            )
        }
    }
}