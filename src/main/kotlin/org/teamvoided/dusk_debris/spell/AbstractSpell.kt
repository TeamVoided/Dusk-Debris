package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskParticles

open class AbstractSpell(
    val priority: Int,
    val coolup: Int = 8
) { //this class was abstract, but the codec was being annoying
    //open fun getCodec(): Codec<out AbstractSpell> = throw NotImplementedError("Function getCodec must be overridden")

    open fun castRequirements(castor: LivingEntity): Boolean = throw NotImplementedError("Function castRequirements must be overridden")

    open fun onCast(castor: LivingEntity) {}

    open fun castTick(castor: LivingEntity) {}

    open fun onCastEnd(castor: LivingEntity) {}

    open fun actualSpell(castor: LivingEntity) {}

    open fun nonEntityBehavior(world: World, random: RandomGenerator, pos: Vec3d, rotation: Vec3d) {
        if (world.isClient) {
            repeat(10) {
                val particlePos = Vec3d(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).add(pos)
                val velocity = rotation.multiply(-0.1)
                world.addParticle(
                    DuskParticles.DRAINED_SOUL,
                    particlePos.x, particlePos.y, particlePos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }
    }

    companion object {
        val SPELL_CASTING_MODIFIER_ID: Identifier = DuskDebris.id("spell_casting")


        val MAP_CODEC: MapCodec<AbstractSpell> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT
                        .fieldOf("priority") //runs through the registry and sorts by priority before running through cast requirements
                        .forGetter { it.priority },
                    Codec.INT
                        .fieldOf("coolup") //the time the spell takes, and how long until the next spell can be cast
                        .orElse(8)
                        .forGetter { it.coolup },
                ).apply(instance, ::AbstractSpell)
            }
    }
}