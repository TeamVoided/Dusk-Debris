package org.teamvoided.dusk_debris.fluid


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.*
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskFluids
import org.teamvoided.dusk_debris.init.DuskParticles
import java.util.*

abstract class AcidFluid : FlowingFluid() {

    override fun spreadTo(
        world: LevelAccessor,
        pos: BlockPos,
        state: BlockState,
        direction: Direction,
        fluidState: FluidState
    ) {
        val fluidState2: FluidState = world.getFluidState(pos)
        if (this.`is`(DuskFluidTags.ACID) && fluidState2.`is`(FluidTags.WATER)) {
            val level2 = fluidState2.getValue(LEVEL)
            if (state.block is LiquidBlock && fluidState.getValue(LEVEL) < level2) {
                world.setBlock(pos, Blocks.WATER.defaultBlockState().setValue(LEVEL, level2 - 1), 3);
            }
            return
        }

        super.spreadTo(world, pos, state, direction, fluidState);
    }

    override fun getFlowing(): Fluid = DuskFluids.FLOWING_ACID

    override fun getSource(): Fluid = DuskFluids.ACID

    override fun getBucket(): Item = Items.WATER_BUCKET

    override fun getDripParticle(): ParticleOptions = ParticleTypes.DRIPPING_WATER

    override fun canConvertToSource(world: Level): Boolean {
        return world.gameRules.getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION)
    }

    override fun createLegacyBlock(state: FluidState): BlockState =
        DuskFluids.ACID_BLOCK.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state))

    override fun isSame(fluid: Fluid): Boolean {
        return fluid == DuskFluids.ACID || fluid == DuskFluids.FLOWING_ACID
    }

    override fun canBeReplacedWith(
        state: FluidState,
        world: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
//        if (this.isIn(DuskFluidTags.ACID) && fluid.isIn(FluidTags.WATER)) {
//            val fluidState2: FluidState = world.getFluidState(pos.offset(direction.opposite))
//            return (fluid.getLevel() > this.getLevel(state))
//        } else
        return direction == Direction.DOWN && !fluid.`is`(DuskFluidTags.ACID_DOES_NOT_REPLACE_BELOW)
    }

    override fun getPickupSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.BUCKET_FILL)
    }

    override fun animateTick(world: Level, pos: BlockPos, state: FluidState, random: RandomSource) {
        if (!state.isSource && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playLocalSound(
                    pos.x + 0.5,
                    pos.y + 0.5,
                    pos.z + 0.5,
                    SoundEvents.WATER_AMBIENT,
                    SoundSource.BLOCKS,
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
        val blockPosUp = pos.above()
        if (world.getBlockState(blockPosUp).isAir &&
            !world.getBlockState(blockPosUp).isSolidRender(world, blockPosUp)
        ) {
            if (random.nextInt(100) == 0) {
                val x = pos.x + random.nextDouble()
                val y = pos.y + 1.0
                val z = pos.z + random.nextDouble()
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0)
                world.playLocalSound(
                    x, y, z,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f,
                    0.9f + random.nextFloat() * 0.15f,
                    false
                )
            }
        }
        val blockPosDown = pos.below()

    }

    override fun beforeDestroyingBlock(world: LevelAccessor, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropResources(state, world, pos, blockEntity)
    }

    override fun getSlopeFindDistance(world: LevelReader): Int = 3

    override fun getDropOff(world: LevelReader): Int = 1

    override fun getTickDelay(world: LevelReader): Int = 6

    override fun getExplosionResistance(): Float = 100f

    internal class Flowing : AcidFluid() {
        override fun getAmount(state: FluidState): Int = state.getValue(LEVEL)

        override fun isSource(state: FluidState): Boolean = false

        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }
    }

    internal class Still : AcidFluid() {
        override fun getAmount(state: FluidState): Int = 8
        override fun isSource(state: FluidState): Boolean = true
    }
}