package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock

open class NethershroomThrowableEntity : AbstractThrwowableBombEntity {

    //    val replaceItem: Item by lazy {  DuskItems.ANCIENT_BLACK_POWDER_BARREL}
    open var statusEffect: Holder<MobEffect>? = null
    open var hasDoubleEffect: Boolean = false


    constructor(entityType: EntityType<out NethershroomThrowableEntity>, world: Level) : super(entityType, world)

    constructor(entity: EntityType<out NethershroomThrowableEntity>, owner: LivingEntity?, world: Level) : super(
        entity,
        owner,
        world
    )

    constructor(
        entity: EntityType<out NethershroomThrowableEntity>,
        world: Level,
        x: Double,
        y: Double,
        z: Double
    ) : super(entity, x, y, z, world)

    override fun explode() {
        level().playSound(
            this,
            this.blockPosition(),
            SoundEvents.GLASS_BREAK,
            SoundSource.BLOCKS,
            0.7f,
            0.7f + level().random.nextFloat() * 0.2f
        )
        if (statusEffect == null) {
            NethershroomPlantBlock.explode(level(), this.blockPosition(), getTrailingParticle())
        } else {
            NethershroomPlantBlock.explode(
                level(),
                this.blockPosition(),
                this.getTrailingParticle(),
                this.statusEffect!!,
                this.hasDoubleEffect
            )
        }
        super.explode()
    }
    override fun getTrailingParticle(): ParticleOptions = ParticleTypes.SMOKE
}