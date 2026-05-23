package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.tags.EnchantmentTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.DecoratedPotBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity
import net.minecraft.world.level.block.entity.PotDecorations
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.stream.Stream

class DecoratedPotBlock(settings: Properties) : BaseEntityBlock(settings), SimpleWaterloggedBlock {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(CRACKED, false)
        )
    }

    public override fun codec(): MapCodec<DecoratedPotBlock> = CODEC

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        return defaultBlockState().setValue(FACING, ctx.horizontalDirection)
            .setValue(WATERLOGGED, fluidState.type == Fluids.WATER)
            .setValue(CRACKED, false)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DecoratedPotBlockEntity) {
            if (world.isClientSide) {
                return ItemInteractionResult.CONSUME
            } else {
                val itemStack: ItemStack = blockEntity.theItem
                if (!stack.isEmpty &&
                    (itemStack.isEmpty || ItemStack.isSameItemSameComponents(itemStack, stack)
                            && itemStack.count < itemStack.maxStackSize)
                ) {
                    blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE)
                    entity.awardStat(Stats.ITEM_USED.get(stack.item))
                    val itemStack2 = stack.consumeAndReturn(1, entity)
                    val ratio: Float
                    if (blockEntity.isEmpty) {
                        blockEntity.theItem = itemStack2
                        ratio = itemStack2.count.toFloat() / itemStack2.maxStackSize.toFloat()
                    } else {
                        itemStack.grow(1)
                        ratio = itemStack.count.toFloat() / itemStack.maxStackSize.toFloat()
                    }

                    world.playSound(
                        null as Player?,
                        pos,
                        SoundEvents.DECORATED_POT_INSERT,
                        SoundSource.BLOCKS,
                        1.0f,
                        0.7f + 0.5f * ratio
                    )
                    if (world is ServerLevel) {
                        world.sendParticles(
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

                    blockEntity.setChanged()
                    world.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
                    return ItemInteractionResult.SUCCESS
                } else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                }
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        }
    }

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DecoratedPotBlockEntity) {
            world.playSound(
                null as Player?,
                pos,
                SoundEvents.DECORATED_POT_INSERT_FAIL,
                SoundSource.BLOCKS,
                1.0f,
                1.0f
            )
            blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE)
            world.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
            return InteractionResult.SUCCESS
        } else {
            return InteractionResult.PASS
        }
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean = false

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPE

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED, CRACKED)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DecoratedPotBlockEntity(pos, state)

    override fun onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moved: Boolean) {
        Containers.dropContentsOnDestroy(state, newState, world, pos)
        super.onRemove(state, world, pos, newState, moved)
    }

    override fun getDrops(
        state: BlockState,
        lootParameterBuilder: LootParams.Builder
    ): List<ItemStack> {
        val blockEntity = lootParameterBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
        if (blockEntity is DecoratedPotBlockEntity) {

            lootParameterBuilder.withDynamicDrop(SHERDS) { consumer ->
                blockEntity.decorations.ordered().forEach { sherd ->
                    consumer.accept(sherd.defaultInstance)
                }
            }
        }

        return super.getDrops(state, lootParameterBuilder)
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        val itemStack = player.mainHandItem
        var blockState = state
        if (itemStack.`is`(ItemTags.BREAKS_DECORATED_POTS) &&
            !EnchantmentHelper.hasTag(itemStack, EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING)
        ) {
            blockState = state.setValue(CRACKED, true)
            world.setBlock(pos, blockState, 4)
        }

        return super.playerWillDestroy(world, pos, blockState, player)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun getSoundType(state: BlockState): SoundType {
        return if (state.getValue(CRACKED)) SoundType.DECORATED_POT_CRACKED
        else SoundType.DECORATED_POT
    }

    override fun appendHoverText(
        stack: ItemStack,
        tooltipContext: Item.TooltipContext,
        tooltip: MutableList<Component>,
        options: TooltipFlag
    ) {
        super.appendHoverText(stack, tooltipContext, tooltip, options)
        val sherds = stack.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY)
        if (sherds != PotDecorations.EMPTY) {
            tooltip.add(CommonComponents.EMPTY)
            Stream.of(sherds.front(), sherds.left(), sherds.right(), sherds.back()).forEach {
                tooltip.add(
                    ItemStack(it.orElse(defaultBrick()), 1).hoverName
                        .plainCopy().withStyle(ChatFormatting.GRAY)
                )
            }
        }
    }


    override fun onProjectileHit(world: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
        val blockPos = hit.blockPos
        if (!world.isClientSide && projectile.mayInteract(world, blockPos) && projectile.mayBreak(world)) {
            world.setBlock(blockPos, state.setValue(CRACKED, true), 4)
            world.destroyBlock(blockPos, true, projectile)
        }
    }

    override fun getCloneItemStack(world: LevelReader, pos: BlockPos, state: BlockState): ItemStack {
        val var5 = world.getBlockEntity(pos)
        return if (var5 is DecoratedPotBlockEntity) {
            var5.potAsItem
        } else {
            super.getCloneItemStack(world, pos, state)
        }
    }

    override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    override fun getAnalogOutputSignal(state: BlockState, world: Level, pos: BlockPos): Int {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos))
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
    }

    fun defaultBrick(): Item = Items.BRICK

    companion object {
        val CODEC: MapCodec<DecoratedPotBlock> = simpleCodec(::DecoratedPotBlock)
        val SHERDS: ResourceLocation = ResourceLocation.withDefaultNamespace("sherds")
        private val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
        private val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        val CRACKED: BooleanProperty = BlockStateProperties.CRACKED
        private val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}
