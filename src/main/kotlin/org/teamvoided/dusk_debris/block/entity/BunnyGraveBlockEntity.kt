package org.teamvoided.dusk_debris.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.block.BunnyGraveBlock
import org.teamvoided.dusk_debris.entity.DustBunnyEntity
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.init.DuskEntities
import java.util.*

open class BunnyGraveBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(DuskBlockEntities.BUNNY_GRAVE, pos, state) {
    private val bunnyIds = mutableListOf<UUID>()
    var dustBunnies: MutableList<Entity> = mutableListOf()
        get() {
            if (level is ServerLevel && field.isEmpty() && bunnyIds.isNotEmpty()) {
                field = bunnyIds.mapNotNull { uuid -> (level as ServerLevel).getEntity(uuid) }.toMutableList()
            }
            return field
        }

    private var timeSinceLastBunny = 0

    override fun triggerEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            summonBunny()
            return true
        } else {
            return super.triggerEvent(type, data)
        }
    }

    fun summonBunny() {
        val blockPos = this.blockPos
        if (level != null) {
            val entity = DustBunnyEntity(DuskEntities.DUST_BUNNY, level!!)
            entity.moveTo(blockPos.center, 0f, 0f)
            entity.summonedPos = blockPos
            entity.finalizeSpawn(
                level as ServerLevel,
                this.level?.getCurrentDifficultyAt(blockPos),
                MobSpawnType.MOB_SUMMONED,
                null
            )
            level!!.addFreshEntity(entity)
            dustBunnies.addLast(entity)
            this.timeSinceLastBunny = 0
            this.setChanged()
        }
    }

    fun getDustBunniesFromBlock(): MutableList<Entity> {
        return dustBunnies
    }

    fun removeDustBunny(entity: LivingEntity) {
        dustBunnies.removeIf { it.uuid == entity.uuid }
        bunnyIds.remove(entity.uuid)
    }

    override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
        val nbt = CompoundTag()
        writeBunnies(nbt)
        return nbt
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.saveAdditional(nbt, lookupProvider)
        writeBunnies(nbt)
    }

    private fun writeBunnies(nbt: CompoundTag) {
        val list = ListTag()
        dustBunnies.forEach { entity -> list.add(StringTag.valueOf(entity.stringUUID)) }
        nbt.put(TAG_KEY, list)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        val list = nbt.getList(TAG_KEY, Tag.TAG_STRING.toInt())
        bunnyIds.clear()
        bunnyIds.addAll(list.map { UUID.fromString(it.asString) })
        super.loadAdditional(nbt, lookupProvider)
    }

    companion object {
        val TAG_KEY = "dustBunnies"

        fun bunniesAmount(dustState: Int): Int = dustState * 3
        fun tick(
            world: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: BunnyGraveBlockEntity
        ) {
            if (blockEntity.timeSinceLastBunny < 1200) {
                ++blockEntity.timeSinceLastBunny
            }
            if (blockEntity.timeSinceLastBunny % 20 == 0) {
                val dustState = state.getValue(BunnyGraveBlock.DUST)
                val dustBunniesAmount = blockEntity.dustBunnies.size
                if (dustBunniesAmount < bunniesAmount(dustState)) {
                    if (world.random.nextFloat() < dustState * 0.7) {
                        blockEntity.triggerEvent(1, 1)
                    }
                } else if (dustBunniesAmount > bunniesAmount(dustState)) {
                    val entity = blockEntity.dustBunnies.first()
                    entity.hurt(entity.damageSources().starve(), 1.0f)
                }
            }
        }

        fun serverTick(world: Level, pos: BlockPos, state: BlockState, blockEntity: BunnyGraveBlockEntity) {
            tick(world, pos, state, blockEntity)
        }
    }
}