package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CalibratedSculkSensorBlock
import net.minecraft.block.sculk.SculkBlock
import net.minecraft.block.sculk.SculkShriekerBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.BlockTags
import net.minecraft.sound.SoundCategory
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.feature.SculkPatchFeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.util.asProperty
import org.teamvoided.dusk_debris.util.rotateVoxelShape

object SculkDirectionalStuff {

    /* - - - BLOCK FUNCTIONS - - - */
    private val HALF_BLOCK: VoxelShape = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0)
    private val HALF_NORTH_BLOCK: VoxelShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0)
    val FACING: DirectionProperty = Properties.FACING

    @JvmStatic
    fun getDirectionalSlabShape(state: BlockState, cir: CallbackInfoReturnable<VoxelShape>) {
        val facing = state.get(FACING)
        if (facing.axis != Direction.Axis.Y) {
            val times = state.get(FACING).horizontal
            cir.returnValue = rotateVoxelShape(times, HALF_NORTH_BLOCK)
        } else if (facing == Direction.DOWN) {
            cir.returnValue = HALF_BLOCK
        }
    }

    @JvmStatic
    fun getPlacementState(supr: BlockState?, ctx: ItemPlacementContext?): BlockState? {
        return if (ctx == null || supr == null) supr
        else supr.with(FACING, ctx.side)
    }

    @JvmStatic
    fun isNotUp(state: BlockState) = state.get(Properties.FACING) != Direction.UP

    @JvmStatic
    fun isNotCalibrated(block: Block) = block !is CalibratedSculkSensorBlock

    @JvmStatic
    fun isNotUpCalibrated(state: BlockState) =
        isNotCalibrated(state.block) && isNotUp(state)

    @JvmStatic
    fun spin(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state.get(FACING)))

    @JvmStatic
    fun spin(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(FACING)))

    @JvmStatic
    fun noCreativeFlightAnnoyance(entity: Entity): Boolean =
        !(entity is PlayerEntity && entity.isCreative && entity.abilities.flying)


    /* - - - SPREADING FUNCTIONS - - - */
    @JvmStatic
    fun tryUseChargeSpreadRewrite(
        world: WorldAccess,
        pos: BlockPos,
        charge: Int,
        cost: Int,
        random: RandomGenerator,
        canSummon: Boolean
    ): Boolean {
        val worldState = world.getBlockState(pos)
        val worldBlock = worldState.block
        if (worldBlock is SculkBlock) {
            if (random.nextInt(cost) < charge) {
                Direction.entries.forEach {
                    val posOffset: BlockPos = pos.offset(it)
                    if (canSpreadTo(world, pos, it)) {
                        var blockState: BlockState =
                            worldBlock.getRandomGrowthState(world, posOffset, random, canSummon)
                        if (blockState.contains(Properties.FACING) || it == Direction.UP) {
                            blockState = blockState.withIfExists(Properties.FACING, it)
                            world.setBlockState(posOffset, blockState, 3)
                            sound(world, pos, blockState)
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun sound(world: WorldAccess, pos: BlockPos, state: BlockState) =
        world.playSound(null, pos, state.soundGroup.placeSound, SoundCategory.BLOCKS, 1f, 1f)

    private fun canSpreadTo(world: WorldAccess, pos: BlockPos, direction: Direction): Boolean {
        val upState = world.getBlockState(pos.offset(direction))
        if (upState.isAir || upState.isOf(Blocks.WATER) && upState.fluidState.isOf(Fluids.WATER)) {
            val search: Iterator<BlockPos> = getIteratorFromDirection(pos, direction)

            var limit = 0
            search.forEach {
                if (world.getBlockState(it).isIn(DuskBlockTags.SCULK_SPREAD_SEARCH) && ++limit > 2) {
                    return false
                }
            }

            return true
        } else {
            return false
        }
    }
//            do {
//                if (!search.hasNext()) {
//                    return true
//                }
//
//                val searchPos = search.next()
//                val worldState = world.getBlockState(searchPos)
//                if (worldState.isIn(DuskBlockTags.SCULK_SPREAD_SEARCH)) {
//                    ++i
//                }
//            } while (i <= 2)
//
//            return false

    private fun getIteratorFromDirection(pos: BlockPos, direction: Direction): Iterator<BlockPos> {
//        val var4: Iterator<BlockPos> = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 2, 4)).iterator()

        val iterator: Iterator<BlockPos> = when (direction) {
            Direction.UP -> BlockPos.iterate(
                pos.add(-4, 0, -4),
                pos.add(4, 2, 4)
            ).iterator()

            Direction.DOWN -> BlockPos.iterate(
                pos.add(-4, -2, -4),
                pos.add(4, 0, 4)
            ).iterator()

            Direction.NORTH -> BlockPos.iterate(
                pos.add(-4, -4, -2),
                pos.add(4, 4, 0)
            ).iterator()

            Direction.SOUTH -> BlockPos.iterate(
                pos.add(-4, -4, 0),
                pos.add(4, 4, 2)
            ).iterator()

            Direction.WEST -> BlockPos.iterate(
                pos.add(-2, -4, -4),
                pos.add(0, 4, 4)
            ).iterator()

            Direction.EAST -> BlockPos.iterate(
                pos.add(0, -4, -4),
                pos.add(2, 4, 4)
            ).iterator()
        }

        return iterator
    }

    /* - - - FEATURE FUNCTIONS - - - */
    @JvmStatic
    fun featureCatalystAndShrieker(context: FeatureContext<SculkPatchFeatureConfig>) {
        val config = context.config
        val random = context.random
        val world = context.world
        val blockPos = context.origin

        //blockPos is sculk block it is placing on
        if (random.nextFloat() <= config.catalystChance()) {
            extraGrowthCatalyst(world, blockPos)
        }

        val extraGrowths = config.extraRareGrowths[random]
        for (loop in 0 until extraGrowths) {
            extraGrowthShrieker(world, random, blockPos)
        }
    }


    private fun extraGrowthCatalyst(world: StructureWorldAccess, blockPos: BlockPos) {
        Direction.entries.forEach {
            val posOffset = blockPos.offset(it)
            if (
                world.getBlockState(posOffset).isFullCube(world, posOffset) &&
                (it == Direction.DOWN || world.getBlockState(blockPos).isIn(BlockTags.REPLACEABLE))
            ) {
                world.setBlockState(
                    blockPos,
                    Blocks.SCULK_CATALYST.defaultState.with(Properties.FACING, it.opposite),
                    3
                )
                return
            }
        }
    }

    private fun extraGrowthShrieker(
        world: StructureWorldAccess,
        random: RandomGenerator,
        blockPos: BlockPos
    ) {
        Direction.entries.forEach {
            val blockPosRand = blockPos.getRandomOffset(random, it)
            if (world.getBlockState(blockPosRand).isAir &&
                world.getBlockState(blockPosRand.offset(it))
                    .isSideSolidFullSquare(world, blockPosRand.offset(it), it.opposite)
            ) {
                world.setBlockState(
                    blockPosRand,
                    Blocks.SCULK_SHRIEKER.defaultState
                        .with(Properties.FACING, it.opposite)
                        .with(SculkShriekerBlock.CAN_SUMMON, true),
                    3
                )
                return
            }
        }
    }

    private fun BlockPos.getRandomOffset(random: RandomGenerator, direction: Direction): BlockPos {
        val x = random.nextInt(5) - 2
        val y = 0
        val z = random.nextInt(5) - 2
        return when (direction.axis) {
            Direction.Axis.Y -> this.add(Vec3i(x, y, z))
            Direction.Axis.X -> this.add(Vec3i(y, x, z))
            Direction.Axis.Z -> this.add(Vec3i(x, z, y))
        }
    }

}