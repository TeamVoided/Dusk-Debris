package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.EntityData
import net.minecraft.entity.SpawnReason
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess

interface Initialize {
    fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData
    ): EntityData?

    companion object {

    }
}