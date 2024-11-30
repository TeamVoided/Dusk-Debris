package org.teamvoided.dusk_debris.block


import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import java.util.*

class BubbleBlock(settings: Settings) : Block(settings), Waterloggable, FluidDrainable {
    public override fun getCodec(): MapCodec<BubbleBlock> {
        return CODEC
    }

    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        return if (stateFrom.isOf(this)) true else super.isSideInvisible(state, stateFrom, direction)
    }

    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (!world.isClient && (!entity.type.isIn(DuskEntityTypeTags.DONT_POP_FOG_BUBBLES) && entity is LivingEntity)) {
            if ((world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING) || entity is PlayerEntity) &&
                entity.canModifyAt(world, pos)
            ) {
                world.breakBlock(pos, false)
            }
        }
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = VoxelShapes.empty()

    override fun getCameraCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = VoxelShapes.empty()

    override fun tryDrainFluid(player: PlayerEntity?, world: WorldAccess, pos: BlockPos, state: BlockState): ItemStack {
        world.setBlockState(pos, Blocks.AIR.defaultState, 11)
        if (!world.isClient) {
            world.syncWorldEvent(2001, pos, getRawIdFromState(state))
        }
        return ItemStack(Items.POWDER_SNOW_BUCKET)
    }

    override fun getBucketFillSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<BubbleBlock> = createCodec(::BubbleBlock)
        fun isBlockedByBubble(entity: Entity): Boolean {
            return if (entity.type.isIn(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
                true
            } else {
                if (entity is LivingEntity) entity.getEquippedStack(EquipmentSlot.FEET)
                    .isOf(Items.LEATHER_BOOTS) else false
            }
        }
    }
}