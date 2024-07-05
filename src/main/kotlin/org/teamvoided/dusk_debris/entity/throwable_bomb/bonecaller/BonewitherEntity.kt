package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.SpawnUtil
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect

open class BonewitherEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonewitherEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONEWITHER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONEWITHER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONEWITHER, x, y, z, world)

    override fun doOtherSkeletonThings(skeletonEntity:SkeletonEntity) {
        skeletonEntity.equipStack(
            EquipmentSlot.MAINHAND, if (random.nextFloat() > 0.8f) {
                if (random.nextInt(3) < 2) {
                    ItemStack(DuskItems.BLACKSTONE_AXE)
                } else {
                    ItemStack(Items.BOW)
                }
            } else {
                ItemStack(DuskItems.BLACKSTONE_SWORD)
            }
        )
    }

    override fun getCalledEntity() = EntityType.WITHER_SKELETON
    override fun getDefaultItem(): Item {
        return DuskItems.BONEWITHER_ITEM
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0x515353, 0x303233)
}