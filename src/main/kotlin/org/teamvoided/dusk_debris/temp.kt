//package net.minecraft.entity.passive
//
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.MovementType
//import net.minecraft.entity.ai.goal.Goal
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.damage.DamageSource
//import net.minecraft.entity.effect.StatusEffects
//import net.minecraft.entity.mob.MobEntity
//import net.minecraft.entity.mob.WaterCreatureEntity
//import net.minecraft.particle.ParticleEffect
//import net.minecraft.particle.ParticleTypes
//import net.minecraft.registry.tag.FluidTags
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.sound.SoundEvent
//import net.minecraft.sound.SoundEvents
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.math.Vec3d
//import net.minecraft.world.World
//
//open class SquidEntity(entityType: EntityType<out SquidEntity?>?, world: World?) :
//    WaterCreatureEntity(entityType, world) {
//    var tiltAngle: Float = 0f
//    var prevTiltAngle: Float = 0f
//    var rollAngle: Float = 0f
//    var prevRollAngle: Float = 0f
//    var thrustTimer: Float = 0f
//    var prevThrustTimer: Float = 0f
//    var tentacleAngle: Float = 0f
//    var prevTentacleAngle: Float = 0f
//    private var swimVelocityScale = 0f
//    private var thrustTimerSpeed: Float
//    private var turningSpeed = 0f
//    private var swimX = 0f
//    private var swimY = 0f
//    private var swimZ = 0f
//
//    init {
//        random.setSeed(id.toLong())
//        this.thrustTimerSpeed = 1.0f / (random.nextFloat() + 1.0f) * 0.2f
//    }
//
//    override fun initGoals() {
//        goalSelector.add(0, SwimGoal(this, this))
//        goalSelector.add(1, EscapeAttackerGoal())
//    }
//
//    override fun getAmbientSound(): SoundEvent? {
//        return SoundEvents.ENTITY_SQUID_AMBIENT
//    }
//
//    override fun getHurtSound(source: DamageSource): SoundEvent? {
//        return SoundEvents.ENTITY_SQUID_HURT
//    }
//
//    override fun getDeathSound(): SoundEvent? {
//        return SoundEvents.ENTITY_SQUID_DEATH
//    }
//
//    protected open val squirtSound: SoundEvent?
//        get() = SoundEvents.ENTITY_SQUID_SQUIRT
//
//    override fun canBeLeashed(): Boolean {
//        return true
//    }
//
//    override fun getSoundVolume(): Float {
//        return 0.4f
//    }
//
//    override fun getMoveEffect(): MoveEffect {
//        return MoveEffect.EVENTS
//    }
//
//    override fun getDefaultGravity(): Double {
//        return 0.08
//    }
//
//    override fun tickMovement() {
//        super.tickMovement()
//        this.prevTiltAngle = this.tiltAngle
//        this.prevRollAngle = this.rollAngle
//        this.prevThrustTimer = this.thrustTimer
//        this.prevTentacleAngle = this.tentacleAngle
//        this.thrustTimer += this.thrustTimerSpeed
//        if (thrustTimer.toDouble() > 6.283185307179586) {
//            if (world.isClient) {
//                this.thrustTimer = 6.2831855f
//            } else {
//                this.thrustTimer -= 6.2831855f
//                if (random.nextInt(10) == 0) {
//                    this.thrustTimerSpeed = 1.0f / (random.nextFloat() + 1.0f) * 0.2f
//                }
//
//                world.sendEntityStatus(this, 19.toByte())
//            }
//        }
//
//        if (this.isInsideWaterOrBubbleColumn) {
//            if (this.thrustTimer < 3.1415927f) {
//                val f = this.thrustTimer / 3.1415927f
//                this.tentacleAngle = MathHelper.sin(f * f * 3.1415927f) * 3.1415927f * 0.25f
//                if (f.toDouble() > 0.75) {
//                    this.swimVelocityScale = 1.0f
//                    this.turningSpeed = 1.0f
//                } else {
//                    this.turningSpeed *= 0.8f
//                }
//            } else {
//                this.tentacleAngle = 0.0f
//                this.swimVelocityScale *= 0.9f
//                this.turningSpeed *= 0.99f
//            }
//
//            if (!world.isClient) {
//                this.setVelocity(
//                    (this.swimX * this.swimVelocityScale).toDouble(),
//                    (this.swimY * this.swimVelocityScale).toDouble(),
//                    (this.swimZ * this.swimVelocityScale).toDouble()
//                )
//            }
//
//            val vec3d = this.velocity
//            val d = vec3d.horizontalLength()
//            this.bodyYaw += (-(MathHelper.atan2(vec3d.x, vec3d.z).toFloat()) * 57.295776f - this.bodyYaw) * 0.1f
//            this.yaw = this.bodyYaw
//            this.rollAngle += 3.1415927f * this.turningSpeed * 1.5f
//            this.tiltAngle += (-(MathHelper.atan2(d, vec3d.y).toFloat()) * 57.295776f - this.tiltAngle) * 0.1f
//        } else {
//            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.thrustTimer)) * 3.1415927f * 0.25f
//            if (!world.isClient) {
//                var e = velocity.y
//                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
//                    e = 0.05 * (getStatusEffect(StatusEffects.LEVITATION)!!.amplifier + 1).toDouble()
//                } else {
//                    e -= this.gravity
//                }
//
//                this.setVelocity(0.0, e * 0.9800000190734863, 0.0)
//            }
//
//            this.tiltAngle += (-90.0f - this.tiltAngle) * 0.02f
//        }
//    }
//
//    override fun damage(source: DamageSource, amount: Float): Boolean {
//        if (super.damage(source, amount) && this.attacker != null) {
//            if (!world.isClient) {
//                this.squirt()
//            }
//
//            return true
//        } else {
//            return false
//        }
//    }
//
//    private fun applyBodyRotations(shootVector: Vec3d): Vec3d {
//        var vec3d = shootVector.rotateX(this.prevTiltAngle * 0.017453292f)
//        vec3d = vec3d.rotateY(-this.prevBodyYaw * 0.017453292f)
//        return vec3d
//    }
//
//    private fun squirt() {
//        this.makeSound(this.squirtSound)
//        val vec3d = applyBodyRotations(Vec3d(0.0, -1.0, 0.0)).add(this.x, this.y, this.z)
//
//        for (i in 0..29) {
//            val vec3d2 = this.applyBodyRotations(
//                Vec3d(
//                    random.nextFloat().toDouble() * 0.6 - 0.3, -1.0,
//                    random.nextFloat().toDouble() * 0.6 - 0.3
//                )
//            )
//            val vec3d3 = vec3d2.multiply(0.3 + (random.nextFloat() * 2.0f).toDouble())
//            (world as ServerWorld).spawnParticles(
//                inkParticle, vec3d.x, vec3d.y + 0.5, vec3d.z, 0, vec3d3.x, vec3d3.y, vec3d3.z, 0.10000000149011612
//            )
//        }
//    }
//
//    protected open val inkParticle: ParticleEffect
//        get() = ParticleTypes.SQUID_INK
//
//    override fun travel(movementInput: Vec3d) {
//        this.move(MovementType.SELF, this.velocity)
//    }
//
//    override fun handleStatus(status: Byte) {
//        if (status.toInt() == 19) {
//            this.thrustTimer = 0.0f
//        } else {
//            super.handleStatus(status)
//        }
//    }
//
//    fun setSwimmingVector(x: Float, y: Float, z: Float) {
//        this.swimX = x
//        this.swimY = y
//        this.swimZ = z
//    }
//
//    fun hasSwimmingVector(): Boolean {
//        return this.swimX != 0.0f || (this.swimY != 0.0f) || (this.swimZ != 0.0f)
//    }
//
//    internal inner class SwimGoal(squidEntity: SquidEntity?, private val squid: SquidEntity) :
//        Goal() {
//        override fun canStart(): Boolean {
//            return true
//        }
//
//        override fun tick() {
//            val i = squid.getDespawnCounter()
//            if (i > 100) {
//                squid.setSwimmingVector(0.0f, 0.0f, 0.0f)
//            } else if (squid.getRandom()
//                    .nextInt(toGoalTicks(50)) == 0 || !squid.touchingWater || !squid.hasSwimmingVector()
//            ) {
//                val f = squid.getRandom().nextFloat() * 6.2831855f
//                val g = MathHelper.cos(f) * 0.2f
//                val h = -0.1f + squid.getRandom().nextFloat() * 0.2f
//                val j = MathHelper.sin(f) * 0.2f
//                squid.setSwimmingVector(g, h, j)
//            }
//        }
//    }
//
//    internal inner class EscapeAttackerGoal : Goal() {
//        private var timer = 0
//
//        override fun canStart(): Boolean {
//            val livingEntity = this@SquidEntity.attacker
//            return if (this@SquidEntity.isTouchingWater && livingEntity != null) {
//                this@SquidEntity.squaredDistanceTo(livingEntity) < 100.0
//            } else {
//                false
//            }
//        }
//
//        override fun start() {
//            this.timer = 0
//        }
//
//        override fun requiresUpdateEveryTick(): Boolean {
//            return true
//        }
//
//        override fun tick() {
//            ++this.timer
//            val livingEntity = this@SquidEntity.attacker
//            if (livingEntity != null) {
//                var vec3d = Vec3d(
//                    this@SquidEntity.x - livingEntity.x,
//                    this@SquidEntity.y - livingEntity.y,
//                    this@SquidEntity.z - livingEntity.z
//                )
//                val blockState = world.getBlockState(
//                    BlockPos.create(
//                        this@SquidEntity.x + vec3d.x,
//                        this@SquidEntity.y + vec3d.y,
//                        this@SquidEntity.z + vec3d.z
//                    )
//                )
//                val fluidState = world.getFluidState(
//                    BlockPos.create(
//                        this@SquidEntity.x + vec3d.x,
//                        this@SquidEntity.y + vec3d.y,
//                        this@SquidEntity.z + vec3d.z
//                    )
//                )
//                if (fluidState.isIn(FluidTags.WATER) || blockState.isAir) {
//                    val d = vec3d.length()
//                    if (d > 0.0) {
//                        vec3d.normalize()
//                        var e = 3.0
//                        if (d > 5.0) {
//                            e -= (d - 5.0) / 5.0
//                        }
//
//                        if (e > 0.0) {
//                            vec3d = vec3d.multiply(e)
//                        }
//                    }
//
//                    if (blockState.isAir) {
//                        vec3d = vec3d.subtract(0.0, vec3d.y, 0.0)
//                    }
//
//                    this@SquidEntity.setSwimmingVector(
//                        vec3d.x.toFloat() / 20.0f,
//                        vec3d.y.toFloat() / 20.0f,
//                        vec3d.z.toFloat() / 20.0f
//                    )
//                }
//
//                if (this.timer % 10 == 5) {
//                    world.addParticle(
//                        ParticleTypes.BUBBLE,
//                        this@SquidEntity.x,
//                        this@SquidEntity.y,
//                        this@SquidEntity.z, 0.0, 0.0, 0.0
//                    )
//                }
//            }
//        }
//
//        companion object {
//            private const val SPEED_MULTIPLIER = 3.0f
//            private const val MIN_DISTANCE = 5.0f
//            private const val MAX_DISTANCE = 10.0f
//        }
//    }
//
//    companion object {
//        fun createAttributes(): DefaultAttributeContainer.Builder {
//            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
//        }
//    }
//}