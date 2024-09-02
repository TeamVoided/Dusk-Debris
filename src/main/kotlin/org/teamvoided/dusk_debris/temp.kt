////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.entity.ai.goal
//
//import net.minecraft.entity.ai.NoPenaltyTargeting
//import net.minecraft.entity.mob.PathAwareEntity
//import net.minecraft.util.math.Vec3d
//import java.util.*
//
//open class WanderAroundGoal(
//    protected val mob: PathAwareEntity,
//    protected val speed: Double,
//    protected var chance: Int,
//    private val canDespawn: Boolean
//) :
//    Goal() {
//    protected var targetX: Double = 0.0
//    protected var targetY: Double = 0.0
//    protected var targetZ: Double = 0.0
//    protected var ignoringChance: Boolean = false
//
//    @JvmOverloads
//    constructor(mob: PathAwareEntity, speed: Double, chance: Int = 120) : this(mob, speed, chance, true)
//
//    init {
//        this.controls = EnumSet.of(Control.MOVE)
//    }
//
//    override fun canStart(): Boolean {
//        if (mob.hasControllingPassenger()) {
//            return false
//        } else {
//            if (!this.ignoringChance) {
//                if (this.canDespawn && mob.despawnCounter >= 100) {
//                    return false
//                }
//
//                if (mob.method_59922().nextInt(toGoalTicks(this.chance)) != 0) {
//                    return false
//                }
//            }
//
//            val vec3d = this.wanderTarget
//            if (vec3d == null) {
//                return false
//            } else {
//                this.targetX = vec3d.x
//                this.targetY = vec3d.y
//                this.targetZ = vec3d.z
//                this.ignoringChance = false
//                return true
//            }
//        }
//    }
//
//    protected open val wanderTarget: Vec3d?
//        get() = NoPenaltyTargeting.find(this.mob, 10, 7)
//
//    override fun shouldContinue(): Boolean {
//        return !mob.navigation.isIdle && !mob.hasControllingPassenger()
//    }
//
//    override fun start() {
//        mob.navigation.startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed)
//    }
//
//    override fun stop() {
//        mob.navigation.stop()
//        super.stop()
//    }
//
//    fun ignoreChanceOnce() {
//        this.ignoringChance = true
//    }
//
//    fun setChance(chance: Int) {
//        this.chance = chance
//    }
//}