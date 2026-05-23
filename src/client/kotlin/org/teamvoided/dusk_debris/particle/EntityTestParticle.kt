package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.entity.EntityTestParticleEffect
import org.teamvoided.dusk_debris.util.Utils.rotate360
import kotlin.math.cos
import kotlin.math.sin

class EntityTestParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    val entity: Entity?
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {
    var rotationOffset = 0f

    init {
        this.lifetime = 50 + random.nextInt(50)
        this.quadSize = random.nextFloat() * 0.1f + 0.01f
        this.oRoll = roll
        this.friction = 0.9f
        this.xd = velX
        this.yd = velY
        this.zd = velZ
        rotationOffset = random.nextFloat()*rotate360
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    public override fun getLightColor(tint: Float): Int {
        return 15728880
    }

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            xd *= friction
            yd *= friction
            zd *= friction
            if (this.age % 3 == 0 && entity != null) {
                val orbitOffsetXZ = (entity.bbWidth) + 1
                val orbitOffsetY = (entity.bbHeight) * (age.toDouble() / lifetime)
                val velocity = Vec3(
                    orbitOffsetXZ * sin((age.toDouble() / 10)),
                    orbitOffsetY,
                    orbitOffsetXZ * cos((age.toDouble() / 10))
                ).add(entity.position()).subtract(Vec3(this.x, this.y, this.z)).scale(0.1)
                xd += velocity.x
                yd += velocity.y
                zd += velocity.z
            }
            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<EntityTestParticleEffect> {
        override fun createParticle(
            type: EntityTestParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val entity = type.entity
            val target = if (entity != null) {
                world.getEntity(entity)
            } else null
            val particle = EntityTestParticle(world, posX, posY, posZ, velX, velY, velZ, target)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}