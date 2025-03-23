package org.teamvoided.dusk_debris.entity

import net.minecraft.client.util.ColorUtil
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.EulerAngle
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems

class DiceEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out DiceEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity, stack: ItemStack) :
            super(DuskEntities.DIE, owner, world, stack, null as ItemStack?) {
        this.color = getDiceColor(stack)
    }

    constructor(world: World, x: Double, y: Double, z: Double, stack: ItemStack) :
            super(DuskEntities.DIE, x, y, z, world, stack, stack) {
        this.color = getDiceColor(stack)
    }

    var sideUp: Int
        get() = dataTracker[TRACKER_SIDE_UP]
        set(sideUp) {
            dataTracker[TRACKER_SIDE_UP] = sideUp
        }

    var rotationVec: EulerAngle
        get() = dataTracker[TRACKER_ROTATION]
        set(rotationVec) {
            dataTracker[TRACKER_ROTATION] = rotationVec
        }

    var timeSinceLastFall: Int
        get() = dataTracker[TRACKER_TIME_SINCE_LAST_FALL]
        set(timeSinceLastFall) {
            dataTracker[TRACKER_TIME_SINCE_LAST_FALL] = timeSinceLastFall
        }

    var color: Int
        get() = dataTracker[TRACKER_COLOR]
        set(color) {
            dataTracker[TRACKER_COLOR] = color
        }

    init {
        sideUp = 1
//        rotationVec = EulerAngle(random.nextFloat(), random.nextFloat(), random.nextFloat())
        rotationVec = EulerAngle(0f, 0f, 0f)
        timeSinceLastFall = 20
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(TRACKER_SIDE_UP, 1)
        builder.add(TRACKER_ROTATION, DEFAULT_ROTATION)
        builder.add(TRACKER_TIME_SINCE_LAST_FALL, 20)
        builder.add(TRACKER_COLOR, 0xFFFFFF)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        nbt.putInt("SideUp", this.sideUp)
        nbt.put("RotationVector", rotationVec.toNbt())
        nbt.putInt("TimeSinceLastFall", this.timeSinceLastFall)
        nbt.putInt("Color", this.color)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        val rotation = nbt.getList("RotationVector", 5)

        this.sideUp = nbt.getInt("SideUp")
        this.rotationVec = (if (rotation.isEmpty()) DEFAULT_ROTATION else EulerAngle(rotation))
        this.timeSinceLastFall = nbt.getInt("TimeSinceLastFall")
        this.color = nbt.getInt("Color")
    }

    override fun getDefaultItemStack(): ItemStack = DuskItems.DIE_ITEM.defaultStack

    override fun shouldRender(cameraX: Double, cameraY: Double, cameraZ: Double): Boolean {
        return true
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        if (world.isClient)
            sideUp = random.nextInt(5) + 1
        if (timeSinceLastFall < 4 && blockHitResult.side == Direction.UP) {
            rotationVec = rotationValues(sideUp)
            super.onBlockHit(blockHitResult)
        } else {
            //            rotationVec = EulerAngle(random.nextFloat(), random.nextFloat(), random.nextFloat())
            velocity = when (blockHitResult.side) {
                Direction.UP -> velocity.multiply(theEvil, theEvilY, theEvil)
                Direction.DOWN -> velocity.multiply(theEvil, -theEvil, theEvil)
                Direction.NORTH -> velocity.multiply(theEvil, theEvil, -theEvil)
                Direction.SOUTH -> velocity.multiply(theEvil, theEvil, -theEvil)
                Direction.EAST -> velocity.multiply(-theEvil, theEvil, theEvil)
                Direction.WEST -> velocity.multiply(-theEvil, theEvil, theEvil)
                else -> velocity
            }
        }
        timeSinceLastFall = 0
    }

    override fun tick() {
        super.tick()
        if (!isOnGround) {
//            rotationVec = EulerAngle(
//                rotationVec.pitch * tickRotateMult,
//                rotationVec.yaw * tickRotateMult,
//                rotationVec.roll * tickRotateMult
//            )
        }
        timeSinceLastFall++
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        val superResult = super.interact(player, hand)
        if (superResult == ActionResult.PASS)
            return superResult
        if (player.getStackInHand(hand).isEmpty) {
            player.giveItemStack(stack)
            this.kill()
            return ActionResult.success(world.isClient)
        }
        return ActionResult.PASS
    }

    override fun tryPickup(player: PlayerEntity?): Boolean = false
    override fun checkDespawn() {
        super.checkDespawn()
    }

    fun rotationValues(side: Int): EulerAngle {
        if (!(side >= 1 && side <= 6)) {
            println("oopsie :) --------------------------------------------------------------")
            println(side)
        }
        return when (side) {
            1 -> EulerAngle(rotate180, 0f, 0f)
            2 -> EulerAngle(0f, 0f, rotate270)
            3 -> EulerAngle(rotate90, 0f, 0f)
            4 -> EulerAngle(rotate270, 0f, 0f)
            5 -> EulerAngle(0f, 0f, rotate90)
            6 -> EulerAngle(0f, 0f, 0f)
            else -> EulerAngle(0f, 0f, 0f)
        }
    }

    override fun getDefaultGravity(): Double {
        return 0.07
    }

    override fun isPushable(): Boolean = false

    fun getDiceColor(itemStack: ItemStack): Int {
        return ColorUtil.Argb32.toOpaque(
            DyedColorComponent.getColorOrDefault(
                itemStack,
                0xFFFFFF
            )
        )
    }

//    private val particleParameters: ParticleEffect
//        get() {
//            val itemStack = this.stack
//            return (if (!itemStack.isEmpty && !itemStack.isOf(this.defaultItem)) ItemStackParticleEffect(
//                ParticleTypes.ITEM,
//                itemStack
//            ) else ParticleTypes.ITEM_SNOWBALL) as ParticleEffect
//        }

//    override fun handleStatus(status: Byte) {
//        if (status.toInt() == 3) {
//            val particleEffect = this.particleParameters
//
//            for (i in 0..7) {
//                world.addParticle(particleEffect, this.x, this.y, this.z, 0.0, 0.0, 0.0)
//            }
//        }
//    }

//    override fun onEntityHit(entityHitResult: EntityHitResult) {
//        super.onEntityHit(entityHitResult)
//        val entity = entityHitResult.entity
//        val i = if (entity is BlazeEntity) 3 else 0
//        entity.damage(this.damageSources.thrown(this, this.owner), i.toFloat())
//    }


//    override fun onCollision(hitResult: HitResult) {
//        super.onCollision(hitResult)
//        if (!world.isClient) {
//            world.sendEntityStatus(this, 3.toByte())
//            this.discard()
//        }
//    }

    companion object {
        val theEvil = 0.75
        val theEvilY = -0.2
        val tickRotateMult = 0.9f
        private val DEFAULT_ROTATION = EulerAngle(0f, 0f, 0f)
        val TRACKER_SIDE_UP: TrackedData<Int> = DataTracker.registerData(
            DiceEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
        val TRACKER_ROTATION: TrackedData<EulerAngle> = DataTracker.registerData(
            DiceEntity::class.java, TrackedDataHandlerRegistry.ROTATION
        )
        val TRACKER_TIME_SINCE_LAST_FALL: TrackedData<Int> = DataTracker.registerData(
            DiceEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
        val TRACKER_COLOR: TrackedData<Int> = DataTracker.registerData(
            DiceEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )


        // TODO replace with voidlib
        const val rotate45 = 0.785f
        const val rotate90 = 1.571f
        const val rotate135 = 2.356f
        const val rotate180 = 3.142f
        const val rotate225 = 3.927f
        const val rotate270 = 4.712f
        const val rotate315 = 5.498f
        const val rotate360 = 6.28319f
    }
}