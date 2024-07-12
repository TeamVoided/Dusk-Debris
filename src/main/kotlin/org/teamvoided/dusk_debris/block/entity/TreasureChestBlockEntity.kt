package org.teamvoided.dusk_debris.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.ChestLidAnimator
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.block.enums.ChestType
import net.minecraft.client.block.ChestAnimationProgress
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.DoubleInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskBlockEntities.TREASURE_CHEST

class TreasureChestBlockEntity : LootableContainerBlockEntity, ChestAnimationProgress {
    constructor(pos: BlockPos, state: BlockState) : super(TREASURE_CHEST, pos, state)

    private var inventory: DefaultedList<ItemStack>
    private val stateManager: ViewerCountManager
    private val lidAnimator: ChestLidAnimator

    init {
        this.inventory = DefaultedList.ofSize(CHEST_SIZE, ItemStack.EMPTY)
        this.stateManager = object : ViewerCountManager() {
            override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN)
            }

            override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {
                playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE)
            }

            override fun onViewerCountUpdate(
                world: World,
                pos: BlockPos,
                state: BlockState,
                oldViewerCount: Int,
                newViewerCount: Int
            ) {
                this@TreasureChestBlockEntity.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount)
            }

            override fun isPlayerViewing(player: PlayerEntity): Boolean {
                if (player.currentScreenHandler !is GenericContainerScreenHandler) {
                    return false
                } else {
                    val inventory = (player.currentScreenHandler as GenericContainerScreenHandler).inventory
                    return inventory === this@TreasureChestBlockEntity || inventory is DoubleInventory && inventory.isPart(
                        this@TreasureChestBlockEntity
                    )
                }
            }
        }
        this.lidAnimator = ChestLidAnimator()
    }
    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        if (!this.writeLootTableNbt(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, lookupProvider)
        }
    }

    override fun method_11014(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.method_11014(nbt, lookupProvider)
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
        if (!this.readLootTableNbt(nbt)) {
            Inventories.readNbt(nbt, this.inventory, lookupProvider)
        }
    }


    protected open fun onInvOpenOrClose(
        world: World,
        pos: BlockPos,
        state: BlockState,
        oldViewerCount: Int,
        newViewerCount: Int
    ) {
        val block = state.block
        world.addSyncedBlockEvent(pos, block, 1, newViewerCount)
    }


    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
        return BlockEntityUpdateS2CPacket.of(this)
    }

    override fun size(): Int = CHEST_SIZE

    override fun getContainerName(): Text = Text.translatable("container.chest")

    override fun method_11282(): DefaultedList<ItemStack> = this.inventory

    override fun method_11281(defaultedList: DefaultedList<ItemStack>) {
        this.inventory = defaultedList
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
    }

    override fun getAnimationProgress(tickDelta: Float): Float {
        return this.lidAnimator.getProgress(tickDelta);
    }
    fun tick() {
        if (!this.removed) {
            stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.cachedState)
        }
    }
    companion object {
        val CHEST_SIZE = 27
        fun playSound(world: World, pos: BlockPos, state: BlockState, soundEvent: SoundEvent?) {
            var d = pos.x.toDouble() + 0.5
            val e = pos.y.toDouble() + 0.5
            var f = pos.z.toDouble() + 0.5
            world.playSound(
                null as PlayerEntity,
                d,
                e,
                f,
                soundEvent,
                SoundCategory.BLOCKS,
                0.5f,
                world.random.nextFloat() * 0.1f + 0.9f
            )
        }
    }
}