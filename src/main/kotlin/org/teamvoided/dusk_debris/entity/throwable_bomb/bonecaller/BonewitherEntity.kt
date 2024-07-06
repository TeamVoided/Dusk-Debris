package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.SpawnUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color

open class BonewitherEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonewitherEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONEWITHER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONEWITHER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONEWITHER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerWorld, bandanaColor: Int, team: Team?) {
        //overide this to get your own
        val bandana = DuskItems.BONECALLER_BANDANA.defaultStack
        val WitherSkeletonEntity = WitherSkeletonEntity(EntityType.WITHER_SKELETON as EntityType<out WitherSkeletonEntity>, world)
        val spawnPos = getSummonPos(
            WitherSkeletonEntity,
            SpawnReason.MOB_SUMMONED,
            serverWorld,
            blockPos,
            20,
            3,
            6,
            SpawnUtil.Strategy.field_39401
        )
        WitherSkeletonEntity.refreshPositionAndAngles(spawnPos, 0f, 0.0f)
        WitherSkeletonEntity.initialize(
            serverWorld,
            this.world.getLocalDifficulty(this.blockPos),
            SpawnReason.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponentTypes.DYED_COLOR,
            DyedColorComponent(bandanaColor, true)
        )
        WitherSkeletonEntity.equipStack(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(WitherSkeletonEntity.profileName, team)
        }
        serverWorld.spawnParticles(
            getTrailingParticle(),
            spawnPos.x + 0.5,
            spawnPos.y.toDouble(),
            spawnPos.z + 0.5,
            20,
            0.0,
            0.0,
            0.0,
            1.0
        )
        world.spawnEntity(WitherSkeletonEntity)
    }

    override val color1: Color = Color(0x515353)
    override val color2: Color = Color(0x303233)
    override fun bandanaColors(): Int {
        val f = (Math.random().toFloat())
        return color1.lerp(color2, f).rgb
    }
    override fun getDefaultItem(): Item {
        return DuskItems.BONEWITHER_ITEM
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0x515353, 0x303233)
}