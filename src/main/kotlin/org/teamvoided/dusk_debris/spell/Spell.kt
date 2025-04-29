package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.spell.config.SpellConfig
import java.util.function.Function

interface Spell<C : SpellConfig> {
    fun id() = DuskRegistries.SPELL.getId(this)!!

    fun castRequirements(castor: LivingEntity): Boolean = true

    fun onCast(castor: LivingEntity) {}

    fun castTick(castor: LivingEntity) {}

    fun onCastEnd(castor: LivingEntity) {}

    fun actualSpell(castor: LivingEntity)

    fun nonEntityBehavior(world: World, random: RandomGenerator, pos: Vec3d, rotation: Vec3d) {
        if (world.isClient) {
            repeat(10) {
                val particlePos = Vec3d(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).add(pos)
                val velocity = rotation.multiply(0.1)
                world.addParticle(
                    DuskParticles.DRAINED_SOUL,
                    particlePos.x, particlePos.y, particlePos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }
    }

    companion object {
        val CODEC: Codec<Spell<*>> = DuskRegistries.SPELL.codec
        //val PACKET_CODEC = PacketCodecs.fromCodec(DuskRegistries.Spell.codec)

        val SPELL_CASTING_MODIFIER_ID: Identifier = DuskDebris.id("spell_casting")
    }
}