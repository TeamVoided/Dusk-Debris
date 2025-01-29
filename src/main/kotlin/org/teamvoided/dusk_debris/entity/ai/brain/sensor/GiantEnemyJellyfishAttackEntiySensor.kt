package org.teamvoided.dusk_debris.entity.ai.brain.sensor

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Iterables
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.NearestLivingEntitiesSensor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import org.teamvoided.dusk_debris.entity.GiantEnemyJellyfishEntity
import java.util.function.Predicate

class GiantEnemyJellyfishAttackEntiySensor : NearestLivingEntitiesSensor<GiantEnemyJellyfishEntity>() {
    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.copyOf(
            Iterables.concat(
                super.getOutputMemoryModules(),
                listOf(MemoryModuleType.NEAREST_ATTACKABLE)
            )
        )
    }

    override fun sense(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity) {
        super.sense(world, jellyfish)
        jellyfish.brain.getOptionalMemory(MemoryModuleType.MOBS).stream()
            .flatMap { obj: List<LivingEntity> -> obj.stream() }
            .filter(JELLYFISH_ATTACKABLES)
            .filter { livingEntity: LivingEntity -> testAttackableTargetPredicate(jellyfish, livingEntity) }
            .findFirst()
            .ifPresentOrElse(
                { jellyfish.brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, it) },
                { jellyfish.brain.forget(MemoryModuleType.NEAREST_ATTACKABLE) })
    }

    override fun horizontalRadius(): Int {
        return RANGE
    }

    override fun verticalRadius(): Int {
        return RANGE
    }

    companion object {
        const val RANGE: Int = 24
        val JELLYFISH_ATTACKABLES: Predicate<Entity> =
            Predicate { entity: Entity -> (entity is PlayerEntity && !entity.isSpectator() && !entity.isCreative) }
    }
}
