package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import kotlin.math.cos
import kotlin.math.sin

open class NethershroomThrowableEntity : AbstractThrwowableBombEntity {

    //    val replaceItem: Item by lazy {  DuskItems.ANCIENT_BLACK_POWDER_BARREL}
    open var statusEffect: Holder<StatusEffect>? = null
    open var hasDoubleEffect: Boolean = false


    constructor(entityType: EntityType<out NethershroomThrowableEntity>, world: World) : super(entityType, world)

    constructor(entity: EntityType<out NethershroomThrowableEntity>, owner: LivingEntity?, world: World) : super(
        entity,
        owner,
        world
    )

    constructor(
        entity: EntityType<out NethershroomThrowableEntity>,
        world: World,
        x: Double,
        y: Double,
        z: Double
    ) : super(entity, x, y, z, world)

    override fun explode() {
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.7f + world.random.nextFloat() * 0.2f
        )
        if (statusEffect == null) {
            NethershroomPlantBlock.explode(world, this.blockPos, getTrailingParticle())
        } else {
            NethershroomPlantBlock.explode(
                world,
                this.blockPos,
                this.getTrailingParticle(),
                this.statusEffect!!,
                this.hasDoubleEffect
            )
        }
        super.explode()
    }
    override fun getTrailingParticle(): ParticleEffect = ParticleTypes.SMOKE
}