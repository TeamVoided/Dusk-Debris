package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.StrayEntity
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

open class BonechillerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonechillerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONECHILLER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONECHILLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONECHILLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerWorld, bandanaColor: Int, team: Team?) {
        val bandana = DuskItems.BONECALLER_BANDANA.defaultStack
        val strayEntity = StrayEntity(EntityType.STRAY as EntityType<out StrayEntity>, world)
        val spawnPos = getSummonPos(
            strayEntity,
            SpawnReason.MOB_SUMMONED,
            serverWorld,
            blockPos,
            20,
            3,
            6,
            SpawnUtil.Strategy.field_39401
        )
        strayEntity.refreshPositionAndAngles(spawnPos, 0f, 0.0f)
        strayEntity.initialize(
            serverWorld,
            this.world.getLocalDifficulty(this.blockPos),
            SpawnReason.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponentTypes.DYED_COLOR,
            DyedColorComponent(bandanaColor, true)
        )
        strayEntity.equipStack(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(strayEntity.profileName, team)
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
        world.spawnEntity(strayEntity)
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BONECHILLER_ITEM
    }

    override fun bandanaColors(): Int {
        val hue = 180f + (Math.random().toFloat() * 90f)
        val saturation = 0.2f + (Math.random().toFloat() * 0.6f)
        val value = 0.2f + (Math.random().toFloat() * 0.6f)
        return Color.HSBtoRGB(hue / 360, saturation, value)
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0xE6ECED, 0xA2B6C4)
}