package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.DecoratedPotBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.DecoratedPotBlockEntity
import net.minecraft.block.entity.Sherds
import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.DataComponentTypes
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.EnchantmentTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.CommonTexts
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.event.GameEvent
import java.util.stream.Stream

class DecoratedPotBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState
            .with(FACING, Direction.NORTH)
            .with(WATERLOGGED, false)
            .with(CRACKED, false)
    }

    public override fun getCodec(): MapCodec<DecoratedPotBlock> = CODEC

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        return defaultState.with(FACING, ctx.playerFacing)
            .with(WATERLOGGED, fluidState.fluid == Fluids.WATER)
            .with(CRACKED, false)
    }

    override fun onInteract(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DecoratedPotBlockEntity) {
            if (world.isClient) {
                return ItemInteractionResult.CONSUME
            } else {
                val itemStack: ItemStack = blockEntity.stack
                if (!stack.isEmpty &&
                    (itemStack.isEmpty || ItemStack.itemsAndComponentsMatch(itemStack, stack)
                            && itemStack.count < itemStack.maxCount)
                ) {
                    blockEntity.wobble(DecoratedPotBlockEntity.WobbleType.POSITIVE)
                    entity.incrementStat(Stats.USED.getOrCreateStat(stack.item))
                    val itemStack2 = stack.copyAndConsume(1, entity)
                    val ratio: Float
                    if (blockEntity.isEmpty) {
                        blockEntity.stack = itemStack2
                        ratio = itemStack2.count.toFloat() / itemStack2.maxCount.toFloat()
                    } else {
                        itemStack.increment(1)
                        ratio = itemStack.count.toFloat() / itemStack.maxCount.toFloat()
                    }

                    world.playSound(
                        null as PlayerEntity?,
                        pos,
                        SoundEvents.BLOCK_DECORATED_POT_INSERT,
                        SoundCategory.BLOCKS,
                        1.0f,
                        0.7f + 0.5f * ratio
                    )
                    if (world is ServerWorld) {
                        world.spawnParticles(
                            ParticleTypes.DUST_PLUME,
                            pos.x.toDouble() + 0.5,
                            pos.y.toDouble() + 1.2,
                            pos.z.toDouble() + 0.5,
                            7,
                            0.0,
                            0.0,
                            0.0,
                            0.0
                        )
                    }

                    blockEntity.markDirty()
                    world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
                    return ItemInteractionResult.SUCCESS
                } else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                }
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DecoratedPotBlockEntity) {
            world.playSound(
                null as PlayerEntity?,
                pos,
                SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
            )
            blockEntity.wobble(DecoratedPotBlockEntity.WobbleType.NEGATIVE)
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
            return ActionResult.SUCCESS
        } else {
            return ActionResult.PASS
        }
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean = false

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED, CRACKED)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DecoratedPotBlockEntity(pos, state)

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        ItemScatterer.scatterInventory(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun getDroppedStacks(
        state: BlockState,
        lootParameterBuilder: LootContextParameterSet.Builder
    ): List<ItemStack> {
        val blockEntity = lootParameterBuilder.getOptionalParameter(LootContextParameters.BLOCK_ENTITY)
        if (blockEntity is DecoratedPotBlockEntity) {

            lootParameterBuilder.withDynamicDrop(SHERDS) { consumer ->
                blockEntity.sherds.ordered().forEach { sherd ->
                    consumer.accept(sherd.defaultStack)
                }
            }
        }

        return super.getDroppedStacks(state, lootParameterBuilder)
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        val itemStack = player.mainHandStack
        var blockState = state
        if (itemStack.isIn(ItemTags.BREAKS_DECORATED_POTS) &&
            !EnchantmentHelper.hasTag(itemStack, EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING)
        ) {
            blockState = state.with(CRACKED, true)
            world.setBlockState(pos, blockState, 4)
        }

        return super.onBreak(world, pos, blockState, player)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    override fun getSoundGroup(state: BlockState): BlockSoundGroup {
        return if (state.get(CRACKED)) BlockSoundGroup.CRACKED_DECORATED_POT
        else BlockSoundGroup.DECORATED_POT
    }

    override fun appendTooltip(
        stack: ItemStack,
        tooltipContext: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipConfig
    ) {
        super.appendTooltip(stack, tooltipContext, tooltip, options)
        val sherds = stack.getOrDefault(DataComponentTypes.POT_DECORATIONS, Sherds.DEFAULT)
        if (sherds != Sherds.DEFAULT) {
            tooltip.add(CommonTexts.EMPTY)
            Stream.of(sherds.front(), sherds.left(), sherds.right(), sherds.back()).forEach {
                tooltip.add(
                    ItemStack(it.orElse(defaultBrick()), 1).name
                        .copyContentOnly().formatted(Formatting.GRAY)
                )
            }
        }
    }


    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        val blockPos = hit.blockPos
        if (!world.isClient && projectile.canModifyAt(world, blockPos) && projectile.canBreakBlocks(world)) {
            world.setBlockState(blockPos, state.with(CRACKED, true), 4)
            world.breakBlock(blockPos, true, projectile)
        }
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState): ItemStack {
        val var5 = world.getBlockEntity(pos)
        return if (var5 is DecoratedPotBlockEntity) {
            var5.asStack()
        } else {
            super.getPickStack(world, pos, state)
        }
    }

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    fun defaultBrick(): Item = Items.BRICK

    companion object {
        val CODEC: MapCodec<DecoratedPotBlock> = createCodec(::DecoratedPotBlock)
        val SHERDS: Identifier = Identifier.ofDefault("sherds")
        private val SHAPE: VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
        private val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val CRACKED: BooleanProperty = Properties.CRACKED
        private val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }
}
