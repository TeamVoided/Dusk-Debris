////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.client.particle
//
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.particle.DustColorTransitionParticleEffect
//import org.joml.Vector3f
//
//@Environment(EnvType.CLIENT)
//class DustColorTransitionParticle protected constructor(
//    world: ClientWorld?,
//    x: Double,
//    y: Double,
//    z: Double,
//    velocityX: Double,
//    velocityY: Double,
//    velocityZ: Double,
//    particleEffect: DustColorTransitionParticleEffect,
//    spriteProvider: SpriteProvider?
//) :
//    AbstractDustParticle<DustColorTransitionParticleEffect?>(
//        world,
//        x,
//        y,
//        z,
//        velocityX,
//        velocityY,
//        velocityZ,
//        particleEffect,
//        spriteProvider
//    ) {
//    private val startColor: Vector3f
//    private val endColor: Vector3f
//
//    init {
//        val f: Float = this.random.nextFloat() * 0.4f + 0.6f
//        this.startColor = this.randomizeColor(particleEffect.startColor, f)
//        this.endColor = this.randomizeColor(particleEffect.toColor, f)
//    }
//
//    private fun randomizeColor(color: Vector3f, factor: Float): Vector3f {
//        return Vector3f(
//            this.randomizeColor(color.x(), factor),
//            this.randomizeColor(color.y(), factor),
//            this.randomizeColor(color.z(), factor)
//        )
//    }
//
//    private fun lerpColors(tickDelta: Float) {
//        val f: Float = (this.age.toFloat() + tickDelta) / (this.maxAge.toFloat() + 1.0f)
//        val vector3f = Vector3f(this.startColor).lerp(this.endColor, f)
//        this.colorRed = vector3f.x()
//        this.colorGreen = vector3f.y()
//        this.colorBlue = vector3f.z()
//    }
//
//    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
//        this.lerpColors(tickDelta)
//        super.buildGeometry(vertexConsumer, camera, tickDelta)
//    }
//
//    @Environment(EnvType.CLIENT)
//    class Factory(spriteProvider: SpriteProvider) : ParticleFactory<DustColorTransitionParticleEffect?> {
//        private val spriteProvider: SpriteProvider = spriteProvider
//
//        override fun createParticle(
//            dustColorTransitionParticleEffect: DustColorTransitionParticleEffect,
//            world: ClientWorld,
//            d: Double,
//            e: Double,
//            f: Double,
//            g: Double,
//            h: Double,
//            i: Double
//        ): net.minecraft.client.particle.Particle? {
//            return DustColorTransitionParticle(
//                world, d, e, f, g, h, i, dustColorTransitionParticleEffect,
//                this.spriteProvider
//            )
//        }
//    }
//}