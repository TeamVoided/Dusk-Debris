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
    var replaceItem: Item = DuskItems.ANCIENT_BLACK_POWDER_BARREL
    var particle: ParticleEffect = ParticleTypes.SMOKE
    var statusEffect: Holder<StatusEffect>? = null
    var hasDoubleEffect: Boolean = false

    constructor(
        entityType: EntityType<out NethershroomThrowableEntity>,
        world: World
    ) : super(entityType, world) {
        println("construct 1")
        println(entityType)
        println(this.replaceItem)
        println(replaceItem)
    }

    constructor(
        entityType: EntityType<out NethershroomThrowableEntity>,
        world: World,
        item: Item
    ) : super(entityType, world) {
        println("construct 2")
        println(this.replaceItem)
        println(item)
        this.replaceItem = item
        println(this.replaceItem)
        println(item)
    }

    constructor(
        world: World,
        owner: LivingEntity?,
        item: Item,
        entity: EntityType<out NethershroomThrowableEntity>,
        particle: ParticleEffect,
        statusEffect: Holder<StatusEffect>?,
        hasDoubleEffect: Boolean,
    ) : super(entity, owner, world) {
        println("construct 3")
        println(this.replaceItem)
        println(item)
        this.replaceItem = item
        println(this.replaceItem)
        println(item)
        this.particle = particle
        this.statusEffect = statusEffect
        this.hasDoubleEffect = hasDoubleEffect
    }

    constructor(
        world: World,
        x: Double,
        y: Double,
        z: Double,
        item: Item,
        entity: EntityType<out NethershroomThrowableEntity>,
        particle: ParticleEffect,
        statusEffect: Holder<StatusEffect>?,
        hasDoubleEffect: Boolean,
    ) :
            this(entity, world, item) {
        println("construct 4")
        val randVelocity = world.random.nextDouble() * 6.3
        this.setPosition(x, y, z)
        this.setVelocity(-sin(randVelocity) * 0.02, 0.04, -cos(randVelocity) * 0.02)
        this.prevX = x
        this.prevY = y
        this.prevZ = z
        println(this.replaceItem)
        println(item)
        this.replaceItem = item
        println(this.replaceItem)
        println(item)
        this.particle = particle
        this.statusEffect = statusEffect
        this.hasDoubleEffect = hasDoubleEffect
    }
    override val trailingParticle: ParticleEffect = ParticleTypes.SMOKE

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
            NethershroomPlantBlock.explode(world, this.blockPos, this.particle)
        } else {
            NethershroomPlantBlock.explode(
                world,
                this.blockPos,
                this.particle,
                this.statusEffect!!,
                this.hasDoubleEffect
            )
        }
        super.explode()
    }

    override fun getDefaultItem(): Item {
        println(this.replaceItem)
        return Items.SOUL_LANTERN
    }
}