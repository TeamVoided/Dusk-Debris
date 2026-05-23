package org.teamvoided.dusk_debris.entity.ai.brain.sensor

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Iterables
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor
import net.minecraft.world.entity.player.Player
import org.teamvoided.dusk_debris.entity.GiantEnemyJellyfishEntity
import java.util.function.Predicate

class GiantEnemyJellyfishAttackEntiySensor : NearestLivingEntitySensor<GiantEnemyJellyfishEntity>() {
    override fun requires(): Set<MemoryModuleType<*>> {
        return ImmutableSet.copyOf(
            Iterables.concat(
                super.requires(),
                listOf(MemoryModuleType.NEAREST_ATTACKABLE)
            )
        )
    }

    override fun doTick(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity) {
        super.doTick(world, jellyfish)
        jellyfish.brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream()
            .flatMap { obj: List<LivingEntity> -> obj.stream() }
            .filter(JELLYFISH_ATTACKABLES)
            .filter { livingEntity: LivingEntity -> isEntityAttackable(jellyfish, livingEntity) }
            .findFirst()
            .ifPresentOrElse(
                { jellyfish.brain.setMemory(MemoryModuleType.NEAREST_ATTACKABLE, it) },
                { jellyfish.brain.eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE) })
    }

    override fun radiusXZ(): Int {
        return RANGE
    }

    override fun radiusY(): Int {
        return RANGE
    }

    companion object {
        const val RANGE: Int = 24
        val JELLYFISH_ATTACKABLES: Predicate<Entity> =
            Predicate { entity: Entity -> (entity is Player && !entity.isSpectator() && !entity.isCreative) }
    }
}
