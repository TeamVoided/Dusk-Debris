package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.block.throwable_bomb.AbstractThrwowableBombBlock
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.world.explosion.BlunderbombExplosionBehavior

open class BonecallerEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out BonecallerEntity>, world: World) : super(entityType, world)
    constructor(entityType: EntityType<out BonecallerEntity>, owner: LivingEntity?, world: World) :
            super(entityType, owner, world)

    constructor(entityType: EntityType<out BonecallerEntity>, x: Double, y: Double, z: Double, world: World) :
            super(entityType, x, y, z, world)

    override val trailingParticle: ParticleEffect = ParticleTypes.SMOKE

    override fun explode() {
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )

        super.explode()
    }

    fun bonecall(livingEntity: LivingEntity) {
        val serverWorld = this.world
        val team: Team? = livingEntity.scoreboardTeam

        for (i in 0..3) {
//            val skeletonEntity = EntityType.SKELETON.create(world)
//            if (skeletonEntity != null) {
//                skeletonEntity.refreshPositionAndAngles(this.blockPos, 0.0f, 0.0f)
//                skeletonEntity.initialize(
//                    serverWorld,
//                    this.world.getLocalDifficulty(this.blockPos),
//                    SpawnReason.MOB_SUMMONED,
//                    null
//                )
//                if (team != null) {
//                    serverWorld.scoreboard.addPlayerToTeam(skeletonEntity.profileName, team)
//                }
//                serverWorld.emitGameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.create(this))
//            }
        }
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BLUNDERBOMB
    }
}