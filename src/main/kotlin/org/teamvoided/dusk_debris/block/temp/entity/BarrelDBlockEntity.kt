package org.teamvoided.dusk_debris.block.temp.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.ContainerOpenersCounter
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class BarrelDBlockEntity(pos: BlockPos, state: BlockState) :
    RandomizableContainerBlockEntity(BlockEntityType.BARREL, pos, state) {
    private var inventory: NonNullList<ItemStack>
    private val stateManager: ContainerOpenersCounter

    init {
        this.inventory = NonNullList.withSize(27, ItemStack.EMPTY)
        this.stateManager = object : ContainerOpenersCounter() {
            override fun onOpen(world: Level, pos: BlockPos, state: BlockState) {
                this@BarrelDBlockEntity.playSound(state, SoundEvents.BARREL_OPEN)
                this@BarrelDBlockEntity.setOpen(state, true)
            }

            override fun onClose(world: Level, pos: BlockPos, state: BlockState) {
                this@BarrelDBlockEntity.playSound(state, SoundEvents.BARREL_CLOSE)
                this@BarrelDBlockEntity.setOpen(state, false)
            }

            override fun openerCountChanged(
                world: Level,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {

            }

            override fun isOwnContainer(player: Player): Boolean {
                val currentScreenHandler = player.containerMenu
                if (currentScreenHandler is ChestMenu) {
                    val inventory = currentScreenHandler.container
                    return inventory == this@BarrelDBlockEntity
                } else {
                    return false
                }
            }
        }
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

    override fun getContainerSize(): Int = 27

    override fun getItems(): NonNullList<ItemStack> = this.inventory

    override fun setItems(stacks: NonNullList<ItemStack>) {
        this.inventory = stacks
    }

    override fun getDefaultName(): Component {
        return Component.translatable("container.barrel")
    }

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return ChestMenu.threeRows(syncId, playerInventory, this)
    }

    override fun startOpen(player: Player) {
        if (!this.remove && !player.isSpectator) {
            stateManager.incrementOpeners(player, this.getLevel(), blockPos, this.blockState)
        }
    }

    override fun stopOpen(player: Player) {
        if (!this.remove && !player.isSpectator) {
            stateManager.decrementOpeners(player, this.getLevel(), blockPos, this.blockState)
        }
    }

    fun tick() {
        if (!this.remove) {
            stateManager.recheckOpeners(this.getLevel(), blockPos, this.blockState)
        }
    }

    fun setOpen(state: BlockState, open: Boolean) {
        level!!.setBlock(blockPos, state.setValue(BlockStateProperties.OPEN, open), 3)
    }

    fun playSound(state: BlockState, soundEvent: SoundEvent?) {
        val vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).normal
        val d = worldPosition.x.toDouble() + 0.5 + (vec3i.x.toDouble() / 2.0)
        val e = worldPosition.y.toDouble() + 0.5 + (vec3i.y.toDouble() / 2.0)
        val f = worldPosition.z.toDouble() + 0.5 + (vec3i.z.toDouble() / 2.0)
        level!!.playSound(
            null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f,
            level!!.random.nextFloat() * 0.1f + 0.9f
        )
    }
}