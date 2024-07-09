package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.BoggedEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.SpawnUtil
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color

open class BogcallerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BogcallerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BOGCALLER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BOGCALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BOGCALLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerWorld, bandanaColor: Int, team: Team?) {
        //overide this to get your own
        val bandana = DuskItems.BONECALLER_BANDANA.defaultStack
        val boggedEntity = BoggedEntity(EntityType.BOGGED as EntityType<out BoggedEntity>, world)
        val spawnPos = getSummonPos(
            boggedEntity,
            SpawnReason.MOB_SUMMONED,
            serverWorld,
            blockPos,
            20,
            3,
            6,
            SpawnUtil.Strategy.field_39401
        )
        boggedEntity.refreshPositionAndAngles(spawnPos, 0f, 0.0f)
        boggedEntity.initialize(
            serverWorld,
            this.world.getLocalDifficulty(this.blockPos),
            SpawnReason.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponentTypes.DYED_COLOR,
            DyedColorComponent(bandanaColor, true)
        )
        boggedEntity.equipStack(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(boggedEntity.profileName, team)
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
        world.spawnEntity(boggedEntity)
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BOGCALLER_ITEM
    }

    override fun bandanaColors(): Int {
        val hue = 70f + (Math.random().toFloat() * 210f)
        val saturation = 0.4f + (Math.random().toFloat() * 0.4f)
        val value = 0.1f + (Math.random().toFloat() * 0.4f)
        return Color.HSBtoRGB(hue / 360, saturation, value)
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0xEDE8BD, 0x93BA77)
}