package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.registry.HolderLookup
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.block.BunnyGraveBlock
import org.teamvoided.dusks_and_dungeons.entity.DustBunnyEntity
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities
import org.teamvoided.dusks_and_dungeons.init.DnDEntities
import java.util.*

open class BunnyGraveBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(DnDBlockEntities.BUNNY_GRAVE, pos, state) {
    private val bunnyIds = mutableListOf<UUID>()
    var dustBunnies: MutableList<Entity> = mutableListOf()
        get() {
            if (world is ServerWorld && field.isEmpty() && bunnyIds.isNotEmpty()) {
                field = bunnyIds.mapNotNull { uuid -> (world as ServerWorld).getEntity(uuid) }.toMutableList()
            }
            return field
        }

    private var timeSinceLastBunny = 0

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            summonBunny()
            return true
        } else {
            return super.onSyncedBlockEvent(type, data)
        }
    }

    fun summonBunny() {
        val blockPos = this.getPos()
        if (world != null) {
            val entity = DustBunnyEntity(DnDEntities.DUST_BUNNY, world!!)
            entity.refreshPositionAndAngles(blockPos.ofCenter(), 0f, 0f)
            entity.summonedPos = blockPos
            entity.initialize(
                world as ServerWorld,
                this.world?.getLocalDifficulty(blockPos),
                SpawnReason.MOB_SUMMONED,
                null
            )
            world!!.spawnEntity(entity)
            dustBunnies.addLast(entity)
            this.timeSinceLastBunny = 0
            this.markDirty()
        }
    }

    fun getDustBunniesFromBlock(): MutableList<Entity> {
        return dustBunnies
    }

    fun removeDustBunny(entity: LivingEntity) {
        dustBunnies.removeIf { it.uuid == entity.uuid }
        bunnyIds.remove(entity.uuid)
    }

    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider): NbtCompound {
        val nbt = NbtCompound()
        writeBunnies(nbt)
        return nbt
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        writeBunnies(nbt)
    }

    private fun writeBunnies(nbt: NbtCompound) {
        val list = NbtList()
        dustBunnies.forEach { entity -> list.add(NbtString.of(entity.uuidAsString)) }
        nbt.put(TAG_KEY, list)
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        val list = nbt.getList(TAG_KEY, NbtElement.STRING_TYPE.toInt())
        bunnyIds.clear()
        bunnyIds.addAll(list.map { UUID.fromString(it.asString()) })
        super.readNbtImpl(nbt, lookupProvider)
    }

    companion object {
        val TAG_KEY = "dustBunnies"

        fun bunniesAmount(dustState: Int): Int = dustState * 3
        fun tick(
            world: World,
            pos: BlockPos,
            state: BlockState,
            blockEntity: BunnyGraveBlockEntity
        ) {
            if (blockEntity.timeSinceLastBunny < 1200) {
                ++blockEntity.timeSinceLastBunny
            }
            if (blockEntity.timeSinceLastBunny % 20 == 0) {
                val dustState = state.get(BunnyGraveBlock.DUST)
                val dustBunniesAmount = blockEntity.dustBunnies.size
                if (dustBunniesAmount < bunniesAmount(dustState)) {
                    if (world.random.nextFloat() < dustState * 0.7) {
                        blockEntity.onSyncedBlockEvent(1, 1)
                    }
                } else if (dustBunniesAmount > bunniesAmount(dustState)) {
                    val entity = blockEntity.dustBunnies.first()
                    entity.damage(entity.damageSources.starve(), 1.0f)
                }
            }
        }

        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: BunnyGraveBlockEntity) {
            tick(world, pos, state, blockEntity)
        }
    }
}