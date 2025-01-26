package org.teamvoided.dusk_debris.fluid


import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.*
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskFluids
import org.teamvoided.dusk_debris.init.DuskParticles
import java.util.*

abstract class AcidFluid : FlowableFluid() {

    override fun flow(
        world: WorldAccess,
        pos: BlockPos,
        state: BlockState,
        direction: Direction,
        fluidState: FluidState
    ) {
        val fluidState2: FluidState = world.getFluidState(pos)
        if (this.isIn(DuskFluidTags.ACID) && fluidState2.isIn(FluidTags.WATER)) {
            val level2 = fluidState2.get(LEVEL)
            if (state.block is FluidBlock && fluidState.get(LEVEL) < level2) {
                world.setBlockState(pos, Blocks.WATER.defaultState.with(LEVEL, level2 - 1), 3);
            }
            return
        }

        super.flow(world, pos, state, direction, fluidState);
    }

    override fun getFlowing(): Fluid = DuskFluids.FLOWING_ACID

    override fun getStill(): Fluid = DuskFluids.ACID

    override fun getBucketItem(): Item = Items.WATER_BUCKET

    override fun getParticle(): ParticleEffect = ParticleTypes.DRIPPING_WATER

    override fun isInfinite(world: World): Boolean {
        return world.gameRules.getBooleanValue(GameRules.WATER_SOURCE_CONVERSION)
    }

    override fun toBlockState(state: FluidState): BlockState =
        DuskFluids.ACID_BLOCK.defaultState.with(FluidBlock.LEVEL, getBlockStateLevel(state))

    override fun matchesType(fluid: Fluid): Boolean {
        return fluid == DuskFluids.ACID || fluid == DuskFluids.FLOWING_ACID
    }

    override fun canBeReplacedWith(
        state: FluidState,
        world: BlockView,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
//        if (this.isIn(DuskFluidTags.ACID) && fluid.isIn(FluidTags.WATER)) {
//            val fluidState2: FluidState = world.getFluidState(pos.offset(direction.opposite))
//            return (fluid.getLevel() > this.getLevel(state))
//        } else
        return direction == Direction.DOWN && !fluid.isIn(DuskFluidTags.ACID_DOES_NOT_REPLACE_BELOW)
    }

    override fun getBucketFillSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL)
    }

    override fun randomDisplayTick(world: World, pos: BlockPos, state: FluidState, random: RandomGenerator) {
        if (!state.isSource && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound(
                    pos.x + 0.5,
                    pos.y + 0.5,
                    pos.z + 0.5,
                    SoundEvents.BLOCK_WATER_AMBIENT,
                    SoundCategory.BLOCKS,
                    random.nextFloat() * 0.25F + 0.75F,
                    random.nextFloat() + 0.25F,
                    false
                )
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(
                DuskParticles.UNDERACID,
                pos.x + random.nextDouble(),
                pos.y + random.nextDouble(),
                pos.z + random.nextDouble(),
                0.0,
                0.0,
                0.0
            )
        }
        val blockPosUp = pos.up()
        if (world.getBlockState(blockPosUp).isAir &&
            !world.getBlockState(blockPosUp).isOpaqueFullCube(world, blockPosUp)
        ) {
            if (random.nextInt(100) == 0) {
                val x = pos.x + random.nextDouble()
                val y = pos.y + 1.0
                val z = pos.z + random.nextDouble()
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0)
                world.playSound(
                    x, y, z,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH,
                    SoundCategory.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f,
                    0.9f + random.nextFloat() * 0.15f,
                    false
                )
            }
        }
        val blockPosDown = pos.down()

    }

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    override fun getFlowSpeed(world: WorldView): Int = 3

    override fun getLevelDecreasePerBlock(world: WorldView): Int = 1

    override fun getTickRate(world: WorldView): Int = 6

    override fun getBlastResistance(): Float = 100f

    internal class Flowing : AcidFluid() {
        override fun getLevel(state: FluidState): Int = state.get(LEVEL)

        override fun isSource(state: FluidState): Boolean = false

        override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }
    }

    internal class Still : AcidFluid() {
        override fun getLevel(state: FluidState): Int = 8
        override fun isSource(state: FluidState): Boolean = true
    }
}