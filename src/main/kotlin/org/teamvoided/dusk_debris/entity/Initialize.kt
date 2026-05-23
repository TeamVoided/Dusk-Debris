package org.teamvoided.dusk_debris.entity

import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.level.ServerLevelAccessor

interface Initialize {
    fun initialize(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData
    ): SpawnGroupData?

    companion object {

    }
}