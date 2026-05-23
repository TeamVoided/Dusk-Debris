package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.complex
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.*
import org.teamvoided.dusk_debris.particle.color.*
import org.teamvoided.dusk_debris.particle.entity.DivingParticleEffect
import org.teamvoided.dusk_debris.particle.entity.EinsteinParticleEffect
import org.teamvoided.dusk_debris.particle.entity.EntityTestParticleEffect


@Suppress("HasPlatformType")
object DuskParticles {
    //the only difference in Class.REGISTER is that it doesent fit on one line
    val TOXIC_SMOKE_PARTICLE = NethershroomSporeParticleEffect.REGISTER
    val GUNPOWDER_EXPLOSION_SMOKE = GunpowderExplosionSmokeParticleEffect.REGISTER
    val GUNPOWDER_EXPLOSION_EMMITER = GunpowderExplosionEmitterParticleEffect.REGISTER
    val BLUNDERBOMB = simple()
    val FIREBOMB = simple()
    val BONECALLER = complex(BonecallerParticleEffect.CODEC, BonecallerParticleEffect.PACKET_CODEC)
    val GEYSER = simple()

    val GODHOME = complex(GodhomeParticleEffect.CODEC, GodhomeParticleEffect.PACKET_CODEC)
    val DRAINED_SOUL = simple()
    val SPELL = simple()
    val SPELL_DIVE_FOLLOW = simple()
    val SPELL_DIVE = complex(DivingParticleEffect.CODEC, DivingParticleEffect.PACKET_CODEC)
    val CUBE = simple()
    val UNDERACID = simple()
    val ACID_BUBBLE_POP = simple()
    val STATIONARY_EMITTER = StationaryEmitterParticleEffect.REGISTER
    val SMALL_PURPLE_BUBBLE_CUBE = simple()
    val PURPLE_BIOME_BUBBLE = simple()
    val PURPLE_BUBBLE = simple()

    val WIND = complex(WindParticleEffect.CODEC, WindParticleEffect.PACKET_CODEC)
    val EXHAUST_WARMUP = simple()
    val EXHAUST_BLAST = simple()
    val SNAIL = simple()

    val SPARK = simple()
    val RISING_EMBER = simple()

    val COSMOS = simple()
    val ENTITY_TEST = complex(EntityTestParticleEffect.CODEC, EntityTestParticleEffect.PACKET_CODEC)
    val EINSTEIN_ORBIT = complex(EinsteinParticleEffect.CODEC, EinsteinParticleEffect.PACKET_CODEC)

    val FLASH = complex(FlashParticleEffect.CODEC, FlashParticleEffect.PACKET_CODEC)

    val ASTRAS_FLYING_GOOP = complex(GoopFlyingParticleEffect.CODEC, GoopFlyingParticleEffect.PACKET_CODEC)
    val ASTRAS_LANDED_GOOP = complex(GoopLandedParticleEffect.CODEC, GoopLandedParticleEffect.PACKET_CODEC)

    val SHRIEK_DIRECTIONAL = ShriekDirectionalParticleEffect.REGISTER

    val BETWEEN_POINTS = complex(BetweenPointsParticleEffect.CODEC, BetweenPointsParticleEffect.PACKET_CODEC)

    // DnD
    val SPIDERLILY = simple()
    val MUSHROOM_LAUNCH = simple()
    val DUST_BUNNY = complex(DustBunnyParticleEffect.CODEC, DustBunnyParticleEffect.PACKET_CODEC)
    val SPIRAL = complex(SpiralParticleEffect.CODEC, SpiralParticleEffect.PACKET_CODEC)

    fun init() {
        register("toxic_smoke_particle", TOXIC_SMOKE_PARTICLE)
        register("gunpowder_explosion_smoke", GUNPOWDER_EXPLOSION_SMOKE)
        register("gunpowder_explosion_emitter", GUNPOWDER_EXPLOSION_EMMITER)
        register("blunderbomb", BLUNDERBOMB)
        register("firebomb", FIREBOMB)
        register("bonecaller", BONECALLER)
        register("geyser", GEYSER)

        register("godhome", GODHOME)
        register("drained_soul", DRAINED_SOUL)
        register("spell", SPELL)
        register("spell_dive", SPELL_DIVE)
        register("cube", CUBE)
        register("underacid", UNDERACID)
        register("acid_bubble_pop", ACID_BUBBLE_POP)
        register("small_purple_bubble_cube", SMALL_PURPLE_BUBBLE_CUBE)
        register("purple_biome_bubble", PURPLE_BIOME_BUBBLE)
        register("purple_bubble", PURPLE_BUBBLE)

        register("wind", WIND)
        register("exhaust_warmup", EXHAUST_WARMUP)
        register("exhaust_blast", EXHAUST_BLAST)
        register("snail", SNAIL)
        register("spark", SPARK)
        register("rising_ember", RISING_EMBER)

        register("cosmos", COSMOS)
        register("entity_test", ENTITY_TEST)
        register("einstein_orbit", EINSTEIN_ORBIT)

        register("goop_flying", ASTRAS_FLYING_GOOP)
        register("goop_landed", ASTRAS_LANDED_GOOP)

        register("shriek_directional", SHRIEK_DIRECTIONAL)

        register("between_points", BETWEEN_POINTS)

        // DnD
        register("spiderlily", SPIDERLILY)
        register("mushroom_launch", MUSHROOM_LAUNCH)
        register("dust_bunny", DUST_BUNNY)
        register("spiral", SPIRAL)
    }

    fun register(id: String, particleType: ParticleType<*>) =
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), particleType)
}