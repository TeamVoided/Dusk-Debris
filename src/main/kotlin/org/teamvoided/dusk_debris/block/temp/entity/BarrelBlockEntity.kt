package org.teamvoided.dusk_debris.block.temp.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BarrelBlockEntity(pos: BlockPos, state: BlockState) :
    LootableContainerBlockEntity(BlockEntityType.BARREL, pos, state) {
    private var inventory: DefaultedList<ItemStack>
    private val stateManager: ViewerCountManager

    init {
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
        this.stateManager = object : ViewerCountManager() {
            override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {
                this@BarrelBlockEntity.playSound(state, SoundEvents.BLOCK_BARREL_OPEN)
                this@BarrelBlockEntity.setOpen(state, true)
            }

            override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {
                this@BarrelBlockEntity.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE)
                this@BarrelBlockEntity.setOpen(state, false)
            }

            override fun onViewerCountUpdate(
                world: World,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {

            }

            override fun isPlayerViewing(player: PlayerEntity): Boolean {
                val currentScreenHandler = player.currentScreenHandler
                if (currentScreenHandler is GenericContainerScreenHandler) {
                    val inventory = currentScreenHandler.inventory
                    return inventory == this@BarrelBlockEntity
                } else {
                    return false
                }
            }
        }
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        if (!this.writeLootTableNbt(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, lookupProvider)
        }
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.readNbtImpl(nbt, lookupProvider)
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
        if (!this.readLootTableNbt(nbt)) {
            Inventories.readNbt(nbt, this.inventory, lookupProvider)
        }
    }

    override fun size(): Int = 27

    override fun getInventory(): DefaultedList<ItemStack> = this.inventory

    override fun setInventory(stacks: DefaultedList<ItemStack>) {
        this.inventory = stacks
    }

    override fun getContainerName(): Text {
        return Text.translatable("container.barrel")
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
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

    fun tick() {
        if (!this.removed) {
            stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    fun setOpen(state: BlockState, open: Boolean) {
        world!!.setBlockState(this.getPos(), state.with(Properties.OPEN, open), 3)
    }

    fun playSound(state: BlockState, soundEvent: SoundEvent?) {
        val vec3i = state.get(Properties.HORIZONTAL_FACING).vector
        val d = pos.x.toDouble() + 0.5 + (vec3i.x.toDouble() / 2.0)
        val e = pos.y.toDouble() + 0.5 + (vec3i.y.toDouble() / 2.0)
        val f = pos.z.toDouble() + 0.5 + (vec3i.z.toDouble() / 2.0)
        world!!.playSound(
            null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f,
            world!!.random.nextFloat() * 0.1f + 0.9f
        )
    }
}